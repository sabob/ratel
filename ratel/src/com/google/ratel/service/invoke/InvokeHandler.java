package com.google.ratel.service.invoke;

import com.google.ratel.core.RatelHttpServletRequest;
import com.google.ratel.service.classdata.MethodData;
import javax.servlet.*;

public interface InvokeHandler {

    public void onInit(ServletContext servletContext);
    
    public void onDestroy(ServletContext servletContext);
    
    public Object invokeAsJson(Object target, MethodData methodData, RatelHttpServletRequest request);

    public Object invokeAsHttpMethod(Object target, MethodData methodData, RatelHttpServletRequest request);
}
