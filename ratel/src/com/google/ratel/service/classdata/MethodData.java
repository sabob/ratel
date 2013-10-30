package com.google.ratel.service.classdata;

import java.lang.reflect.*;
import java.util.*;

/**
 *
 */
public class MethodData {

    private Method method;
    
    private String methodPath;

    private String methodName;
    
    private List<ParameterData> parameters = new ArrayList<ParameterData>();
    
    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * @return the parameters
     */
    public List<ParameterData> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(
        List<ParameterData> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the methodPath
     */
    public String getMethodPath() {
        return methodPath;
    }

    /**
     * @param methodPath the methodPath to set
     */
    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
