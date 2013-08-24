/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.portlet;

import com.google.ratel.Context;
import com.google.ratel.RatelConfig;
import static com.google.ratel.RatelFilter.INIT_PARAM_CONFIG_CLASS;
import com.google.ratel.service.handler.RequestHandler;
import java.io.*;
import java.util.*;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import com.google.ratel.deps.lang3.*;
import static com.google.ratel.RatelFilter.INIT_PARAM_MAX_REQUEST_SIZE;
import static com.google.ratel.RatelFilter.INIT_PARAM_PACKAGE_NAMES;
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
            portletContext.log("ServletContext could not be initialized - " + ServletContext.class.getName(), e);
            throw new PortletException(e);
        }

        Class<? extends RatelConfig> ratelConfigClass = getConfigClass(filterConfig);

        List<String> packageNameList = getPackageNames(config);

        int maxRequestSize = getMaxRequestSize(config);

        ratelConfig = createRatelConfig(ratelConfigClass);
        ratelConfig.onInit(getServletContext(), packageNameList, maxRequestSize);
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
                // TODO
            }

        } finally {
            Context.bindContext(null);
        }
    }

    public RatelConfig createRatelConfig(Class<? extends RatelConfig> ratelConfigClass) {
        try {
            if (ratelConfigClass == null) {
                return new RatelConfig();
            }
            RatelConfig localRatelConfig = ratelConfigClass.newInstance();

            System.out.println("RatelConfig created: " + localRatelConfig.getClass().getName());
            return localRatelConfig;

        } catch (Exception e) {
            System.out.println("Could not instantiate RatelConfig: " + ratelConfigClass + ". Using " + RatelConfig.class.getName());
            e.printStackTrace();
            return new RatelConfig();
        }
    }

    protected ServletContext createServletContext(PortletContext portletContext) {
        ServletContext localServletContext = new ServletContextImpl(portletContext);
        return localServletContext;
    }

    protected FilterConfig createFilterConfig(ServletContext servletContext, PortletConfig portletConfig) {
        FilterConfig localFilterConfig = new FilterConfigImpl(getClass().getName(), portletConfig, servletContext);
        return localFilterConfig;
    }

    protected HttpServletRequest createServletRequest(ResourceRequest resourceRequest, ServletContext servletContext) {
        HttpServletRequest localServletContext = new HttpServletRequestImpl(resourceRequest, servletContext);
        return localServletContext;
    }

    protected HttpServletResponse createServletResponse(ResourceResponse resourceResponse, ServletContext servletContext) {
        HttpServletResponse servletResponse = new HttpServletResponseImpl(resourceResponse);
        return servletResponse;
    }
    
    protected Class<? extends RatelConfig> getConfigClass(FilterConfig filterConfig) {

        Class configClass = null;
        String value = filterConfig.getInitParameter(INIT_PARAM_CONFIG_CLASS);
        if (StringUtils.isNotBlank(value)) {
            try {
                configClass= Class.forName(value);
                System.out.println("RatelConfig class specified: " + configClass.getName());
            } catch (ClassNotFoundException e) {
                System.out.println("Could not load custom RatelConfig: " + value + ". Using " + RatelConfig.class.getName());
                e.printStackTrace();
            }
        }
        return configClass;
    }

    protected List<String> getPackageNames(PortletConfig config) {
        List<String> packageNameList = new ArrayList<String>();
        String packageNames = config.getInitParameter(INIT_PARAM_PACKAGE_NAMES);
        if (StringUtils.isNotBlank(packageNames)) {
            StringTokenizer st = new StringTokenizer(packageNames, ",");
            while (st.hasMoreTokens()) {
                String packageName = st.nextToken().trim();
                packageNameList.add(packageName);
            }
        }
        return packageNameList;
    }

    protected int getMaxRequestSize(PortletConfig config) {
        String str = config.getInitParameter(INIT_PARAM_MAX_REQUEST_SIZE);
        if (StringUtils.isNotBlank(str)) {
            int maxRequestSize = Integer.parseInt(str);
            return maxRequestSize;
        }
        return -1;
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
}
