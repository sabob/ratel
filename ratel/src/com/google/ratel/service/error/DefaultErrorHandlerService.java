package com.google.ratel.service.error;

import com.google.ratel.*;
import com.google.ratel.deps.io.*;
import com.google.ratel.service.classdata.*;
import com.google.ratel.service.log.LogService;
import com.google.ratel.service.resolver.*;
import com.google.ratel.util.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 */
public class DefaultErrorHandlerService implements ErrorHandlerService {

    private String errorTemplate;

    private ServletContext servletContext;

    @Override
    public void onInit(ServletContext servletContext) {
        this.servletContext = servletContext;

        errorTemplate = loadTemplate("error.htm");
        if (errorTemplate == null) {
            errorTemplate = "<html><body><h1>ErrorReport</h1>@TOKEN</body></html>";
        }
    }

    @Override
    public void onDestroy() {
        servletContext = null;
    }

    @Override
    public void handleRuntimeException(Throwable throwable, Mode mode, HttpServletRequest request, HttpServletResponse response,
        RatelConfig ratelConfig) {

        LogService logService = ratelConfig.getLogService();

        if (mode == Mode.PROFILE || mode == Mode.PRODUCTION) {
            logService.error("Could not handle request", throwable);

        } else {
            logService.error("Could not handle request", throwable);

            ServiceResolver serviceResolver = ratelConfig.getServiceResolver();
            String path = serviceResolver.resolvePath(request);

            String serviceName = serviceResolver.resolveServiceName(path);

            ClassData serviceData = serviceResolver.resolveService(serviceName);

            Class serviceClass = null;
            Method method = null;

            if (serviceData != null) {

                serviceClass = serviceData.getServiceClass();

                String methodName = serviceResolver.resolveMethodName(path);
                if (methodName != null) {

                    MethodData methodData = serviceResolver.resolveMethod(serviceData, methodName);

                    if (methodData != null) {
                        method = methodData.getMethod();
                    }
                }
            }

            ErrorReport errorReport = new ErrorReport(throwable, serviceClass, method, mode, request, ratelConfig);

            String content = errorTemplate.replace("@TOKEN", errorReport.toString());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            RatelUtils.writeContent(response, content, Constants.HTML);

            // TODO
            //logService.error("Could not handle request", throwable);
            //throw new RuntimeException(throwable);
        }
    }

    @Override
    public void handleInitializationException(Throwable throwable, Mode mode, ServletContext servletContext, LogService logService) {

        if (mode == Mode.PROFILE || mode == Mode.PRODUCTION) {
            logService.error("Could not initialize Ratel", throwable);

        } else {
            logService.error("Could not initialize Ratel", throwable);
            throw new RuntimeException(throwable);
        }
    }

    protected String loadTemplate(String name) {
        InputStream is = servletContext.getResourceAsStream("/" + name);
        if (is == null) {
            String resourcePath = "/META-INF/resources/" + name;
            is = RatelUtils.getClasspathResourceAsStream(resourcePath);
        }

        String template = null;

        try {
            if (is != null) {
                template = IOUtils.toString(is, (Charset) null);
            }
            return template;

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}