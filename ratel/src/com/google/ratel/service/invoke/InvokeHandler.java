package com.google.ratel.service.invoke;

import com.google.ratel.core.RatelHttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import com.google.ratel.service.classdata.*;
import com.google.ratel.service.classdata.MethodData;
import com.google.ratel.service.invoke.builder.JsonArgumentBuilder;
import com.google.ratel.service.invoke.builder.ParameterArgumentBuilder;
import com.google.ratel.service.json.*;
import com.google.ratel.util.*;

/**
 * Notes: should be an interface with JSON and Form-Encoding implementations
 */
public class InvokeHandler {

    private JsonService jsonService;
    private int maxRequestSize = -1;

    public InvokeHandler(JsonService jsonService, int maxRequestSize) {
        this.jsonService = jsonService;
        this.maxRequestSize = maxRequestSize;
    }

    public Object invokeAsJson(Object target, MethodData methodData, RatelHttpServletRequest request) throws Exception {

        Method method = methodData.getMethod();
        List<ParameterData> parameters = methodData.getParameters();

        int argCount = parameters.size();

        if (argCount == 0) {
            // Invoke no-arg method with json
            Object result = method.invoke(target);
            return result;

        } else if (argCount == 1) {
            JsonArgumentBuilder builder = new JsonArgumentBuilder(jsonService, maxRequestSize);
            Object arg = builder.buildArgument(target, methodData, request);
            //Object arg = buildArgument(target, methodData, request);
            Object result = method.invoke(target, arg);
            return result;

        } else {
            JsonArgumentBuilder builder = new JsonArgumentBuilder(jsonService, maxRequestSize);
            Object[] args = builder.buildArguments(target, methodData, request);
            // Invoke multi-arg method with json
            Object result = method.invoke(target, args);
            return result;
        }
    }

    /**
     * 
     */
    public Object invokeAsGetOrPost(Object target, MethodData methodData, RatelHttpServletRequest request) throws Exception {

        Method method = methodData.getMethod();
        List<ParameterData> parameters = methodData.getParameters();

        int argCount = parameters.size();

        if (argCount == 0) {
            // Invoke no-arg method with json
            Object result = method.invoke(target);
            return result;

        } else if (argCount == 1) {
            ParameterArgumentBuilder builder = new ParameterArgumentBuilder(jsonService);
            Object arg = builder.buildArgument(target, methodData, request);
            Object result = method.invoke(target, arg);
            return result;

        } else {
            ParameterArgumentBuilder builder = new ParameterArgumentBuilder(jsonService);
            Object[] args = builder.buildArguments(target, methodData, request);
            Object result = method.invoke(target, args);
            return result;
        }
    }
}
