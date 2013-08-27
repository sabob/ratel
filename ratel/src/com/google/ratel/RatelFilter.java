package com.google.ratel;

import com.google.ratel.core.Mode;
import com.google.ratel.service.handler.RequestHandler;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.*;
import com.google.ratel.service.error.ErrorHandlerService;
import com.google.ratel.util.FilterUtils;
import javax.servlet.Filter;

/**
 *
 */
public class RatelFilter implements Filter {

    protected ServletContext servletContext;

    protected RatelConfig ratelConfig;

    public RatelFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        this.servletContext = filterConfig.getServletContext();

        Class<? extends RatelConfig> ratelConfigClass = getConfigClass(filterConfig);
        this.ratelConfig = createRatelConfig(ratelConfigClass);

        FilterUtils.setRatelConfig(this.ratelConfig, this.servletContext);

        Mode mode = getMode(filterConfig);
        this.ratelConfig.setMode(mode);

        List<String> packageNameList = getPackageNames(filterConfig);
        int maxRequestSize = getMaxRequestSize(filterConfig);

        this.ratelConfig.onInit(servletContext, packageNameList, maxRequestSize);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException,
        ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {

            // CLK-312. Apply request.setCharacterEncoding before wrapping
            // request in ClickRequestWrapper
            /*
             String charset = clickServlet.getConfigService().getCharset();
             if (charset != null) {

             try {
             request.setCharacterEncoding(charset);

             } catch (UnsupportedEncodingException ex) {
             String msg =
             "The character encoding " + charset + " is invalid.";
             System.out.println(msg);
             ex.printStackTrace();
             }
             }*/

            Context context = ratelConfig.createAndBindContext(request, response);
            RequestHandler requestHandler = ratelConfig.getRequestHandler();

            boolean continueProcessing = requestHandler.handle(context);

            if (continueProcessing) {
                chain.doFilter(request, response);
            }

        } catch (Exception e) {
            ErrorHandlerService errorHandler = ratelConfig.getErrorHandlerService();
            errorHandler.handleRuntimeException(e, ratelConfig.getMode(), request, response, ratelConfig);

        } finally {
            Context.bindContext(null);
        }
    }

    @Override
    public void destroy() {
        ratelConfig.onDestroy(servletContext);
        servletContext = null;
    }
    
    protected RatelConfig createRatelConfig(Class<? extends RatelConfig> ratelConfigClass) {
        return FilterUtils.createRatelConfig(ratelConfigClass);
    }

    protected Class<? extends RatelConfig> getConfigClass(FilterConfig filterConfig) {
        return FilterUtils.getConfigClass(filterConfig);
    }

    protected List<String> getPackageNames(FilterConfig filterConfig) {
        return FilterUtils.getPackageNames(filterConfig);
    }

    protected int getMaxRequestSize(FilterConfig filterConfig) {
        return FilterUtils.getMaxRequestSize(filterConfig);
    }
    
    protected Mode getMode(FilterConfig filterConfig) {
        return FilterUtils.getMode(filterConfig);
    }
}
