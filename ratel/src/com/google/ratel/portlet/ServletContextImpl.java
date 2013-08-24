package com.google.ratel.portlet;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

public class ServletContextImpl implements ServletContext {
  private PortletContext portletContext;


  public ServletContextImpl(PortletContext portletContext) {
    this.portletContext = portletContext;
  }


  @Override
  public ServletContext getContext(String uripath) {
    throw new UnsupportedOperationException();
  }


  //@Override
  public String getContextPath() {
    throw new UnsupportedOperationException();
  }


  @Override
  public int getMajorVersion() {
    return 2;
  }


  @Override
  public int getMinorVersion() {
    return 4;
  }


  @Override
  public String getMimeType(String file) {
    return portletContext.getMimeType(file);
  }


  @Override
  public Set getResourcePaths(String path) {
    return portletContext.getResourcePaths(path);
  }


  @Override
  public URL getResource(String path) throws MalformedURLException {
    return portletContext.getResource(path);
  }


  @Override
  public InputStream getResourceAsStream(String path) {
    return portletContext.getResourceAsStream(path);
  }


  @Override
  public RequestDispatcher getRequestDispatcher(String path) {
    PortletRequestDispatcher portletDispatcher = portletContext.getRequestDispatcher(path);
    return portletDispatcher == null ? null : new RequestDispatcherImpl(portletDispatcher);
  }


  @Override
  public RequestDispatcher getNamedDispatcher(String name) {
    PortletRequestDispatcher portletDispatcher = portletContext.getNamedDispatcher(name);
    return portletDispatcher == null ? null : new RequestDispatcherImpl(portletDispatcher);
  }


  @Override
  public Servlet getServlet(String name) throws ServletException {
    throw new UnsupportedOperationException();
  }


  @Override
  public Enumeration getServlets() {
    throw new UnsupportedOperationException();
  }


  @Override
  public Enumeration getServletNames() {
    throw new UnsupportedOperationException();
  }


  @Override
  public void log(String msg) {
    portletContext.log(msg);
  }


  @Override
  public void log(Exception exception, String msg) {
    portletContext.log(msg, exception);
  }


  @Override
  public void log(String message, Throwable throwable) {
    portletContext.log(message, throwable);
  }


  @Override
  public String getRealPath(String path) {
    return portletContext.getRealPath(path);
  }


  @Override
  public String getServerInfo() {
    return portletContext.getServerInfo();
  }


  @Override
  public String getInitParameter(String name) {
    return portletContext.getInitParameter(name);
  }


  @Override
  public Enumeration getInitParameterNames() {
    return portletContext.getInitParameterNames();
  }


  @Override
  public Object getAttribute(String name) {
    return portletContext.getAttribute(name);
  }


  @Override
  public Enumeration getAttributeNames() {
    return portletContext.getAttributeNames();
  }


  @Override
  public void setAttribute(String name, Object object) {
    portletContext.setAttribute(name, object);
  }


  @Override
  public void removeAttribute(String name) {
    portletContext.removeAttribute(name);
  }


  @Override
  public String getServletContextName() {
    throw new UnsupportedOperationException();
  }


  public PortletContext getPortletContext() {
    return portletContext;
  }
}
