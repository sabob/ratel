package com.google.ratel.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Provides resourceName deployment operations to copying resources from a source jar/folder to a target folder.
 * <p/>
 * This class also tracks which resources were deployed, skipped or outdated. This information can then be used to write a summary report of
 * the resources.
 */
class Deploy {

    // --------------------------------------------------------- Public Methods
    private Class annotationClass;

    public Deploy(Class annotationClass) {
        this.annotationClass = annotationClass;

    }

    /**
     * Deploy all resources from the jar to the given target folder.
     *
     * @param jar the jar file to deploy resources from
     * @param target the target folder to deploy resources to
     * @return true if resources was deployed, false otherwise
     * @throws IOException if a IO exception occurs
     */
    public boolean deployResourcesInJar(File jar, File target) throws IOException {
        boolean hasDeployed = false;

        // Mark the size of the reportEntries before deployment
        //int before1 = deployed.size();
        //int before2 = outdated.size();

        // example jar    -> lib/click-core-2.1.0.jar
        // example target -> c:/dev/webapp/
        if (jar == null) {
            throw new IllegalArgumentException("Jar cannot be null");
        }

        deployResourcesInJar(jar, target, "META-INF/resources/");
        deployResourcesInJar(jar, target, "META-INF/web/");

        //int after1 = deployed.size();
        //int after2 = outdated.size();

        // If new reportEntries was added, set hasDeployeed to true
        //if (before1 != after1 || before2 != after2) {
        //  hasDeployed = true;
        // }
        return hasDeployed;
    }

    /**
     * Deploy resources from a jar to a target folder for the given resourceDirectory, 'META-INF/resources'.
     *
     * @param jarFile the jar file to deploy resources from
     * @param target the target folder to deploy resources to
     * @param resourceDirectory the directory where resources are located in
     * @throws IOException if a IO exception occurs
     */
    private void deployResourcesInJar(File jar, File target,
        final String resourceDirectory) throws IOException {

        JarFile jarFile = null;

        try {
            jarFile = new JarFile(jar);
            //System.out.println("Jar: " + jar.getName());

            if (jarFile.getJarEntry(resourceDirectory) == null) {
                return;
            }

            JarEntry jarEntry = null;

            // Indicates whether feedback should be logged on the jar being
            // deployed
            boolean logFeedback = true;
            Enumeration<JarEntry> en = jarFile.entries();

            while (en.hasMoreElements()) {
                jarEntry = en.nextElement();

                // jarEntryName example -> META-INF/resources/click/table.css
                String jarEntryName = jarEntry.getName();

                // Only deploy resources from "META-INF/resources/"
                int pathIndex = jarEntryName.indexOf(resourceDirectory);
                if (pathIndex == 0) {
                    if (logFeedback) {
                        System.out.println("deploy files from jar -> " + jar.getCanonicalPath());

                        // Only provide feedback once per jar
                        logFeedback = false;
                    }
                    pathIndex += resourceDirectory.length();
                    // resourceName example -> click/table.css
                    String resourceName = jarEntryName.substring(pathIndex);
                    int index = resourceName.lastIndexOf('/');

                    File targetDir = new File(target.getPath());
                    if (index != -1) {
                        // resourceDir example -> click
                        String resourceDir =
                            resourceName.substring(0, index);
                        targetDir = new File(targetDir, resourceDir);
                    }

                    InputStream inputStream = null;
                    try {
                        inputStream = jarFile.getInputStream(jarEntry);
                        byte[] resourceData = TaskUtils.toByteArray(inputStream);

                        // Copy resources to web folder
                        
                        deployFile(jarEntryName, resourceData, targetDir);

                    } finally {
                        TaskUtils.close(inputStream);
                    }
                }
            }
        } finally {
            TaskUtils.close(jarFile);
        }
    }

