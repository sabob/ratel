/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel;

import com.google.ratel.core.RatelHttpServletRequest;
import com.google.ratel.core.RatelHttpServletResponse;
import com.google.ratel.service.error.*;
import com.google.ratel.service.error.ErrorHandlerService;
import com.google.ratel.core.Mode;
import com.google.ratel.service.handler.RequestHandler;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.ratel.service.handler.HelpHandler;
import com.google.ratel.service.invoke.DefaultInvokeHandler;
import com.google.ratel.service.invoke.InvokeHandler;
import com.google.ratel.service.json.*;
import com.google.ratel.service.json.jackson.JacksonService;
import com.google.ratel.service.log.*;
import com.google.ratel.service.log.LogService;
import com.google.ratel.service.resolver.*;
import com.google.ratel.service.template.*;
import com.google.ratel.service.upload.*;

/**
 *
 */
public class RatelConfig {

    public static final String[] MODE_VALUES = { "production", "profile", "development", "debug", "trace" };

    /**
     * The application mode: [ PRODUCTION | PROFILE | DEVELOPMENT | DEBUG | TRACE ].
     */
    private Mode mode;

    protected ServletContext servletContext;

    protected RequestHandler requestHandler;

    private InvokeHandler invokeHandler;

    protected HelpHandler helpHandler;

    protected TemplateService templateService;

    protected ServiceResolver serviceResolver;

    protected FileUploadService fileUploadService;

    protected List<String> packageNameList;

    protected JsonService jsonService;

    private LogService logService;

    private ErrorHandlerService errorHandlerService;

    protected int maxRequestSize;
    
    public RatelConfig() {
    }

    public void onInit(ServletContext servletContext, List<String> packageNameList, int maxRequestSize) {

        try {
            setServletContext(servletContext);

            if (getMode() == null) {
                setMode(createMode());
            }

            setLogService(createLogService());
            setErrorHandlerService(createErrorHandlerService());
        } catch (Exception e) {
            if (getLogService() != null) {
                getLogService().error("Could not initialize Ratel", e);
            }
            throw new RuntimeException(e);
        }

        try {
            setMaxRequestSize(maxRequestSize);
            setPackageNameList(packageNameList);

            setJsonService(createJsonService());

            setTemplateService(createTemplateService());

            setFileUploadService(createFileUploadService());

            setServiceResolver(createServiceResolver());

            setRequestHandler(createRequestHandler());

            setInvokeHandler(createInvokeHandler());

            setHelpHandler(createHelpHandler());

            getLogService().onInit(servletContext);
            getErrorHandlerService().onInit(servletContext);
            if (getTemplateService() != null) {
                getTemplateService().onInit(servletContext);
            }
            getFileUploadService().onInit(servletContext);
            getServiceResolver().onInit(servletContext);
            getRequestHandler().onInit(servletContext);
            getHelpHandler().onInit(servletContext);
            getJsonService().onInit(servletContext);
            getInvokeHandler().onInit(servletContext);

            if (getMode() == Mode.PROFILE || getMode() == Mode.PRODUCTION) {
            getServiceResolver().resolveServices();
        }

        } catch (Exception e) {
            ErrorHandlerService errorHandler = getErrorHandlerService();
            errorHandler.handleInitializationException(e, getMode(), getServletContext(), getLogService());
        }
    }

    public void onDestroy(ServletContext servletContext) {
        if (getTemplateService() != null) {
            getTemplateService().onDestroy(servletContext);
        }
        getServiceResolver().onDestroy(servletContext);
        getInvokeHandler().onDestroy(servletContext);
        getHelpHandler().onDestroy(servletContext);
        getRequestHandler().onDestroy(servletContext);
        getFileUploadService().onDestroy(servletContext);
        getJsonService().onDestroy(servletContext);
        getErrorHandlerService().onDestroy(servletContext);
        getLogService().onDestroy(servletContext);
        this.setServletContext(null);
    }

    public Context createAndBindContext(HttpServletRequest request, HttpServletResponse response) {
        RatelHttpServletRequest ratelRequest = createRequest(request);
        RatelHttpServletResponse ratelResponse = createResponse(response);
        Context context = createContext(getServletContext(), ratelRequest, ratelResponse);

        Context.bindContext(context);
        return context;
    }

    protected RatelHttpServletRequest createRequest(HttpServletRequest request) {
        RatelHttpServletRequest ratelRequest = new RatelHttpServletRequest(request, getFileUploadService());
        return ratelRequest;
    }

    protected RatelHttpServletResponse createResponse(HttpServletResponse response) {
        RatelHttpServletResponse ratelResponse = new RatelHttpServletResponse(response);
        return ratelResponse;
    }

    protected JsonService createJsonService() {
        //GsonService service = new GsonService();
        JacksonService service = new JacksonService();
        System.out.println("JSON service: " + service.getClass().getSimpleName());
        // TODO depends on mode, prod these should be false
        if (getMode().isAtleast(Mode.DEBUG)) {
            service.setPrettyPrint(true);
            service.setSerializeNulls(true);
        }
        return service;
    }

