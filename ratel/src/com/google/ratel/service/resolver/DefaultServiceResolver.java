package com.google.ratel.service.resolver;

import com.google.ratel.RatelConfig;
import com.google.ratel.core.*;
import java.util.*;
import java.util.concurrent.*;
import javax.servlet.*;
import com.google.ratel.service.classdata.ClassData;
import com.google.ratel.service.classdata.MethodData;
import com.google.ratel.util.RatelUtils;
import com.google.ratel.service.classdata.ClassDataService;
import com.google.ratel.deps.lang3.StringUtils;
import com.google.ratel.service.classdata.*;
import com.google.ratel.util.Constants;
import java.util.Map.Entry;
import javax.servlet.http.*;

/**
 *
 */
public class DefaultServiceResolver implements ServiceResolver {

    /**
     * The ServletContext instance.
     */
    ServletContext servletContext;

    /**
     * The map of Service classes keyed on path.
     */
    protected Map<String, ClassData> serviceByPathMap = new ConcurrentHashMap<String, ClassData>();

    //protected ClassDataService classDataService;
    // Cache check to ensure we only scan for all services once
    protected boolean resolvedAllServices = false;

    /**
     * The list of page packages.
     */
    protected List<String> packageNames = new ArrayList<String>();

    protected Mode mode;

    protected RatelConfig ratelConfig;

    public DefaultServiceResolver() {
    }

    @Override
    public void onInit(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.ratelConfig = RatelUtils.getRatelConfig(servletContext);
    }

    @Override
    public void onDestroy(ServletContext servletContext) {
        this.ratelConfig = null;
        this.servletContext = null;
    }

    @Override
    public String resolvePath(HttpServletRequest request) {
        String path = RatelUtils.getResourcePath(request);
        return path;
    }

    @Override
    public RequestTargetData resolveTarget(HttpServletRequest request) {

        String path = resolvePath(request);

        if (isHelpRequest(request, path)) {
            RequestTargetData target = new RequestTargetData();
            target.setHelpRequest(true);
            return target;
        }

        String servicePath = resolveServicePath(path);
        ClassData serviceData = resolveService(servicePath);

        if (serviceData == null) {
            return null;
        }

        String methodPath = resolveMethodPath(path);
        if (methodPath == null) {
            // TODO use constructor or a default method?
            throw new RuntimeException("No service method found for request path: '" + path + "'");
        }

        MethodData methodData = resolveMethod(serviceData, methodPath);

        if (methodData == null) {
            Class serviceClass = serviceData.getServiceClass();
            // TODO use constructor or a default method???
            throw new RuntimeException("Method " + methodPath + " not found on " + serviceClass.getName());
        }

        RequestTargetData target = new RequestTargetData(serviceData, methodData);

        return target;
    }

    @Override
    public Map<String, ClassData> resolveServices() {

        if (!resolvedAllServices) {

            ClassDataService classDataService = new ClassDataService(ratelConfig, getPackageNames());
            Map<String, ClassData> allServicesMap = classDataService.getAllServiceClassData();
            serviceByPathMap.putAll(allServicesMap);

            resolvedAllServices = true;
        }
        return serviceByPathMap;
    }

    @Override
    public void clearServices() {
        serviceByPathMap.clear();
    }

    public List<String> getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(
        List<String> packageNames) {
        this.packageNames = packageNames;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    protected boolean isHelpRequest(HttpServletRequest request, String path) {
        // Ratel doesn't render help in prod modes
        if (getMode().isDevelopmentModes()) {
            if (path.endsWith(Constants.HELP)) {
                return true;
            }
        }
        return false;
    }

    protected String resolveServicePath(String path) {
        String realPath = path;
        String urlPrefix = ratelConfig.getUrlPrefix();
        if (urlPrefix != null) {
            // strip prefix from path
            realPath = path.substring(urlPrefix.length());
            if (ratelConfig.getMode().isDevelopmentModes()) {
                ratelConfig.getLogService().info("urlPrefix removed from path '" + path + "'. New path -> " + realPath);
            }
        }

        int index = realPath.lastIndexOf("/");
        if (index == -1) {
            return null;
        }

        String servicePath = realPath.substring(0, index);
        return servicePath;
    }

    protected ClassData resolveService(String path) {
        ClassData serviceData = getService(path);
        if (serviceData != null) {
            return serviceData;
        }
        return null;
    }

    protected String resolveMethodPath(String path) {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return null;
        }

        String methodPath = path.substring(index);
        return methodPath;
    }

    protected MethodData resolveMethod(ClassData classData, String methodPath) {
        MethodData methodData = getMethod(classData, methodPath);
        return methodData;
    }

    protected ClassData getService(String path) {

        // If in production or profile mode.
        if (mode.isProductionModes()) {
            ClassData classData = serviceByPathMap.get(path);
            return classData;
        }

        // Else in development, debug or trace mode
        ClassData serviceData = serviceByPathMap.get(path);
        if (serviceData != null) {
            return serviceData;
        }

        // Resolve Service class from path
        //try {
        //URL resource = servletContext.getResource(path);
        //if (resource != null) {
        if (getPackageNames().isEmpty()) {
            String emptyPackageName = null;
            serviceData = getService(path, emptyPackageName);

        } else {
            for (int i = 0; i < getPackageNames().size(); i++) {
                String packageName = getPackageNames().get(i).toString();

                serviceData = getService(path, packageName);
                if (serviceData != null) {
                    break;
                }
            }
        }

        //}
        //} catch (MalformedURLException e) {
        //ignore
        //}
        return serviceData;
    }

