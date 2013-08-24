package com.google.ratel;

import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import com.google.ratel.deps.fileupload.FileItem;
import com.google.ratel.core.RatelHttpServletRequest;
import com.google.ratel.core.RatelHttpServletResponse;

public class Context {
    
    /**
     * The attribute key used for storing any error that occurred while
     * Context is created.
     */
    public static final String CONTEXT_FATAL_ERROR = "_context_fatal_error";

    private static ThreadLocal<Context> THREAD_LOCAL_CONTEXT = new ThreadLocal<Context>();

    private ServletContext servletContext;
    private RatelHttpServletRequest request;
    private RatelHttpServletResponse response;
    private RatelConfig ratelConfig;
    
    public Context() {
    }

    /**
     * Instantiates a new request context.
     *
     * @param context the context
     * @param request the request
     * @param response the response
     */
    public Context(RatelConfig ratelConfig, ServletContext context, RatelHttpServletRequest request, RatelHttpServletResponse response) {
        servletContext = context;
        this.request = request;
        this.response = response;
        this.ratelConfig = ratelConfig;
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
     * @return the request
     */
    public RatelHttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(RatelHttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return the response
     */
    public RatelHttpServletResponse getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(RatelHttpServletResponse response) {
        this.response = response;
    }

    /**
     * Return the servlet session object.
     *
     * @return the servlet session object
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }
    
    public static boolean hasContext() {
        return THREAD_LOCAL_CONTEXT.get() != null;
    }

    /**
     * Return the request context object for the current thread.
     *
     * @return the request context object for the current thread
     */
    public static Context getContext() {
        return THREAD_LOCAL_CONTEXT.get();
    }

    /**
     * Bind the request context to the current thread.
     * @param requestContext the request context
     */
    public static void bindContext(Context requestContext) {
        if (requestContext != null) {
            THREAD_LOCAL_CONTEXT.set(requestContext);
        } else {
            THREAD_LOCAL_CONTEXT.remove();
        }
    }
    
    /**
     * Returns a map of <tt>FileItem arrays</tt> keyed on request parameter
     * name for "multipart" POST requests (file uploads). Thus each map entry
     * will consist of one or more <tt>FileItem</tt> objects.
     *
     * @return map of <tt>FileItem arrays</tt> keyed on request parameter name
     * for "multipart" POST requests
     */
    public Map<String, FileItem[]> getFileItemMap() {
        return getRequest().getFileItemMap();
    }

    /**
     * Returns the value of a request parameter as a FileItem, for
     * "multipart" POST requests (file uploads), or null if the parameter
     * is not found.
     * <p/>
     * If there were multivalued parameters in the request (ie two or more
     * file upload fields with the same name), the first fileItem
     * in the array is returned.
     *
     * @param name the name of the parameter of the fileItem to retrieve
     *
     * @return the fileItem for the specified name
     */
    public FileItem getFileItem(String name) {
        FileItem fileItem = getRequest().getFileItem(name);
        return fileItem;
    }

    public FileItem[] getFileItems(String name) {
        FileItem[] fileItems = getRequest().getFileItems(name);
        return fileItems;

    }

    /**
     * @return the ratelConfig
     */
    public RatelConfig getRatelConfig() {
        return ratelConfig;
    }

    /**
     * @param ratelConfig the ratelConfig to set
     */
    public void setRatelConfig(RatelConfig ratelConfig) {
        this.ratelConfig = ratelConfig;
    }
}
