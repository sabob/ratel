/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.ant;

import com.google.ratel.core.*;
import com.google.ratel.deps.io.*;
import com.google.ratel.deps.jackson.databind.*;
import com.google.ratel.deps.lang3.*;
import com.google.ratel.service.classdata.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 *
 */
public class ClassMetadataTask extends Task {

    // -------------------------------------------------------------- Variables
    /**
     * Directory where filenames can be found.
     */
    private File dir;

    /**
     * File where class data be written to.
     */
    private File output;

    protected Class annotationClass = com.google.ratel.core.RatelService.class;

    /**
     * The include filter to use when scanning {@link #dir}.
     */
    private String includes;

    /**
     * The exclude filter to use when scanning {@link #dir}.
     */
    private String excludes;

    /**
     * Default FileSet associated with the DeployTask element that specifies jars and folders to check for filenames.
     */
    private FileSet defaultFileSet;

    /**
     * List of nested FileSets.
     */
    private List<FileSet> fileSets = new ArrayList<FileSet>();

    /**
     * Buffer containing a listing of all the jar and folder sources that contained deployable resources.
     */
    private StringBuilder deployableSourceListing = new StringBuilder();

    /**
     * The report content.
     */
    private Writer reportContent = new StringWriter();

    // ----------------------------------------------------------- Constructors
    /**
     * Creates a default DeployTask instance.
     */
    public ClassMetadataTask() {
        defaultFileSet = new FileSet();
    }

    // ------------------------------------------------------ Public properties
    /**
     * Add the given fileSet to the list of {@link #fileSets}.
     *
     * @param fileSet the fileSet to add to the list of {@link #fileSets}.
     */
    public void addFileSet(FileSet fileSet) {
        if (!fileSets.contains(fileSet)) {
            fileSets.add(fileSet);
        }
    }

    /**
     * Return the list of the {@link #fileSets}.
     *
     * @return the list of fileSets
     */
    public List<FileSet> getFileSets() {
        return fileSets;
    }

    /**
     * Set the directory consisting of JARs and folders where filenames are deployed from.
     *
     * @param dir the directory consisting of JARs and folders where filenames are deployed from.
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    public void setAnnotation(String annotationStr) {
        System.out.println("Annotation set to : " + annotationStr);

        try {
            //TaskUtils.classForName(typeStr); // didn't work...

            this.annotationClass = Class.forName(annotationStr);

        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Could not find type: " + annotationStr, ex);
        }
    }

    /**
     * Set the output file where classdata are written to.
     *
     * @param output Set the output file where classdata are written to.
     */
    public void setOutput(File output) {
        this.output = output;
    }

    /**
     * Set the Ant <tt>excludes</tt> pattern for the source {@link #dir}.
     *
     * @param excludes the Ant <tt>excludes</tt> pattern for the source {@link #dir}.
     */
    public void setExcludes(String excludes) {
        this.excludes = excludes;
        defaultFileSet.setExcludes(excludes);
    }

    /**
     * Set the Ant <tt>includes</tt> pattern for the source {@link #dir}.
     *
     * @param includes the Ant <tt>includes</tt> pattern for the source {@link #dir}.
     */
    public void setIncludes(String includes) {
        this.includes = includes;
        defaultFileSet.setIncludes(includes);
    }

    /**
     * Return the Ant <tt>includes</tt> pattern for the source {@link #dir}.
     *
     * @return the Ant <tt>includes</tt> pattern for the source {@link #dir}.
     */
    public String getIncludes() {
        return includes;
    }

    // --------------------------------------------------------- Public Methods
    /**
     * Execute the task.
     *
     * @throws BuildException if the build fails
     */
    @Override
    public void execute() throws BuildException {

        if (output == null) {
            throw new BuildException("output attribute must be set!");
        }

        if (!isDir(dir) && getFileSets().isEmpty()) {
            throw new BuildException("no input dirs specified.");
        }

        if (dir != null && dir.exists()) {
            defaultFileSet.setDir(dir);
            getFileSets().add(defaultFileSet);
            defaultFileSet.setDefaultexcludes(true);
            if (TaskUtils.isBlank(getIncludes())) {
                // Set default standard includes
                defaultFileSet.setIncludes("**/*.jar, classes");
            }
        }

        Set<Class> classes = new HashSet<Class>();

        for (FileSet fileSet : getFileSets()) {
            DirectoryScanner directoryScanner = fileSet.getDirectoryScanner(getProject());
            String files[] = directoryScanner.getIncludedFiles();
            String dirs[] = directoryScanner.getIncludedDirectories();
            String resources[] = (String[]) TaskUtils.addAll(files, dirs);

            System.out.println("Files[" + resources.length + "] in fileset.getDir(): " + fileSet.getDir());
            //deployResources(fileSet.getDir(), resources);

            addClasses(fileSet.getDir(), resources, classes);

        }

        writeMetadata(classes, output);
    }

