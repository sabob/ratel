package com.google.ratel.service.classdata;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 *
 */
public class MethodData {

    private Method method;
    
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
}
