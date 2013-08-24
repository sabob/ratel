package com.google.ratel;

import com.google.ratel.service.handler.RequestHandler;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.*;
import com.google.ratel.deps.lang3.StringUtils;
import com.google.ratel.service.error.ErrorHandlerService;
import javax.servlet.Filter;

/**
 *
 */
public class RatelFilter implements Filter {
    
    public static final String INIT_PARAM_CONFIG_CLASS = "configClass";

    public static final String INIT_PARAM_PACKAGE_NAMES = "packageNames";

    public static final String INIT_PARAM_MAX_REQUEST_SIZE = "maxRequestSize";

    protected ServletContext servletContext;

    protected RatelConfig ratelConfig;

    public RatelFilter() {
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Class<? extends RatelConfig> ratelConfigClass = getConfigClass(filterConfig);
        ratelConfig = createRatelConfig(ratelConfigClass);

        this.servletContext = filterConfig.getServletContext();
        List<String> packageNameList = getPackageNames(filterConfig);
        int maxRequestSize = getMaxRequestSize(filterConfig);

        ratelConfig.onInit(servletContext, packageNameList, maxRequestSize);
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

    protected List<String> getPackageNames(FilterConfig filterConfig) {
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

    protected int getMaxRequestSize(FilterConfig filterConfig) {
        String str = filterConfig.getInitParameter(INIT_PARAM_MAX_REQUEST_SIZE);
        if (StringUtils.isNotBlank(str)) {
            int maxRequestSize = Integer.parseInt(str);
            return maxRequestSize;
        }
        return -1;
    }
}