    protected MethodData getMethod(ClassData classData, String methodPath) {
        // If in production or profile mode.
        if (mode.isProductionModes()) {
            MethodData methodData = classData.getMethods().get(methodPath);
            return methodData;
        }

        // Else in development, debug or trace mode
        MethodData methodData = classData.getMethods().get(methodPath);
        if (methodData != null) {
            return methodData;
        }

        // Inform user if they access method through it's name but have also mapped it to a different path through the @Path annotation
        for (Entry<String, MethodData> entry : classData.getMethods().entrySet()) {
            MethodData val = entry.getValue();
            String methodName = "/" + val.getMethodName();
            if (methodName.equals(methodPath)) {
                throw new IllegalArgumentException("The service method, '" + methodName + "', has been mapped with the @Path annotation to "
                    + " -> '" + val.getMethodPath() + "'.");
            }
        }
        return null;
    }

    protected ClassData getService(String path, String packageName) {

        ClassData serviceData = null;
        Class<?> serviceClass = getServiceClass(path, packageName);

        if (serviceClass != null) {
            serviceData = new ClassData();
            serviceData.setServiceClass(serviceClass);
            Path pathAnnot = serviceClass.getAnnotation(Path.class);
            if (pathAnnot != null) {
                String newPath = pathAnnot.value();
                if (newPath.endsWith("/")) {
                    newPath = newPath.substring(0, newPath.length() - 1);
                }
                if (!path.equals(newPath)) {
                    throw new IllegalArgumentException("The service, '" + serviceClass.getName() + "', has been mapped with the @Path "
                        + "annotation to -> '" + newPath + "'.");
                }
            }

            serviceData.setServicePath(path);

            RatelUtils.populateMethods(serviceData);

            if (serviceByPathMap.containsKey(path)) {
                throw new IllegalStateException("A service with the path, '" + path + "', already exists! Ensure paths are unique.");
            }

            serviceByPathMap.put(path, serviceData);

            //addToClassMap(page);
            if (ratelConfig.getLogService().isDebugEnabled()) {
                String msg = path + " -> " + serviceClass.getName();
                ratelConfig.getLogService().debug(msg);
            }
        }

        return serviceData;
    }

    protected Class<?> getServiceClass(String servicePath, String servicesPackage) {
        // To understand this method lets walk through an example as the
        // code plays out. Imagine this method is called with the arguments:
        // pagePath='/pages/edit-customer.htm'
        // pagesPackage='com.google.ratel'

        String packageName = "";
        if (StringUtils.isNotBlank(servicesPackage)) {

            if (!servicesPackage.endsWith(".")) {
                // Append period after package
                // packageName = 'com.google.ratel.'
                packageName = servicesPackage + ".";
            }
        }

        String className = "";

        // Strip off extension.
        // path = '/pages/edit-customer'
        //String path = servicePath.substring(0, servicePath.lastIndexOf("."));
        // Build complete packageName.
        // packageName = 'com.google.ratel.pages.'
        // className = 'edit-customer'
        if (servicePath.indexOf("/") != -1) {
            StringTokenizer tokenizer = new StringTokenizer(servicePath, "/");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (tokenizer.hasMoreTokens()) {
                    packageName = packageName + token + ".";
                } else {
                    className = token;
                }
            }
        } else {
            className = servicePath;
        }

        // CamelCase className.
        // className = 'EditCustomer'
        StringTokenizer tokenizer = new StringTokenizer(className, "_-");
        className = "";
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            token = Character.toUpperCase(token.charAt(0)) + token.substring(1);
            className += token;
        }

        // className = 'com.google.ratel.pages.EditCustomer'
        className = packageName + className;

        Class<?> serviceClass = null;
        try {
            // Attempt to load class.
            serviceClass = RatelUtils.classForName(className);
            RatelService anno = serviceClass.getAnnotation(RatelService.class);
            if (anno == null) {

                String msg = "Automapped class " + className + " is not annotated with @RatelService";
                throw new RuntimeException(msg);
            }

        } catch (ClassNotFoundException cnfe) {

            boolean classFound = false;

            // Append "Service" to className and attempt to load class again.
            // className = 'com.google.ratel.pages.EditCustomerService'
            if (!className.endsWith("Service")) {
                String classNameWithService = className + "Service";
                try {
                    // Attempt to load class.
                    serviceClass = RatelUtils.classForName(classNameWithService);

                    RatelService anno = serviceClass.getAnnotation(RatelService.class);
                    if (anno == null) {

                        String msg = "Automapped class " + className + " is not annotated with @RatelService";
                        throw new RuntimeException(msg);
                    }

                    classFound = true;

                } catch (ClassNotFoundException cnfe2) {
                }
            }

            if (!classFound) {
                //if (logService.isDebugEnabled()) {
                //logService.debug(pagePath + " -> CLASS NOT FOUND");
                //}
                //if (logService.isTraceEnabled()) {
                //   logService.trace("class not found: " + className);
                //}
            }
        }

        return serviceClass;
    }
}
