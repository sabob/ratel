package com.google.ratel.service.resolver;

import java.util.*;
import javax.servlet.*;
import com.google.ratel.service.classdata.ClassData;
import com.google.ratel.service.classdata.MethodData;
import javax.servlet.http.*;

/**
 *
 */
public interface ServiceResolver {

    public void onInit(ServletContext servletContext);

    public void onDestroy(ServletContext servletContext);

    public String resolvePath(HttpServletRequest request);

    public String resolveServiceName(String path);

    public ClassData resolveService(String serviceName);

    public String resolveMethodName(String path);

    public MethodData resolveMethod(ClassData classData, String methodName);

    public Map<String, ClassData> resolveServices();

    public void clearServices();
}
