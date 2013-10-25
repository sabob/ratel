/*
 * The contents of this file are subject to the Sapient Public License
 * Version 1.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://carbon.sf.net/License.html.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is The Carbon Component Framework.
 *
 * The Initial Developer of the Original Code is Sapient Corporation
 *
 * Copyright (C) 2003 Sapient Corporation. All Rights Reserved.
 */
package com.google.ratel.util;

import com.google.ratel.*;
import com.google.ratel.service.log.*;
import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.ServletContext;

/**
 * Scans the classpath looking for classes annotated with the given {@link #annotationClass}.Optionally the classes must be contained in a
 * specified {@link #packageNames package}.
 */
public class ClassPathScanner {

    /**
     * The annotation class to look for on classes on the class path.
     */
    protected Class annotationClass = null;

    /**
     * The list of packages to search for classes. Classes not in these packages will be ignored.
     */
    protected List<String> packageNames = null;

    /**
     * The Set of classes found.
     */
    protected Set<Class> classes = new HashSet<Class>();

    /**
     * The ServletContext to scan for classes.
     */
    protected ServletContext servletContext;

    /**
     * Constructs a new ClassFinder which scans the classpath for classes of the provided ServletContext. All classes found will be added.
     *
     * @param servletContext the servletContext to scan for classes
     */
    public ClassPathScanner(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Constructs a new ClassFinder which scans the classpath of the provided ServletContext. Only classes annotated with the
     * annotationClass will be added.
     *
     * @param servletContext the servletContext to scan for classes
     * @param annotationClass classes annotated with this annotationClass will be added
     */
    public ClassPathScanner(ServletContext servletContext, Class annotationClass) {
        this.servletContext = servletContext;
        this.annotationClass = annotationClass;

    }

    /**
     * Constructs a new ClassFinder which scans the classpath of the provided ServletContext. Only classes annotated with the
     * annotationClass will be added.
     *
     * @param servletContext the servletContext to scan for classes
     * @param annotationClass classes annotated with this annotationClass will be added
     */
    public ClassPathScanner(ServletContext servletContext, Class annotationClass, List<String> packageNames) {
        this.servletContext = servletContext;
        this.annotationClass = annotationClass;
        this.packageNames = packageNames;
    }

    public ClassPathScanner(ServletContext servletContext, Class annotationClass, String... packageNames) {
        this.servletContext = servletContext;
        this.annotationClass = annotationClass;
        this.packageNames = Arrays.asList(packageNames);
    }

    public Set<Class> scan() {

        final String javaClassPath = System.getProperty("java.class.path");
                String pathSeparator = System.getProperty("path.separator");
        StringTokenizer classPaths = new StringTokenizer(javaClassPath, pathSeparator);

        List<File> locations = new ArrayList<File>();
        while (classPaths.hasMoreTokens()) {
            String path = classPaths.nextToken();

            File location = new File(path);
            locations.add(location);
        }

        if (servletContext != null) {

            String classesDirStr = servletContext.getRealPath("/WEB-INF/classes");
            File classesDir = new File(classesDirStr);
            if (classesDir.exists()) {
                System.out.println("Adding the path " + classesDir.getAbsolutePath()
                    + " to the list of entries that will be searched for Services");
                locations.add(classesDir);
            } else {
                System.out.println("The path '/WEB-INF/classes' does not exist and will not be searched for Services");
            }
            String libDirStr = servletContext.getRealPath("/WEB-INF/lib");
            File libDir = new File(libDirStr);
            if (libDir.exists()) {
                File[] children = libDir.listFiles();
                System.out.println("Adding the path " + libDir.getAbsolutePath()
                    + " to the list of entries that will be searched for Services");
                locations.add(libDir);

                for (File child : children) {
                    if (isArchive(child.getName())) {
                        locations.add(child);
                    }
                }
            } else {
                System.out.println("The path '/WEB-INF/lib' does not exist and will not be searched for Services");
            }
        }

        for (File path : locations) {
            processFile(path.getAbsolutePath(), "");
        }

        return this.classes;
    }

    protected boolean accept(String name) {
        if (this.packageNames == null || this.packageNames.isEmpty()) {
            return true;
        }

        for (String packageName : packageNames) {
            if (packageName != null) {
                if (name.indexOf(packageName) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected void addClass(String className) {

        boolean accept = accept(className);

        if (accept) {

            Class<?> cls = classForName(className);

            if (cls == null) {
                return;
            }

            if (this.annotationClass == null) {
                this.classes.add(cls);

            } else if (cls.isAnnotationPresent(annotationClass)) {
                this.classes.add(cls);
            }
        }
    }

    /**
     * 
     * @param base
     * @param current 
     */
    protected void processFile(String base, String current) {
        File currentDirectory = new File(base + File.separatorChar + current);

        // Handle special for archives
        if (isArchive(currentDirectory.getName())) {
            try {
                processZip(new ZipFile(currentDirectory));
            } catch (Exception e) {
                // The directory was not found so the classpath was probably in
                // error or we don't have rights to it
            }
            return;
        } else {

            Set directories = new HashSet();

            File[] children = currentDirectory.listFiles();

            // if no children, return
            if (children == null || children.length == 0) {
                return;
            }

            // check for classfiles
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                if (child.isDirectory()) {
                    directories.add(children[i]);
                } else {
                    if (child.getName().endsWith(".class")) {
                        String className = getClassName(
                            current + ((current == "") ? "" : File.separator) + child.getName());
                        addClass(className);
                    }
                }
            }

            //call process file on each directory.  This is an iterative call!!
            for (Iterator i = directories.iterator(); i.hasNext();) {
                processFile(base, current + ((current == "") ? "" : File.separator) + ((File) i.next()).getName());
            }
        }
    }

    /**
     * <p>
     * Looks at the name of a file to determine if it is an archive</p>
     *
     * @param name the name of a file
     * @return true if a file in the classpath is an archive such as a Jar or Zip file
     */
    protected boolean isArchive(String name) {
        return name.endsWith(".jar") || (name.endsWith(".zip"));
    }

    /**
     * <p>
     * Returns the Fully Qualified Class name of a class from it's path
     *
     * @param fileName the full path to a class
     * @return the FQN of a class
     */
    protected String getClassName(String fileName) {
        String newName = fileName.replace(File.separatorChar, '.');
        // Because zipfiles don't have platform specific seperators
        newName = newName.replace('/', '.');
        return newName.substring(0, fileName.length() - 6);
    }

    /**
     * <P>
     * Iterates through the files in a zip looking for files that may be classes. This is not recursive as zip's in zip's are not searched
     * by the classloader either.</p>
     *
     * @param file The ZipFile to be searched
     */
    protected void processZip(ZipFile file) {
        Enumeration files = file.entries();

        while (files.hasMoreElements()) {
            Object tfile = files.nextElement();
            ZipEntry child = (ZipEntry) tfile;
            if (child.getName().endsWith(".class")) {
                addClass(getClassName(child.getName()));
            }
        }
    }

    protected Class classForName(String classname) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class<?> cls = Class.forName(classname, true, classLoader);
            return cls;

        } catch (Exception e) {
            LogService log = getLogService();
            if (log != null) {
                log.error("Could not load class " + classname, e);
            }
        }
        return null;
    }

    protected LogService getLogService() {
        if (Context.hasContext()) {
            Context context = Context.getContext();
            LogService logService = context.getRatelConfig().getLogService();
            return logService;
        }
        return null;
    }
    
}
