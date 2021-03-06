/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.portlet;

import com.google.ratel.Context;
import com.google.ratel.RatelConfig;
import com.google.ratel.service.handler.RequestHandler;
import java.io.*;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import com.google.ratel.util.*;
import javax.servlet.FilterConfig;

/**
 *
 */
public class RatelPortlet extends GenericPortlet {

    private ServletContext servletContext;

    private FilterConfig filterConfig;

    protected RatelConfig ratelConfig;

    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);

        PortletContext portletContext = config.getPortletContext();

        try {
            setServletContext(createServletContext(portletContext));
            setFilterConfig(createFilterConfig(servletContext, config));

        } catch (Exception e) {
            portletContext.log("ServletContext could not be initialized from PortletContext - " + portletContext.getClass().getName(), e);
            throw new PortletException(e);
        }

        this.ratelConfig = FilterUtils.globalInit(filterConfig);
    }

    @Override
    public void destroy() {
        super.destroy();
        ratelConfig.onDestroy(getServletContext());
        setServletContext(null);
    }

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws PortletException, IOException {
        System.out.println("Serve Resource() called ResourceId:" + resourceRequest.getResourceID());

        try {

            HttpServletRequest request = createServletRequest(resourceRequest, getServletContext());
            HttpServletResponse response = createServletResponse(resourceResponse, getServletContext());

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
                // TODO
            }

        } finally {
            Context.bindContext(null);
        }
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
     * @return the filterConfig
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * @param filterConfig the filterConfig to set
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    protected ServletContext createServletContext(PortletContext portletContext) {
        ServletContext localServletContext = new ServletContextImpl(portletContext);
        return localServletContext;
    }

    protected FilterConfig createFilterConfig(ServletContext servletContext, PortletConfig portletConfig) {
        FilterConfig localFilterConfig = new FilterConfigImpl(getClass().getName(), portletConfig, servletContext);
        return localFilterConfig;
    }

    protected HttpServletRequest createServletRequest(final ResourceRequest resourceRequest, final ServletContext servletContext) {
        HttpServletRequest localServletContext = new HttpServletRequestImpl(resourceRequest, servletContext) {

            @Override
            public String getServletPath() {
                return getRequestPath(resourceRequest);
            }
        };

        return localServletContext;
    }

    protected HttpServletResponse createServletResponse(ResourceResponse resourceResponse, ServletContext servletContext) {
        HttpServletResponse servletResponse = new HttpServletResponseImpl(resourceResponse);
        return servletResponse;
    }

    protected String getRequestPath(ResourceRequest resourceRequest) {
      return resourceRequest.getResourceID();
    }
}