    public LogService createLogService() {
        ConsoleLogService localLogService = new ConsoleLogService();

        // Set log levels
        int logLevel = ConsoleLogService.INFO_LEVEL;

        Mode localMode = getMode();

        if (localMode == Mode.PRODUCTION) {
            logLevel = ConsoleLogService.WARN_LEVEL;

        } else if (mode == Mode.PROFILE) {
            logLevel = ConsoleLogService.INFO_LEVEL;

        } else if (mode == Mode.DEVELOPMENT) {
            logLevel = ConsoleLogService.INFO_LEVEL;

        } else if (mode == Mode.DEBUG) {
            logLevel = ConsoleLogService.DEBUG_LEVEL;

        } else if (mode == Mode.TRACE) {
            logLevel = ConsoleLogService.TRACE_LEVEL;
        }

        localLogService.setLevel(logLevel);

        return localLogService;
    }

    public ErrorHandlerService createErrorHandlerService() {
        ErrorHandlerService localErrorHandlerService = new DefaultErrorHandlerService();
        return localErrorHandlerService;
    }

    protected RequestHandler createRequestHandler() {
        RequestHandler localRequestHandler = new RequestHandler();
        localRequestHandler.setMaxRequestSize(this.maxRequestSize);
        return localRequestHandler;
    }

    protected InvokeHandler createInvokeHandler() {
        DefaultInvokeHandler localInvokeHandler = new DefaultInvokeHandler();
        localInvokeHandler.setJsonService(getJsonService());
        localInvokeHandler.setMaxRequestSize(getMaxRequestSize());
        return localInvokeHandler;
    }

    protected HelpHandler createHelpHandler() {
        return new HelpHandler();
    }

    protected Context createContext(ServletContext servletContext, RatelHttpServletRequest request, RatelHttpServletResponse response) {
        Context context = new Context();
        context.setRatelConfig(this);
        context.setServletContext(servletContext);
        context.setRequest(request);
        context.setResponse(response);
        return context;
    }

    protected ServiceResolver createServiceResolver() {
        DefaultServiceResolver localServiceResolver = new DefaultServiceResolver();

        Mode localMode = getMode();
        localServiceResolver.setMode(localMode);

        for (String packageName : packageNameList) {
            localServiceResolver.getPackageNames().add(packageName);
        }
        return localServiceResolver;
    }

    protected TemplateService createTemplateService() {
        //return new VelocityTemplateService();
        return null;
    }

    protected FileUploadService createFileUploadService() {
        return new CommonsFileUploadService();
    }

    /**
     * @return the servletContext
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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

    /**
     * @return the packageNameList
     */
    public List<String> getPackageNameList() {
        return packageNameList;
    }

    /**
     * @param packageNameList the packageNameList to set
     */
    public void setPackageNameList(
        List<String> packageNameList) {
        this.packageNameList = packageNameList;
    }

    /**
     * @return the requestHandler
     */
    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    /**
     * @param requestHandler the requestHandler to set
     */
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * @return the templateService
     */
    public TemplateService getTemplateService() {
        return templateService;
    }

    /**
     * @param templateService the templateService to set
     */
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * @return the serviceResolver
     */
    public ServiceResolver getServiceResolver() {
        return serviceResolver;
    }

    /**
     * @param serviceResolver the serviceResolver to set
     */
    public void setServiceResolver(ServiceResolver serviceResolver) {
        this.serviceResolver = serviceResolver;
    }

    /**
     * @return the fileUploadService
     */
    public FileUploadService getFileUploadService() {
        return fileUploadService;
    }

    /**
     * @param fileUploadService the fileUploadService to set
     */
    public void setFileUploadService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * @return the helpHandler
     */
    public HelpHandler getHelpHandler() {
        return helpHandler;
    }

    /**
     * @param helpHandler the helpHandler to set
     */
    public void setHelpHandler(HelpHandler helpHandler) {
        this.helpHandler = helpHandler;
    }

    public JsonService getJsonService() {
        return jsonService;
    }

    public void setJsonService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    protected Mode createMode() {
        return Mode.PRODUCTION;
        /*
         Mode localMode = Mode.getMode(modeValue);

         if (localMode == null) {
         localMode = Mode.DEBUG;
         System.out.println("invalid mode: '" + modeValue + "' - defaulted to '" + localMode.getName() + "'");
         }
         return mode;
         */
    }

    /**
     * @return the logService
     */
    public LogService getLogService() {
        return logService;
    }

    /**
     * @param logService the logService to set
     */
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /**
     * @return the errorHandlerService
     */
    public ErrorHandlerService getErrorHandlerService() {
        return errorHandlerService;
    }

    /**
     * @param errorHandlerService the errorHandlerService to set
     */
    public void setErrorHandlerService(ErrorHandlerService errorHandlerService) {
        this.errorHandlerService = errorHandlerService;
    }

    /**
     * @return the invokeHandler
     */
    public InvokeHandler getInvokeHandler() {
        return invokeHandler;
    }

    /**
     * @param invokeHandler the invokeHandler to set
     */
    public void setInvokeHandler(InvokeHandler invokeHandler) {
        this.invokeHandler = invokeHandler;
    }
}
