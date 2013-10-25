package com.google.ratel.service.invoke;

import com.google.ratel.core.RatelHttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import com.google.ratel.service.classdata.*;
import com.google.ratel.service.classdata.MethodData;
import com.google.ratel.service.invoke.builder.JsonArgumentBuilder;
import com.google.ratel.service.invoke.builder.ParameterArgumentBuilder;
import com.google.ratel.service.json.*;
import javax.servlet.*;

public class DefaultInvokeHandler implements InvokeHandler {

    private JsonService jsonService;

    private int maxRequestSize = -1;

    public DefaultInvokeHandler() {
    }

    @Override
    public void onInit(ServletContext servletContext) {
    }

    @Override
    public void onDestroy(ServletContext servletContext) {
    }

    @Override
    public Object invokeAsJson(Object target, MethodData methodData, RatelHttpServletRequest request) {
        try {

            Method method = methodData.getMethod();
            List<ParameterData> parameters = methodData.getParameters();

            int argCount = parameters.size();

            if (argCount == 0) {
                // Invoke no-arg method with json
                Object result = method.invoke(target);
                return result;

            } else if (argCount == 1) {
                JsonArgumentBuilder builder = new JsonArgumentBuilder(getJsonService(), getMaxRequestSize());
                Object arg = builder.buildArgument(target, methodData, request);
                //Object arg = buildArgument(target, methodData, request);
                Object result = method.invoke(target, arg);
                return result;

            } else {
                JsonArgumentBuilder builder = new JsonArgumentBuilder(getJsonService(), getMaxRequestSize());
                Object[] args = builder.buildArguments(target, methodData, request);
                // Invoke multi-arg method with json
                Object result = method.invoke(target, args);
                return result;

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invokeAsHttpMethod(Object target, MethodData methodData, RatelHttpServletRequest request) {

        try {
            Method method = methodData.getMethod();
            List<ParameterData> parameters = methodData.getParameters();

            int argCount = parameters.size();

            if (argCount == 0) {
                Object result = method.invoke(target);
                return result;

            } else if (argCount == 1) {
                ParameterArgumentBuilder builder = new ParameterArgumentBuilder(getJsonService());
                Object arg = builder.buildArgument(target, methodData, request);
                Object result = method.invoke(target, arg);
                return result;

            } else {
                ParameterArgumentBuilder builder = new ParameterArgumentBuilder(getJsonService());
                Object[] args = builder.buildArguments(target, methodData, request);
                Object result = method.invoke(target, args);
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the jsonService
     */
    public JsonService getJsonService() {
        return jsonService;
    }

    /**
     * @param jsonService the jsonService to set
     */
    public void setJsonService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    /**
     * @return the maxRequestSize
     */
    public int getMaxRequestSize() {
        return maxRequestSize;
    }

    /**
     * @param maxRequestSize the maxRequestSize to set
     */
    public void setMaxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }
}
