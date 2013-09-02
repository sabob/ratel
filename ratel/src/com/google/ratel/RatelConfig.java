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
import com.google.ratel.util.RatelUtils;
import java.text.Format;

/**
 * Provides a default implementation of the {@link Container} interface
 * to make it easier for developers to create their own containers.
 * <p/>
 * The following example shows how to create an HTML <tt>div</tt> element:
 *
 * <pre class="prettyprint linenums">
 * public class Div extends AbstractContainer {
 *
 *     public String getTag() {
 *         // Return the HTML tag
 *         return "div";
 *     }
 * } </pre>
 */
public class RatelConfig {

    /**
     * The servlet context attribute name. The RatelFilter/RatelPortlet stores the RatelConfig instance in the ServletContext using this
     * context attribute name. The value of this constant is {@value}.
     */
    public static final String CONTEXT_NAME = "com.google.ratel.RatelConfig";

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

    /** The format class. */
    private Class<? extends Format> formatClass;

    /** The character encoding of this application. */
    private String charset;

    /** The default application locale.*/
    private Locale locale;

    public RatelConfig() {
    }

    public void onInit(ServletContext servletContext, List<String> packageNameList, int maxRequestSize) {

        long start = System.currentTimeMillis();

        try {
            setServletContext(servletContext);

            if (getMode() == null) {
                setMode(createMode());
            }

            setLogService(createLogService());

            if (getLogService().isInfoEnabled()) {
                getLogService().info("***  Initializing Ratel " + RatelUtils.getRatelVersion() + " in " + getMode() + " mode  ***");

                String msg = "created LogService: " + getLogService().getClass().getName();
                getLogService().info(msg);
            }

            setErrorHandlerService(createErrorHandlerService());
        } catch (Exception e) {
            if (getLogService() == null) {
                System.out.println("[Ratel] [error] Could not initialize Ratel");
            } else {
                getLogService().error("Could not initialize Ratel");

            }
            throw new RuntimeException(e);
        }

        try {
            setLocale(createLocale());
            setCharset(createCharset());
            setFormatClass(createFormatClass());
            setMaxRequestSize(maxRequestSize);
            setPackageNameList(packageNameList);

            setJsonService(createJsonService());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created JsonService: " + getJsonService().getClass().getName());
            }

            setTemplateService(createTemplateService());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created TemplateService: " + getServiceName(getTemplateService()));
            }

            setFileUploadService(createFileUploadService());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created FileUploadService: " + getServiceName(getFileUploadService()));
            }

            setServiceResolver(createServiceResolver());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created ServiceResolver: " + getServiceName(getServiceResolver()));
            }

            setRequestHandler(createRequestHandler());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created RequestHandler: " + getServiceName(getRequestHandler()));
            }

            setInvokeHandler(createInvokeHandler());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created InvokeHandler: " + getServiceName(getInvokeHandler()));
            }

            setHelpHandler(createHelpHandler());
            if (getLogService().isInfoEnabled()) {
                getLogService().info("created HelpHandler: " + getServiceName(getHelpHandler()));
            }

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

            long time = System.currentTimeMillis() - start;
            if (getLogService().isInfoEnabled()) {
                getLogService().info("Ratel " + RatelUtils.getRatelVersion() + " initialized in " + getMode() + " mode in "
                    + format(time));
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

    protected Class<? extends Format> createFormatClass() {
        return Format.class;
    }
    
     protected Locale createLocale() {
         return null;
    }
     
     protected String createCharset() {
         //return "UTF-8";
         return null;
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


    /**
     * @return the formatClass
     */
    public Class<? extends Format> getFormatClass() {
        return formatClass;
    }

    /**
     * @param formatClass the formatClass to set
     */
    public void setFormatClass(Class<? extends Format> formatClass) {
        this.formatClass = formatClass;
    }


    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    
    private String getServiceName(Object obj) {
        if (obj == null) {
            return "no service specified";
        } else {
            return obj.getClass().getName();
        }
    }

    private String format(long millis) {
        return String.format("%.2fs", millis / 1000.0);
    }
}
