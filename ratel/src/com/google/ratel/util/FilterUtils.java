/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.util;

import com.google.ratel.*;
import com.google.ratel.deps.lang3.*;
import java.util.*;
import javax.servlet.*;

/**
 *
 */
public class FilterUtils {

    public static final String INIT_PARAM_CONFIG_CLASS = "configClass";

    public static final String INIT_PARAM_PACKAGE_NAMES = "packageNames";

    public static final String INIT_PARAM_MAX_REQUEST_SIZE = "maxRequestSize";
    
    public static RatelConfig createRatelConfig(Class<? extends RatelConfig> ratelConfigClass) {
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

    public static Class<? extends RatelConfig> getConfigClass(FilterConfig filterConfig) {

        Class configClass = null;
        String value = filterConfig.getInitParameter(INIT_PARAM_CONFIG_CLASS);
        if (StringUtils.isNotBlank(value)) {
            try {
                configClass = Class.forName(value);
                System.out.println("RatelConfig class specified: " + configClass.getName());
            } catch (ClassNotFoundException e) {
                System.out.println("Could not load custom RatelConfig: " + value + ". Using " + RatelConfig.class.getName());
                e.printStackTrace();
            }
        }
        return configClass;
    }

    public static List<String> getPackageNames(FilterConfig filterConfig) {
        List<String> packageNameList = new ArrayList<String>();
        String packageNames = filterConfig.getInitParameter(INIT_PARAM_PACKAGE_NAMES);
        if (StringUtils.isNotBlank(packageNames)) {
            StringTokenizer st = new StringTokenizer(packageNames, ",");
            while (st.hasMoreTokens()) {
                String packageName = st.nextToken().trim();
                packageNameList.add(packageName);
            }
        }
        return packageNameList;
    }

    public static int getMaxRequestSize(FilterConfig filterConfig) {
        String str = filterConfig.getInitParameter(INIT_PARAM_MAX_REQUEST_SIZE);
        if (StringUtils.isNotBlank(str)) {
            int maxRequestSize = Integer.parseInt(str);
            return maxRequestSize;
        }
        return -1;
    }
}
