package com.google.ratel;

import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import com.google.ratel.deps.fileupload.FileItem;
import com.google.ratel.core.RatelHttpServletRequest;
import com.google.ratel.core.RatelHttpServletResponse;
import com.google.ratel.service.template.TemplateService;
import com.google.ratel.util.*;
import java.io.*;

public class Context {

    /**
     * The user's session Locale key: &nbsp; <tt>locale</tt>.
     */
    public static final String LOCALE = "locale";

    private static ThreadLocal<Context> THREAD_LOCAL_CONTEXT = new ThreadLocal<Context>();

    protected ServletContext servletContext;

    protected RatelHttpServletRequest request;

    protected RatelHttpServletResponse response;

    protected RatelConfig ratelConfig;

    /**
     * The HTTP method is POST flag.
     */
    protected Boolean isPost;
    
    protected Boolean isGet;
    
    protected Boolean isDelete;
    
    protected Boolean isPut;
    
    protected Boolean isHead;
    
    protected Boolean isTrace;
    
    protected Boolean isOptions;

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
     *
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
     * Returns a map of <tt>FileItem arrays</tt> keyed on request parameter name for "multipart" POST requests (file uploads). Thus each map
     * entry will consist of one or more <tt>FileItem</tt> objects.
     *
     * @return map of <tt>FileItem arrays</tt> keyed on request parameter name for "multipart" POST requests
     */
    public Map<String, FileItem[]> getFileItemMap() {
        return getRequest().getFileItemMap();
    }

    /**
     * Returns the value of a request parameter as a FileItem, for "multipart" POST requests (file uploads), or null if the parameter is not
     * found.
     * <p/>
     * If there were multivalued parameters in the request (ie two or more file upload fields with the same name), the first fileItem in the
     * array is returned.
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

    /**
     * Return the users Locale.
     * <p/>
     * If the users Locale is stored in their session this will be returned. Else if the click-app configuration defines a default Locale
     * this value will be returned, otherwise the request's Locale will be returned.
     * <p/>
     * To override the default request Locale set the users Locale using the {@link #setLocale(Locale)} method.
     * <p/>
     * Pages and Controls obtain the users Locale using this method.
     *
     * @return the users Locale in the session, or if null the request Locale
     */
    public Locale getLocale() {
        Locale locale = (Locale) getSessionAttribute(LOCALE);

        if (locale == null) {

            if (ratelConfig.getLocale() != null) {
                locale = ratelConfig.getLocale();

            } else {
                locale = getRequest().getLocale();
            }
        }

        return locale;
    }

    /**
     * This method stores the given Locale in the users session. If the given Locale is null, the "locale" attribute will be removed from
     * the session.
     * <p/>
     * The Locale object is stored in the session using the {@link #LOCALE} key.
     *
     * @param locale the Locale to store in the users session using the key "locale"
     */
    public void setLocale(Locale locale) {
        if (locale == null && hasSession()) {
            getSession().removeAttribute(LOCALE);
        } else {
            setSessionAttribute(LOCALE, locale);
        }
    }

    /**
     * Return the named session attribute, or null if not defined.
     * <p/>
     * If the session is not defined this method will return null, and a session will not be created.
     * <p/>
     * This method supports {@link FlashAttribute} which when accessed are then removed from the session.
     *
     * @param name the name of the session attribute
     * @return the named session attribute, or null if not defined
     */
    public Object getSessionAttribute(String name) {
        if (hasSession()) {
            Object object = getSession().getAttribute(name);

            if (object instanceof FlashAttribute) {
                FlashAttribute flashObject = (FlashAttribute) object;
                object = flashObject.getValue();
                removeSessionAttribute(name);
            }

            return object;
        } else {
            return null;
        }
    }

    /**
     * This method will set the named object in the HttpSession.
     * <p/>
     * This method will create a session if one does not already exist.
     *
     * @param name the storage name for the object in the session
     * @param value the object to store in the session
     */
    public void setSessionAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    /**
     * Remove the named attribute from the session. If the session does not exist or the name is null, this method does nothing.
     *
     * @param name of the attribute to remove from the session
     */
    public void removeSessionAttribute(String name) {
        if (hasSession() && name != null) {
            getSession().removeAttribute(name);
        }
    }

    /**
     * Return true if there is a session and it contains the named attribute.
     *
     * @param name the name of the attribute
     * @return true if the session contains the named attribute
     */
    public boolean hasSessionAttribute(String name) {
        return (getSessionAttribute(name) != null);
    }

    /**
     * Return true if a HttpSession exists, or false otherwise.
     *
     * @return true if a HttpSession exists, or false otherwise
     */
    public boolean hasSession() {
        return (request.getSession(false) != null);
    }

    public boolean isPost() {
        if (isPost == null) {
            this.isPost = RatelUtils.isPost(request);
        }
        return isPost.booleanValue();
    }

    /**
     * Return a rendered Velocity template and model data.
     * <p/>
     * Example method usage:
     * <pre class="codeJava">
     * <span class="kw">public String</span> toString() { Map model = getModel();
     * <span class="kw">return</span> getContext().renderTemplate(<span class="st">"/custom-table.htm"</span>, model); } </pre>
     *
     * @param templatePath the path of the Velocity template to render
     * @param model the model data to merge with the template
     * @return rendered Velocity template merged with the model data
     * @throws RuntimeException if an error occurs
     */
    public String renderTemplate(String templatePath, Map<String, ?> model) {

        if (templatePath == null) {
            String msg = "Null templatePath parameter";
            throw new IllegalArgumentException(msg);
        }

        if (model == null) {
            String msg = "Null model parameter";
            throw new IllegalArgumentException(msg);
        }

        StringWriter stringWriter = new StringWriter(1024);

        TemplateService templateService = getRatelConfig().getTemplateService();

        try {
            templateService.renderTemplate(templatePath, model, stringWriter);

        } catch (Exception e) {
            //String msg = "Error occurred rendering template: " + templatePath + "\n";
            //getRatelConfig().getLogService().error(msg, e);

            throw new RuntimeException(e);
        }

        return stringWriter.toString();
    }
}
