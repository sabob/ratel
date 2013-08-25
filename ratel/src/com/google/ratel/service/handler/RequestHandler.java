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
import com.google.ratel.service.json.*;
import com.google.ratel.util.*;

/**
 * http://localhost:8080/click-service/tests/PersonService/getJson http://localhost:8080/click-service/tests/PersonService/getPerson
 */
public class RequestHandler {

    //private ServiceResolver serviceResolver;
    //private ServletContext servletContext;
    //private ClassDataService classDataService;
    public static String HELP = "/help";

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

        try {

            RatelConfig ratelConfig = context.getRatelConfig();
            RatelHttpServletRequest request = context.getRequest();
            ServiceResolver serviceResolver = ratelConfig.getServiceResolver();
            String path = serviceResolver.resolvePath(request);

            if (path.endsWith(HELP)) {
                Mode mode = context.getRatelConfig().getMode();
                if (mode == Mode.PROFILE || mode == Mode.PRODUCTION) {
                    return false;

                } else {
                    HelpHandler helpHandler = ratelConfig.getHelpHandler();
                    helpHandler.handleHelp(context);
                    return false;

                }
            }


            String serviceName = serviceResolver.resolveServiceName(path);

            ClassData serviceData = serviceResolver.resolveService(serviceName);

            if (serviceData != null) {

                Class serviceClass = serviceData.getServiceClass();

                String methodName = serviceResolver.resolveMethodName(path);
                if (methodName == null) {
                    // TODO use constructor or a default method?
                    throw new RuntimeException("No method found in URL: " + path);
                }

                MethodData methodData = serviceResolver.resolveMethod(serviceData, methodName);

                if (methodData == null) {
                    // TODO use constructor or a default method???
                    throw new RuntimeException("Method " + methodName + " not found on " + serviceClass.getName());
                }

                Object service = createService(serviceClass);

                invokeMethod(context, service, methodData);

            } else {
                // TODO print error report
                return true;
            }

        } catch (Exception e) {
            // TODO print error report
            throw new RuntimeException(e);
        }

        return false;
    }

    public Object createService(Class serviceClass) throws Exception {
        Object service = serviceClass.newInstance();
        return service;
    }

    protected void invokeMethod(Context context, Object service, MethodData methodData) {
        RatelHttpServletRequest request = context.getRequest();
        RatelHttpServletResponse response = context.getResponse();

        boolean isPost = RatelUtils.isPost(request);
        //boolean isGet = false;
        //if (!isPost) {
            //isGet = Constants.GET.equals(request.getMethod());
        //}

        String contentType = request.getContentType();

        RatelConfig ratelConfig = context.getRatelConfig();
        JsonService jsonService = ratelConfig.getJsonService();
        InvokeHandler invokeHandler = ratelConfig.getInvokeHandler();

        try {

            String responseContentType = Constants.JSON;
            //String responseContentType = RatelUtils.getResponseContentType(request);

            if (StringUtils.isBlank(contentType)) {

                if (request.getParameter(PING_PARAM) != null) {
                    HelpHandler helpHandler = ratelConfig.getHelpHandler();
                    Object result = helpHandler.invokeAsPing(service, methodData, context);
                    RatelUtils.writeContent(jsonService, response, result, Constants.HTML);

                } else {
                    Object result = invokeHandler.invokeAsGetOrPost(service, methodData, request);
                    // TODO how should data be written out?
                    RatelUtils.writeContent(jsonService, response, result, responseContentType);
                }

            } else if (contentType.indexOf(Constants.JSON) >= 0) {
                Object result = invokeHandler.invokeAsJson(service, methodData, request);
                RatelUtils.writeContent(jsonService, response, result, responseContentType);

            } else if (isPost && contentType.indexOf(Constants.FORM_ENCODED) >= 0) {
                Object result = invokeHandler.invokeAsGetOrPost(service, methodData, request);
                // TODO how should data be written out?
                RatelUtils.writeContent(jsonService, response, result, responseContentType);

            } else if (request.isMultipartRequest()) {
                Object result = invokeHandler.invokeAsGetOrPost(service, methodData, request);
                // TODO how should data be written out?
                RatelUtils.writeContent(jsonService, response, result, responseContentType);

            } else {
                //throw new RuntimeException("Cannot handle conent type " + contentType + ". Use 'application/json'!");
                // TODO what type should we return?
                //Object result = Utils.invokeMethod(service, method, request);
                //Utils.writeContent(response, result, Constants.JSON);
                Object result = invokeHandler.invokeAsGetOrPost(service, methodData, request);
                // TODO how should data be written out?
                RatelUtils.writeContent(jsonService, response, result, responseContentType);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
