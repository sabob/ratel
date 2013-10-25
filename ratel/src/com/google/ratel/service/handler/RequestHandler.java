package com.google.ratel.service.handler;

import com.google.ratel.core.RatelHttpServletRequest;
import javax.servlet.*;
import com.google.ratel.service.resolver.ServiceResolver;
import com.google.ratel.service.classdata.ClassData;
import com.google.ratel.service.classdata.MethodData;
import com.google.ratel.util.RatelUtils;
import com.google.ratel.service.invoke.InvokeHandler;
import com.google.ratel.util.Constants;
import com.google.ratel.deps.lang3.StringUtils;
import com.google.ratel.Context;
import com.google.ratel.RatelConfig;
import com.google.ratel.core.RatelHttpServletResponse;
import com.google.ratel.service.classdata.RequestTargetData;
import com.google.ratel.service.json.*;

/**
 * http://localhost:8080/click-service/tests/PersonService/getJson http://localhost:8080/click-service/tests/PersonService/getPerson
 */
public class RequestHandler {

    //private ServiceResolver serviceResolver;
    //private ServletContext servletContext;
    //private ClassDataService classDataService;
    public static String PING_PARAM = "ping";

    private int maxRequestSize = -1;

    public RequestHandler() {
    }

    public void onInit(ServletContext servletContext) {
    }

    public void onDestroy(ServletContext servletContext) {
    }

    public boolean handle(Context context) {
        // Incoming request -> http://host.com/service/method

        RatelConfig ratelConfig = context.getRatelConfig();
        RatelHttpServletRequest request = context.getRequest();
        ServiceResolver serviceResolver = ratelConfig.getServiceResolver();

        //String path = serviceResolver.resolvePath(request);

        RequestTargetData target = serviceResolver.resolveTarget(request);

        if (target == null) {
            // Continue processing the request since Ratel could not resolve the request to a service
            return true;
        }

        if (target.isHelpRequest()) {
            HelpHandler helpHandler = ratelConfig.getHelpHandler();
            helpHandler.handleHelp(context);
            return false;
        }

        ClassData serviceData = target.getClassData();
        MethodData methodData = target.getMethodData();

        Class serviceClass = serviceData.getServiceClass();

        Object service = createService(serviceClass);

        invokeMethod(context, service, methodData);

        return false;
    }

    protected Object createService(Class serviceClass) {
        try {
            Object service = serviceClass.newInstance();
            return service;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void invokeMethod(Context context, Object service, MethodData methodData) {
        RatelHttpServletRequest request = context.getRequest();
        RatelHttpServletResponse response = context.getResponse();

        //boolean isPost = context.isPost();
        //boolean isGet = false;
        //if (!isPost) {
        //isGet = Constants.GET.equals(request.getMethod());
        //}

        String contentType = request.getContentType();

        RatelConfig ratelConfig = context.getRatelConfig();
        JsonService jsonService = ratelConfig.getJsonService();
        InvokeHandler invokeHandler = ratelConfig.getInvokeHandler();

        String responseContentType = Constants.JSON;
        //String responseContentType = RatelUtils.getResponseContentType(request);

        if (StringUtils.isBlank(contentType)) {

            if (request.getParameter(PING_PARAM) != null) {
                HelpHandler helpHandler = ratelConfig.getHelpHandler();
                Object result = helpHandler.invokeAsPing(service, methodData, context);
                RatelUtils.writeContent(jsonService, response, result, Constants.HTML);

            } else {
                Object result = invokeHandler.invokeAsHttpMethod(service, methodData, request);
                // TODO how should data be written out?
                RatelUtils.writeContent(jsonService, response, result, responseContentType);
            }

        } else if (contentType.indexOf(Constants.JSON) >= 0) {
            Object result = invokeHandler.invokeAsJson(service, methodData, request);
            RatelUtils.writeContent(jsonService, response, result, responseContentType);

        } else if (contentType.indexOf(Constants.FORM_ENCODED) >= 0) {
            Object result = invokeHandler.invokeAsHttpMethod(service, methodData, request);
            // TODO how should data be written out?
            RatelUtils.writeContent(jsonService, response, result, responseContentType);

        } else if (request.isMultipartRequest()) {
            Object result = invokeHandler.invokeAsHttpMethod(service, methodData, request);
            // TODO how should data be written out?
            RatelUtils.writeContent(jsonService, response, result, responseContentType);

        } else {
            //throw new RuntimeException("Cannot handle conent type " + contentType + ". Use 'application/json'!");
            // TODO what type should we return?
            //Object result = Utils.invokeMethod(service, method, request);
            //Utils.writeContent(response, result, Constants.JSON);
            Object result = invokeHandler.invokeAsHttpMethod(service, methodData, request);
            // TODO how should data be written out?
            RatelUtils.writeContent(jsonService, response, result, responseContentType);
        }
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
