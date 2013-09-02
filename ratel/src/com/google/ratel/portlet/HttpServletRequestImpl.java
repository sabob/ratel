package com.google.ratel.portlet;

import javax.portlet.ResourceRequest;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.portlet.PortletSession;

public class HttpServletRequestImpl implements HttpServletRequest {
  private ServletContext servletContext;
  private ResourceRequest resourceRequest;
  private HttpSession session;


  public HttpServletRequestImpl(ResourceRequest resourceRequest, ServletContext servletContext) {
    this.resourceRequest = resourceRequest;
    this.servletContext = servletContext;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getAuthType,...
   */
  @Override
  public String getAuthType() {
    return resourceRequest.getAuthType();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getCookies,...   *
   */
  @Override
  public Cookie[] getCookies() {
    return resourceRequest.getCookies();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be based on the properties
   * provided by the getProperties method of the PortletRequest interface: ...,getDateHeader,...
   */
  @Override
  public long getDateHeader(String name) {
      String value = getHeader(name);
        if (value == null) {
            return -1;
        }

        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
        try {
            return df.parse(value).getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't convert header to date " + name + ": " + value);
        }
  }

  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be based on the properties
   * provided by the getProperties method of the PortletRequest interface: ...,getHeader,...
   */
  @Override
  public String getHeader(String name) {
    return resourceRequest.getProperty("name");
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be based on the properties
   * provided by the getProperties method of the PortletRequest interface: ...,getHeaders,...
   */
  @Override
  public Enumeration getHeaders(String name) {
    return resourceRequest.getProperties(name);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be based on the properties
   * provided by the getProperties method of the PortletRequest interface: ...,getHeaderNames,...
   */
  @Override
  public Enumeration getHeaderNames() {
    return resourceRequest.getPropertyNames();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be based on the properties
   * provided by the getProperties method of the PortletRequest interface: ...,getIntHeader,...
   */
  @Override
  public int getIntHeader(String name) {
    return (getHeader(name) == null) ? -1 : Integer.parseInt(getHeader(name));
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the ResourceRequest of
   * similar name: ...,getMethod,...
   */
  @Override
  public String getMethod() {
    return resourceRequest.getMethod();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return the path and query
   * string information used to obtain the PortletRequestDispatcher object:
   * ..., getPathInfo,...
   * 
   * SERVLET-2_4-FR-SPEC SRV.15.1.3.2
   * Returns any extra path information associated with the URL the client sent
   * when it made this resourceRequest. The extra path information follows the servlet path
   * but precedes the query string and will start with a / character.
   * This method returns null if there was no extra path information.
   * Same as the value of the CGI variable PATH_INFO.
   * 
   * Returns: a String, decoded by the web container, specifying extra path
   * information that comes after the servlet path but before the query string in the
   * request URL; or null if the URL does not have any extra path information
   * 
   * TODO: Should be implemented as per specification.
   */
  @Override
  public String getPathInfo() {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return the path and query
   * string information used to obtain the PortletRequestDispatcher object:
   * ..., getPathTranslated,...
   * 
   * SERVLET-2_4-FR-SPEC SRV.15.1.3.2
   * Returns any extra path information after the servlet name but before the
   * query string, and translates it to a real path. Same as the value of the CGI
   * variable PATH_TRANSLATED.    \
   * 
   * If the URL does not have any extra path information, this method returns
   * null or the servlet container cannot translate the virtual path to a real path for
   * any reason (such as when the web application is executed from an archive).
   * The web container does not decode this string.
   * 
   * Returns: a String specifying the real path, or null if the URL does not
   * have any extra path information
   * 
   * TODO: Should be implemented as per specification.
   */
  @Override
  public String getPathTranslated() {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getContextPath,...
   */
  @Override
  public String getContextPath() {
    return resourceRequest.getContextPath();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return the path and query
   * string information used to obtain the PortletRequestDispatcher object:
   * ..., getQueryString,...
   * 
   * SERVLET-2_4-FR-SPEC SRV.15.1.3.2
   * Returns the query string that is contained in the request URL after the path.
   * This method returns null if the URL does not have a query string. Same as
   * the value of the CGI variable QUERY_STRING.
   * 
   * Returns: a String containing the query string or null if the URL contains
   * no query string. The value is not decoded by the container.
   * 
   * TODO: Should be implemented as per specification.
   */
  @Override
  public String getQueryString() {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getRemoteUser,...
   */
  @Override
  public String getRemoteUser() {
    return resourceRequest.getRemoteUser();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., isUserInRole, ...
   */
  @Override
  public boolean isUserInRole(String role) {
    return resourceRequest.isUserInRole(role);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getUserPrincipal,...
   */
  @Override
  public Principal getUserPrincipal() {
    return resourceRequest.getUserPrincipal();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getRequestedSessionId,...
   */
  @Override
  public String getRequestedSessionId() {
    return resourceRequest.getRequestedSessionId();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return the path and query
   * string information used to obtain the PortletRequestDispatcher object:
   * ..., getRequestURI,...
   * 
   * SERVLET-2_4-FR-SPEC SRV.15.1.3.2
   * Returns the part of this request's URL from the protocol name up to the query
   * string in the first line of the HTTP resourceRequest. The web container does not
   * decode this String. For example:
   * 
   * +--------------------------------------------------+-------------------------+
   * | First line of HTTP request                       | Returned Value          |
   * +==================================================+=========================|
   * | POST /some/path.html HTTP/1.1                    | /some/path.html         |
   * | GET http://foo.bar/a.html HTTP/1.0               | /a.html                 |
   * | HEAD /xyz?a=b HTTP/1.1                           | /xyz                    |
   * +--------------------------------------------------+-------------------------+
   * 
   * Returns: a String containing the part of the URL from the protocol name
   * up to the query string
   * See Also: HttpUtils.getRequestURL(HttpServletRequest)
   * 
   * TODO: Should be implemented as per specification.
   */
  @Override
  public String getRequestURI() {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return null: ..., getRequestURL,...
   */
  @Override
  public StringBuffer getRequestURL() {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return the path and query
   * string information used to obtain the PortletRequestDispatcher object:
   * ..., getServletPath,...
   *
   * SERVLET-2_4-FR-SPEC SRV.15.1.3.2
   * Returns the part of this request’s URL that calls the servlet. This path starts
   * with a "/" character and includes either the servlet name or a path to the servlet,
   * but does not include any extra path information or a query string. Same as
   * the value of the CGI variable SCRIPT_NAME.
   * This method will return an empty string ("") if the servlet used to process this
   * request was matched using the "/*" pattern.
   *
   * Returns: a String containing the name or path of the servlet being called,
   * as specified in the request URL, decoded, or an empty string if the servlet
   * used to process the request is matched using the "/*" pattern.
   *
   * TODO: Should be implemented as per specification.
   */
  @Override
  public String getServletPath() {
      return resourceRequest.getResourceID();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., getSession, ...
   */
  @Override
  public HttpSession getSession(boolean create) {
      if (create && session == null) {
          PortletSession portletSession = getResourceRequest().getPortletSession(create);
          session = new HttpSessionImpl(portletSession, getServletContext());
      }
      return session;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., getSession, ...
   */
  @Override
  public HttpSession getSession() {
    return getSession(true);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., isRequestedSessionIdValid,...
   */
  @Override
  public boolean isRequestedSessionIdValid() {
    return resourceRequest.isRequestedSessionIdValid();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., isRequestedSessionIdFromCookie, ...
   */
  @Override
  public boolean isRequestedSessionIdFromCookie() {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., isRequestedSessionIdFromURL, ...
   */
  @Override
  public boolean isRequestedSessionIdFromURL() {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., isRequestedSessionIdFromUrl, ...
   */
  @Override
  public boolean isRequestedSessionIdFromUrl() {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getAttribute,...
   */
  @Override
  public Object getAttribute(String name) {
    return resourceRequest.getAttribute(name);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getAttributeNames,...
   */
  @Override
  public Enumeration getAttributeNames() {

    return resourceRequest.getAttributeNames();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the ResourceRequest of
   * similar name: ...,getCharacterEncoding,...
   */
  @Override
  public String getCharacterEncoding() {
    return resourceRequest.getCharacterEncoding();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the ResourceRequest of
   * similar name: ...,setCharacterEncoding,...
   */
  @Override
  public void setCharacterEncoding(String value) throws UnsupportedEncodingException {
    resourceRequest.setCharacterEncoding(value);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the ResourceRequest of
   * similar name: ...,getContentLength,...
   */
  @Override
  public int getContentLength() {
    return resourceRequest.getContentLength();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the ResourceRequest of
   * similar name: ...,getContentType,...
   */
  @Override
  public String getContentType() {
    return resourceRequest.getContentType();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The HttpServletRequest getInputStream must be equivalent to the method getPortletInputStream of the ResourceRequest
   */
  @Override
  public ServletInputStream getInputStream() throws IOException {
    return new ServletInputStreamImpl(resourceRequest.getPortletInputStream());
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the PortletRequest of
   * similar name with the provision defined in PLT.18.1.1 Query Strings in Request Dispatcher Paths Section:
   * ..., getParameter,...
   */
  @Override
  public String getParameter(String name) {
    return resourceRequest.getParameter(name);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the PortletRequest of
   * similar name with the provision defined in PLT.18.1.1 Query Strings in Request Dispatcher Paths Section:
   * ..., getParameterNames,...
   */
  @Override
  public Enumeration getParameterNames() {
    return resourceRequest.getParameterNames();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the PortletRequest of
   * similar name with the provision defined in PLT.18.1.1 Query Strings in Request Dispatcher Paths Section:
   * ..., getParameterValues,...
   */
  @Override
  public String[] getParameterValues(String name) {
    return resourceRequest.getParameterValues(name);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the PortletRequest of
   * similar name with the provision defined in PLT.18.1.1 Query Strings in Request Dispatcher Paths Section:
   * ..., getParameterMap,...
   */
  @Override
  public Map getParameterMap() {
    return resourceRequest.getParameterMap();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The getProtocol method of the HttpServletRequest must always return "HTTP/1.1".
   */
  @Override
  public String getProtocol() {
    return "HTTP/1.1";
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getScheme,...
   */
  @Override
  public String getScheme() {
    return resourceRequest.getScheme();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getServerName,...
   */
  @Override
  public String getServerName() {
    return resourceRequest.getServerName();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getServerPort,...
   */
  @Override
  public int getServerPort() {
    return resourceRequest.getServerPort();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods of the ResourceRequest of
   * similar name: ...,getReader,...
   */
  @Override
  public BufferedReader getReader() throws IOException {
    return resourceRequest.getReader();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return null: ..., getRemoteAddr,...
   */
  @Override
  public String getRemoteAddr() {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return null: ..., getRemoteHost,...
   */
  @Override
  public String getRemoteHost() {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., setAttribute,...
   */
  @Override
  public void setAttribute(String name, Object value) {
    resourceRequest.setAttribute(name, value);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., removeAttribute,...
   */
  @Override
  public void removeAttribute(String name) {
    resourceRequest.removeAttribute(name);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getLocale,...
   */
  @Override
  public Locale getLocale() {
    return resourceRequest.getLocale();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., getLocales,...
   */
  @Override
  public Enumeration getLocales() {
    return resourceRequest.getLocales();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must be equivalent to the methods
   * of the PortletRequest of similar name: ..., isSecure,...
   */
  @Override
  public boolean isSecure() {
    return resourceRequest.isSecure();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must provide the functionality defined by the
   * Servlet Specification: ..., getRequestDispatcher, ...
   */
  @Override
  public RequestDispatcher getRequestDispatcher(String path) {
    throw new UnsupportedOperationException();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return null: ..., getRealPath,...
   */
  @Override
  public String getRealPath(String path) {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return 0: ..., getRemotePort,...
   */
  @Override
  public int getRemotePort() {
    return 0;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return null: ..., getLocalName,...
   */
  @Override
  public String getLocalName() {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return null: ..., getLocalAddr,...
   */
  @Override
  public String getLocalAddr() {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletRequest must return 0: ..., getLocalPort,...
   */
  @Override
  public int getLocalPort() {
    return 0;
  }


  public ResourceRequest getResourceRequest() {
    return resourceRequest;
  }


  public ServletContext getServletContext() {
    return servletContext;
  }
}