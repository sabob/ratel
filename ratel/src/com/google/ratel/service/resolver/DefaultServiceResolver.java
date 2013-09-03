package com.google.ratel.service.resolver;

import com.google.ratel.RatelConfig;
import java.util.*;
import java.util.concurrent.*;
import javax.servlet.*;
import com.google.ratel.service.classdata.ClassData;
import com.google.ratel.service.classdata.MethodData;
import com.google.ratel.util.RatelUtils;
import com.google.ratel.service.classdata.ClassDataService;
import com.google.ratel.deps.lang3.StringUtils;
import com.google.ratel.core.RatelService;
import com.google.ratel.service.classdata.*;
import com.google.ratel.util.Constants;
import com.google.ratel.core.Mode;
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

        String serviceName = resolveServiceName(path);
        ClassData serviceData = resolveService(serviceName);

        if (serviceData == null) {
            return null;
        }

        String methodName = resolveMethodName(path);
        if (methodName == null) {
            // TODO use constructor or a default method?
            throw new RuntimeException("No service method found for request path: '" + path + "'");
        }

        MethodData methodData = resolveMethod(serviceData, methodName);

        if (methodData == null) {
            Class serviceClass = serviceData.getServiceClass();
            // TODO use constructor or a default method???
            throw new RuntimeException("Method " + methodName + " not found on " + serviceClass.getName());
        }

        RequestTargetData target = new RequestTargetData(serviceData, methodData);

        return target;
    }

    @Override
    public Map<String, ClassData> resolveServices() {

        if (!resolvedAllServices) {

            ClassDataService classDataService = new ClassDataService(servletContext, getPackageNames());
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
        if (getMode().isAtleast(Mode.DEVELOPMENT)) {
            if (path.endsWith(Constants.HELP)) {
                return true;
            }
        }
        return false;
    }

    protected String resolveServiceName(String path) {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return null;
        }

        String serviceName = path.substring(0, index);
        return serviceName;
    }

    protected ClassData resolveService(String path) {
        ClassData serviceData = getService(path);
        if (serviceData != null) {
            return serviceData;
        }
        return null;
    }

    protected String resolveMethodName(String path) {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return null;
        }

        String methodName = path.substring(index + 1);
        return methodName;
    }

    protected MethodData resolveMethod(ClassData classData, String methodName) {
        MethodData methodData = classData.getMethods().get(methodName);
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
        for (int i = 0; i < getPackageNames().size(); i++) {
            String pagesPackage = getPackageNames().get(i).toString();

            Class serviceClass = getServiceClass(path, pagesPackage);

            if (serviceClass != null) {
                serviceData = new ClassData();
                serviceData.setServiceClass(serviceClass);
                serviceData.setServicePath(path);

                RatelUtils.populateMethods(serviceData);

                serviceByPathMap.put(path, serviceData);
                //addToClassMap(page);

                if (ratelConfig.getLogService().isDebugEnabled()) {
                    String msg = path + " -> " + serviceClass.getName();
                    ratelConfig.getLogService().debug(msg);
                }

                break;
            }
        }
        //}
        //} catch (MalformedURLException e) {
        //ignore
        //}
        return serviceData;
    }

    protected Class getServiceClass(String servicePath, String servicesPackage) {
        // To understand this method lets walk through an example as the
        // code plays out. Imagine this method is called with the arguments:
        // pagePath='/pages/edit-customer.htm'
        // pagesPackage='com.google.ratel'

        String packageName = "";
        if (StringUtils.isNotBlank(servicesPackage)) {
            // Append period after package
            // packageName = 'com.google.ratel.'
            packageName = servicesPackage + ".";
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
                    if (anno != null) {

                        //if (!Service.class.isAssignableFrom(serviceClass)) {
                        String msg = "Automapped page class " + className
                            + " is not a subclass of com.google.ratel.service.Service";
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
