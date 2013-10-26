package com.google.ratel.service.classdata;

import com.google.ratel.deps.jackson.annotation.*;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 */
public class ClassData {
    
    private Class serviceClass;

    private String servicePath;
    
    private Map<String, MethodData> methods = new ConcurrentHashMap<String, MethodData>();
    
    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    /**
     * @return the methods
     */
    public Map<String, MethodData> getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(
        Map<String, MethodData> methods) {
        this.methods = methods;
    }
}