    protected void addClasses(File dir, String filenames[], Set<Class> classes) {
        System.out.println("Find RatelServices in : " + dir.getAbsolutePath());

        try {
            for (String filename : filenames) {
                File file = new File(dir, filename);

                if (filename.indexOf(".jar") >= 0) {
                    addClassesInJar(file, classes);
                } else {
                    addClassesInDir(file, classes);
                }
            }

        } catch (IOException ioe) {
            throw new BuildException(ioe.getClass().getName() + ":" + ioe.getMessage(), ioe);
        }
    }

    protected void addClassesInDir(File dir, Set<Class> classes) throws IOException {

        List<File> files = TaskUtils.listFiles(dir, new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                //System.out.println("accept:" + file + "   " + name + " " + file.isDirectory());
                if (file.isDirectory()) {
                    return false;
                }
                return file.getName().endsWith(".class");
            }
        });

        for (File file : files) {
            String path = file.getPath();
            path = path.replace("\\", "/");
            int start = path.indexOf("WEB-INF/classes/");
            if (start >= 0) {

                String className = file.getPath().substring(start + "WEB-INF/classes/".length());
                className = className.replace("\\", ".");

                // remove .class extension
                className = className.substring(0, className.length() - 6);

                addClass(className, classes, dir.getCanonicalPath());
            } else {
                System.out.println("Skipping class '" + file.getPath()
                    + "'. Only classes under the 'WEB-INF/classes' folder will be checked.");
            }
        }
    }

    protected void addClassesInJar(File jar, Set<Class> classes) throws IOException {

        System.out.println("Find RatelServices in jar: " + jar.getName());
        JarFile jarFile = null;

        try {
            jarFile = new JarFile(jar);

            Enumeration<JarEntry> en = jarFile.entries();

            while (en.hasMoreElements()) {
                JarEntry jarEntry = en.nextElement();

                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }

                // remove .class
                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                addClass(className, classes, jar.getCanonicalPath());

            }
        } finally {
            TaskUtils.close(jarFile);
        }
    }

    protected void addClass(String className, Set<Class> classes, String location) {

        className = className.replace('/', '.');

        Class c = loadClass(className);
        if (c == null) {
            return;
        }

        if (accept(c)) {
            if (classes.add(c)) {
                System.out.println("    Found RatelService, '" + c.getName() + "', in -> " + location);
            }
        }
    }

    protected boolean accept(Class cls) {
        if (cls == null) {
            return false;
        }

        if (annotationClass != cls) {
            if (cls.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }

    protected Class loadClass(String className) {
        try {

            Class cls = Class.forName(className, false, getClass().getClassLoader());
            //Class cls2 = Class.forName(className, false, getClass().getClassLoader());
            //System.out.println("HUH: " + (cls == cls2));

            return cls;
        } catch (ClassNotFoundException ignore) {
            //ignore.printStackTrace();
        } catch (NoClassDefFoundError ignore) {
            //ignore.printStackTrace();
        }

        return null;
    }

    protected void writeMetadata(Set<Class> classes, File outputFile) {
        Set<Class> classSet = new HashSet<Class>();

        for (Class serviceClass : classes) {
            classSet.add(serviceClass);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {

            String json = mapper.writeValueAsString(classSet);
            FileUtils.write(outputFile, json);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // -------------------------------------------------------- Private Methods
    /**
     * Return true if the given file is a directory, false otherwise.
     *
     * @param file file to check if its a directory
     * @return true if the given file is a directory, false otherwise
     */
    private boolean isDir(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return false;
        }
        return true;
    }

    public static ClassData createClassData(Class<?> serviceClass, List<String> packageNames) {

        ClassData classData = new ClassData();
        classData.setServiceClass(serviceClass);
        Path pathAnnot = serviceClass.getAnnotation(Path.class);

        if (pathAnnot != null) {
            String path = pathAnnot.value().trim();
            classData.setServicePath(path);

        } else {
            String serviceName = serviceClass.getName();
            for (String packageName : packageNames) {
                int origLength = serviceName.length();
                serviceName = StringUtils.remove(serviceName, packageName);
                if (serviceName.length() != origLength) {
                    break;
                }
            }

            String servicePath = serviceName.replace(".", "/");
            if (!servicePath.startsWith("/")) {
                StringBuilder sb = new StringBuilder();
                sb.append("/").append(servicePath);
                servicePath = sb.toString();
            }

            classData.setServicePath(servicePath);
        }

        return classData;
    }
}