    /**
     * Deploy all resources from the source folder to the given target folder.
     *
     * @param source the folder to deploy resources from
     * @param target the target folder to deploy resources to
     * @return true if resources was deployed, false otherwise
     * @throws IOException if a IO exception occurs
     */
    public boolean deployResourcesInDir(File source, File target) throws IOException {
        boolean hasDeployed = false;

        // Mark the size of the reportEntries before deployment
        //int before1 = deployed.size();
        //int before2 = outdated.size();

        // example source -> c:/source/webapp/WEB-INF/classes
        // example target -> c:/dev/webapp/
        if (source == null) {
            throw new IllegalArgumentException("Jar cannot be null");
        }

        if (source.exists()) {
            //deployResourcesInDir(source, target, "META-INF/resources");
            //deployResourcesInDir(source, target, "META-INF/web");
            deployResourcesInDir(source, target, "");
        }

        //int after1 = deployed.size();
        //int after2 = outdated.size();

        // If new reportEntries was added, set hasDeployeed to true
        //if (before1 != after1 || before2 != after2) {
        //  hasDeployed = true;
        //}

        return hasDeployed;
    }

    /**
     * Deploy resources from a source folder to a target folder for the given resourceDirectory, 'META-INF/resources'.
     *
     * @param source the source folder to deploy resources from
     * @param target the target folder to deploy resources to
     * @param resourceDirectory the directory where resources are located in
     * @throws IOException if a IO exception occurs
     */
    private void deployResourcesInDir(File source, File target,
        final String resourceDirectory) throws IOException {

        File dir = new File(source, resourceDirectory);

        Iterator files = TaskUtils.listFiles(dir, new FilenameFilter() {
            public boolean accept(File file, String name) {
                //System.out.println("accept:" + file + "   " + name + " " + file.isDirectory());
                if (file.isDirectory()) {
                    return false;
                }

                String path = file.getAbsolutePath();
                path = path.replace('\\', '/');
                return path.indexOf(resourceDirectory) >= 0;
            }
        }).iterator();

        boolean logFeedback = true;
        while (files.hasNext()) {
            // example file -> c:/source/webapp/WEB-INF/classes/META-INF/resources/click/table.css
            File file = (File) files.next();

            // Guard against loading folders -> META-INF/resources/click/
            if (file.isDirectory()) {
                continue;
            }

            String fileName = file.getCanonicalPath().replace('\\', '/');

            // Only deploy resources from "META-INF/resources/"
            int pathIndex = fileName.indexOf(resourceDirectory);
            if (pathIndex != -1) {
                if (logFeedback) {
                    //System.out.println("load files from folder -> " + source.getAbsolutePath());

                    // Only provide feedback once per source
                    logFeedback = false;
                }
                pathIndex += resourceDirectory.length();

                //fileName = fileName.substring(pathIndex);
                //int index = fileName.lastIndexOf('/');

                //File targetDir = new File(target.getPath());
                //if (index != -1) {
                    // resourceDir example -> click
                    //String resourceDir = fileName.substring(0, index);
                    //targetDir = new File(targetDir, resourceDir);
                //}

                //InputStream inputStream = null;
                try {
                    //inputStream = new FileInputStream(file);
                    //byte[] resourceData = TaskUtils.toByteArray(inputStream);

                    if (file.getName().indexOf(".class") >= 0) {
                        //System.out.println("GOES: " +file.getAbsolutePath() + ", " + file.getParent());
                        int start = file.getPath().indexOf("classes");
                        if (start >= 0) {
                            String classname = file.getPath().substring(start + 8);
                            classname = classname.replace("\\", ".");
                            classname = classname.substring(0, classname.length() - 6);
                            //System.out.println("Goes: "+ classname);

                            try {
                                Class cls = Class.forName(classname, false, getClass().getClassLoader());
                                //Class cls = Class.forName(classname);
                                //System.out.println("Goes: "+ cls + " " + annotationClass);
                                if (annotationClass != cls) {
                                    if (cls.isAnnotationPresent(annotationClass)) {
                                        //Annotation a = type.getAnnotation(cls);

                                        String msg = " class '" + classname + "' is annotated with " + annotationClass;
                                        System.out.println("HIHI: " + msg);
                                    }
                                }
                            } catch (ClassNotFoundException ignore) {
                                //ignore.printStackTrace();
                            } catch (NoClassDefFoundError ignore) {
                                //ignore.printStackTrace();
                            }

                        }
                        //TaskUtils.classForName();
                    }


                    // Copy resources to web folder
                    //deployFile(fileName, resourceData, targetDir);

                } finally {
                    //TaskUtils.close(inputStream);
                }
            }
        }
    }

