package com.google.ratel;

import com.google.ratel.service.handler.RequestHandler;
import java.io.*;
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
        this.ratelConfig = FilterUtils.globalInit(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException,
        ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {

            // Apply request.setCharacterEncoding before wrapping request in RatelHttpServletRequest
             String charset = ratelConfig.getCharset();
            if (charset != null) {

                try {
                    request.setCharacterEncoding(charset);

                } catch (UnsupportedEncodingException ex) {
                    String msg = "The character encoding " + charset + " is invalid.";
                    ratelConfig.getLogService().error(msg, ex);
                }
            }

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
}
