/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.util;

import com.google.ratel.*;
import com.google.ratel.core.*;
import com.google.ratel.deps.lang3.*;
import java.util.*;
import javax.servlet.*;

/**
 *
 */
public class FilterUtils {

    public static final String CONTEXT_PARAM_CONFIG_CLASS = "ratel.configClass";

    public static final String CONTEXT_PARAM_PACKAGE = "ratel.package";

    public static final String CONTEXT_PARAM_MAX_REQUEST_SIZE = "ratel.maxRequestSize";

    public static final String CONTEXT_PARAM_MODE = "ratel.mode";

    public static final String CONTEXT_PARAM_CHARSET = "ratel.charset";
    
    public static final String CONTEXT_PARAM_URL_PREFIX = "ratel.urlPrefix";
    
    public static RatelConfig globalInit(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        
        // Check if config has already been loaded
        RatelConfig ratelConfig = RatelUtils.getRatelConfig(servletContext);
        if (ratelConfig != null) {
            return ratelConfig;
        }

        Class<? extends RatelConfig> ratelConfigClass = FilterUtils.getConfigClass(filterConfig);
        ratelConfig = createRatelConfig(ratelConfigClass);

        setRatelConfig(ratelConfig, servletContext);

        Mode mode = FilterUtils.getMode(filterConfig);
        ratelConfig.setMode(mode);

        String charset = FilterUtils.getCharset(filterConfig);
        ratelConfig.setCharset(charset);

        List<String> packageNameList = FilterUtils.getPackageNames(filterConfig);
        ratelConfig.setPackageNameList(packageNameList);

        int maxRequestSize = getMaxRequestSize(filterConfig);
       ratelConfig.setMaxRequestSize(maxRequestSize);

        String urlPrefix = getUrlPrefix(filterConfig);
       ratelConfig.setUrlPrefix(urlPrefix);

        ratelConfig.onInit(servletContext);

        return ratelConfig;
    }
    
    public static void setRatelConfig(RatelConfig ratelConfig, ServletContext servletContext) {
        servletContext.setAttribute(RatelConfig.CONTEXT_NAME, ratelConfig);
    }

    public static RatelConfig createRatelConfig(Class<? extends RatelConfig> ratelConfigClass) {

        try {
            if (ratelConfigClass == null) {
                ratelConfigClass = RatelConfig.class;
            }

            RatelConfig localRatelConfig = ratelConfigClass.newInstance();

            System.out.println("[Ratel] [info ] created RatelConfig: " + localRatelConfig.getClass().getName());
            return localRatelConfig;

        } catch (Exception e) {
            System.out.println("[Ratel] [error] Could not instantiate RatelConfig: " + ratelConfigClass + ". Using " + RatelConfig.class.getName() + " instead.");
            e.printStackTrace();
            return new RatelConfig();
        }
    }

    public static Class<? extends RatelConfig> getConfigClass(FilterConfig filterConfig) {

        ServletContext servletContext = filterConfig.getServletContext();

        String value = servletContext.getInitParameter(CONTEXT_PARAM_CONFIG_CLASS);
        if (StringUtils.isNotBlank(value)) {

            try {
                Class configClass = Class.forName(value);
                System.out.println("[Ratel] [info ] RatelConfig class specified: " + configClass.getName());
                return configClass;

            } catch (ClassNotFoundException e) {
                System.out.println("[Ratel] [error] Could not load custom RatelConfig: " + value + ". Using " + RatelConfig.class.getName() + " instead.");
                e.printStackTrace();
            }
        }

        warnIfInitParamUsed(filterConfig, CONTEXT_PARAM_CONFIG_CLASS);
        return null;
    }

    public static List<String> getPackageNames(FilterConfig filterConfig) {

        ServletContext servletContext = filterConfig.getServletContext();

        List<String> packageNameList = new ArrayList<String>();
        String packageNames = servletContext.getInitParameter(CONTEXT_PARAM_PACKAGE);

        if (StringUtils.isNotBlank(packageNames)) {
            StringTokenizer st = new StringTokenizer(packageNames, ",");
            while (st.hasMoreTokens()) {
                String packageName = st.nextToken().trim();
                packageNameList.add(packageName);
            }
        }

        warnIfInitParamUsed(filterConfig, CONTEXT_PARAM_PACKAGE);
        return packageNameList;
    }

    public static int getMaxRequestSize(FilterConfig filterConfig) {

        ServletContext servletContext = filterConfig.getServletContext();

        String str = servletContext.getInitParameter(CONTEXT_PARAM_MAX_REQUEST_SIZE);
        if (StringUtils.isNotBlank(str)) {
            int maxRequestSize = Integer.parseInt(str);
            return maxRequestSize;
        }

        warnIfInitParamUsed(filterConfig, CONTEXT_PARAM_MAX_REQUEST_SIZE);
        return RatelConfig.DEFAULT_MAX_REQUEST_SIZE;
    }

    public static String getUrlPrefix(FilterConfig filterConfig) {

        ServletContext servletContext = filterConfig.getServletContext();

        String str = servletContext.getInitParameter(CONTEXT_PARAM_URL_PREFIX);
        if (StringUtils.isNotBlank(str)) {
            if (!StringUtils.startsWith(str, "/")) {
                str = str + "/";
            }
            return str;
        }

        warnIfInitParamUsed(filterConfig, CONTEXT_PARAM_URL_PREFIX);
        return null;
    }

    public static Mode getMode(FilterConfig filterConfig) {

        ServletContext servletContext = filterConfig.getServletContext();

        String str = servletContext.getInitParameter(CONTEXT_PARAM_MODE);

        if (StringUtils.isNotBlank(str)) {
            Mode mode = Mode.getMode(str);
            return mode;
        }

        warnIfInitParamUsed(filterConfig, CONTEXT_PARAM_MODE);
        return null;
    }

    public static String getCharset(FilterConfig filterConfig) {

        ServletContext servletContext = filterConfig.getServletContext();

        String str = servletContext.getInitParameter(CONTEXT_PARAM_CHARSET);

        if (StringUtils.isNotBlank(str)) {
            return str;
        }

        warnIfInitParamUsed(filterConfig, CONTEXT_PARAM_CHARSET);
        return null;
    }
    
    public static void warnIfInitParamUsed(FilterConfig filterConfig, String param) {
        String str = filterConfig.getInitParameter(param);
        if (StringUtils.isNotBlank(str)) {
            System.out.println("The parameter '" + param + "' must be specified as a <context-param> and not as a <init-param> of the filter.");            
        }
    }
}
