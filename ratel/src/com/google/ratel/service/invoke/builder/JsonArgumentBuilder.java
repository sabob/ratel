/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.service.invoke.builder;

import com.google.ratel.core.RatelHttpServletRequest;
import java.util.List;
import com.google.ratel.service.classdata.*;
import com.google.ratel.service.json.*;
import com.google.ratel.util.*;
import static com.google.ratel.util.RatelUtils.createLimitedReader;
import java.io.*;

/**
 *
 */
public class JsonArgumentBuilder extends ParameterArgumentBuilder {

    private int maxRequestSize = -1;

    public JsonArgumentBuilder(JsonService jsonService, int maxRequestSize) {
        super(jsonService);
        this.maxRequestSize = maxRequestSize;
    }

    @Override
    public Object buildArgument(Object target, MethodData methodData, RatelHttpServletRequest request) {

        List<ParameterData> parameters = methodData.getParameters();
        ParameterData parameter = parameters.get(0);
        Class parameterType = parameter.getType();


        if (parameterType == String.class) {
            String inputJson = RatelUtils.getContent(request, maxRequestSize);
            return inputJson;

        } else {
            Reader reader = createLimitedReader(request, maxRequestSize);
            Object arg = parseJson(reader, parameterType);
            return arg;
        }
    }

    @Override
    public Object[] buildArguments(Object target, MethodData methodData, RatelHttpServletRequest request) {

        Reader reader = createLimitedReader(request, maxRequestSize);

        JsonElement jsonElement = jsonService.parseJson(reader);

        if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            Object[] args = buildArgumentsFromJsonArray(array, target, methodData);
            return args;

        } else {
            String errorLocation = RatelUtils.buildLocation(target, methodData.getMethod());
            String json = jsonService.toJson(jsonElement.unwrap());
            throw new RuntimeException("Service methods with multiple arguments only supports Json Arrays. Json passed in was : " + json
                + ". " + errorLocation);
        }
    }

    protected Object[] buildArgumentsFromJsonArray(JsonArray array, Object target, MethodData methodData) {
        List<ParameterData> parameters = methodData.getParameters();
        Object[] args = new Object[parameters.size()];

        for (int i = 0; i < parameters.size(); i++) {
            ParameterData parameter = parameters.get(i);
            Class type = parameter.getType();

            if (array.size() > i) {
                JsonElement element = array.get(i);
                args[i] = parseJson(element, type);
            }
        }

        return args;
    }
}
