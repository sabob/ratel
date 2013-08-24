/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.service.invoke.builder;

import com.google.ratel.core.RatelHttpServletRequest;
import com.google.ratel.core.Param;
import com.google.ratel.core.JsonParam;
import java.util.*;
import com.google.ratel.deps.fileupload.FileItem;
import com.google.ratel.service.classdata.*;
import com.google.ratel.service.json.*;
import com.google.ratel.util.*;
import java.io.Reader;

/**
 *
 */
public class ParameterArgumentBuilder {

    protected JsonService jsonService;

    public ParameterArgumentBuilder(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    public Object buildArgument(Object target, MethodData methodData, RatelHttpServletRequest request) {

        List<ParameterData> parameters = methodData.getParameters();
        if (parameters.isEmpty()) {
            return null;
        }

        ParameterData parameter = parameters.get(0);

        Object arg = buildArgument(parameter, target, methodData, request);
        return arg;
    }

    public Object[] buildArguments(Object target, MethodData methodData, RatelHttpServletRequest request) {

        List<ParameterData> parameters = methodData.getParameters();
        if (parameters.isEmpty()) {
            return null;
        }

        Object[] result = new Object[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            ParameterData parameter = parameters.get(i);

            Object arg = buildArgument(parameter, target, methodData, request);
            result[i] = arg;

        }

        return result;
    }

    protected Object buildArgument(ParameterData parameter, Object target, MethodData methodData, RatelHttpServletRequest request) {

        Class parameterType = parameter.getType();

        Param param = parameter.getParam();
        if (param != null) {
            Object arg = buildArgument(param, target, parameterType, methodData, request);
            return arg;
        }

        JsonParam jsonParam = parameter.getJsonParam();
        if (jsonParam != null) {
            Object arg = buildArgument(jsonParam, target, parameterType, methodData, request);
            return arg;
        }

        return null;
    }

    protected Object buildArgument(Param param, Object target, Class parameterClass, MethodData methodData, RatelHttpServletRequest request) {
        String name = param.name();
        boolean required = param.required();

        if (ParamUtils.isSupportedParameter(parameterClass)) {
            String value = request.getParameter(name);

            if (required && value == null) {
                String location = RatelUtils.buildLocation(target, methodData.getMethod(), parameterClass);
                throw new IllegalArgumentException(location + ": the parameter, " + name + ", is required!");
            }

            Object arg = ParamUtils.toType(parameterClass, value);
            return arg;

        } else if (parameterClass.isArray()) {
            Class arrayType = parameterClass.getComponentType();

            if (ParamUtils.isSupportedParameter(arrayType)) {
                String[] values = request.getParameterValues(name);

                if (required && values == null) {
                    String location = RatelUtils.buildLocation(target, methodData.getMethod(), parameterClass);
                    throw new IllegalArgumentException(location + ": the parameter, " + name + ", is required!");
                }

                Object arg = ParamUtils.toType(arrayType, values);
                return arg;
            } else {
                if (arrayType == FileItem.class) {
                    FileItem[] values = request.getFileItems(name);
                    return values;
                } else {
                    String location = RatelUtils.buildLocation(target, methodData.getMethod());
                    throw new IllegalArgumentException(location + " is annotated with Param, but the parameter type, '"
                        + parameterClass.getName() + "', is not supported by Ratel.!");
                }
            }

        } else {
            if (parameterClass == FileItem.class) {
                FileItem value = request.getFileItem(name);
                return value;

            } else {
                String location = RatelUtils.buildLocation(target, methodData.getMethod());
                throw new IllegalArgumentException(location + " is annotated with Param, but the parameter type, '"
                    + parameterClass.getName() + "', is not supported by Ratel.!");
            }
        }
    }

    protected Object buildArgument(JsonParam param, Object target, Class parameterType, MethodData methodData,
        RatelHttpServletRequest request) {
        String name = param.name();
        boolean required = param.required();

        String value = request.getParameter(name);

        if (required && value == null) {
            String location = RatelUtils.buildLocation(target, methodData.getMethod(), parameterType);
            throw new IllegalArgumentException(location + ": the parameter, " + name + ", is required!");
        }

        if (parameterType == String.class) {
            return value;
        } else {
            Object arg = parseJson(value, parameterType);
            return arg;
        }
    }

    protected Object parseJson(Reader reader, Class type) {
        Object arg = jsonService.fromJson(reader, type);
        return arg;
    }
    
    protected Object parseJson(String str, Class type) {
        Object arg = jsonService.fromJson(str, type);
        return arg;
    }

    protected Object parseJson(JsonElementWrapper element, Class type) {
        Object arg = jsonService.fromJson(element, type);
        return arg;
    }
}