    /**
     * Deploy a resource consisting of the resourceName and resourceData to the target folder.
     *
     * @param resourceName the name of the resource to deploy
     * @param resourceData the resource data as a byte array
     * @param target the target folder to deploy the resource to
     * @throws IOException if a IO exception occurs
     */
    private void deployFile(String resourceName, byte[] resourceData, File target) {

        if (TaskUtils.isBlank(resourceName)) {
            String msg = "resource parameter cannot be empty";
            throw new IllegalArgumentException(msg);
        }

        if (target == null) {
            String msg = "Null targetDir parameter";
            throw new IllegalArgumentException(msg);
        }

        if (resourceData == null) {
            String msg = "Null resourceData parameter";
            throw new IllegalArgumentException(msg);
        }

        try {

            // Create files deployment directory
            if (!target.mkdirs()) {
                if (!target.exists()) {
                    String msg = "could not create deployment directory: " + target.getAbsolutePath();
                    throw new IOException(msg);
                }
            }

            String destination = resourceName;
            int index = resourceName.lastIndexOf('/');
            if (index != -1) {
                destination = resourceName.substring(index + 1);
            }

            File destinationFile = new File(target, destination);

            if (destinationFile.exists()) {

                // Skip directories
                if (!destinationFile.isDirectory()) {

                    InputStream existingResource = new FileInputStream(
                        destinationFile);
                    try {
                        byte[] existingResourceData =
                            TaskUtils.toByteArray(existingResource);

                        boolean contentEquals = TaskUtils.areEqual(resourceData,
                                                                   existingResourceData);

                        if (!contentEquals) {
                            // Indicate that an updated version of the resourceName
                            // is available
                            //outdated.add(new DeployReportEntry(resourceName, removeResourceDirectoryPrefix(resourceName)));
                        }

                    } finally {
                        TaskUtils.close(existingResource);
                    }
                }

            } else {

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(destinationFile);
                    fos.write(resourceData, 0, resourceData.length);

                    int lastIndex =
                        destination.lastIndexOf(File.separatorChar);
                    if (lastIndex != -1) {
                        destination =
                            destination.substring(lastIndex + 1);
                    }
                    //deployed.add(new DeployReportEntry(resourceName, removeResourceDirectoryPrefix(resourceName)));

                } finally {
                    TaskUtils.close(fos);
                }
            }

        } catch (IOException ioe) {
            String msg =
                "error occured deploying resource " + resourceName
                + ", error " + ioe;
            throw new RuntimeException(msg, ioe);

        } catch (SecurityException se) {
            String msg =
                "error occured deploying resource " + resourceName
                + ", error " + se;
            throw new RuntimeException(msg, se);
        }
    }

    /**
     * Remove the resource directory prefix, 'META-INF/resources' from the given resourceName.
     *
     * @param resourceName the resource name from which the prefix must be removed
     * @return the resourceName without the resource directory prefix
     */
    private String removeResourceDirectoryPrefix(String resourceName) {
        if (resourceName.startsWith("META-INF/resources")) {
            resourceName = resourceName.replace("META-INF/resources", "");
        } else {
            resourceName = resourceName.replace("META-INF/web", "");
        }
        return resourceName;
    }
}
