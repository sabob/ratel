package com.google.ratel.portlet;

import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class HttpSessionImpl implements HttpSession {
  private PortletSession session;
  private ServletContext servletContext;

  public HttpSessionImpl(PortletSession session, ServletContext servletContext) {
    this.session = session;
    this.servletContext = servletContext;
  }


  public PortletSession getPortletSession() {
    return session;
  }


  @Override
  public long getCreationTime() {
    return session.getCreationTime();
  }


  @Override
  public String getId() {
    return session.getId();
  }


  @Override
  public long getLastAccessedTime() {
    return session.getLastAccessedTime();
  }


  @Override
  public ServletContext getServletContext() {
    return servletContext;
  }


  @Override
  public void setMaxInactiveInterval(int interval) {
  }


  @Override
  public int getMaxInactiveInterval() {
    return session.getMaxInactiveInterval();
  }


  @Override @SuppressWarnings({"deprecation"})
  public javax.servlet.http.HttpSessionContext getSessionContext() {
    throw new UnsupportedOperationException();
  }


  @Override
  public Object getAttribute(String name) {
    return session.getAttribute(name);
  }


  @Override
  public Object getValue(String name) {
    return getAttribute(name);
  }


  @Override
  public Enumeration<String> getAttributeNames() {
    return session.getAttributeNames();
  }


  @Override @SuppressWarnings({"unchecked"})
  public String[] getValueNames() {
    List<String> attributeNames = new ArrayList<String>();
    Enumeration<String> enu = getAttributeNames();
    while(enu.hasMoreElements()) attributeNames.add(enu.nextElement());
    String[] result = new String[attributeNames.size()];
    return attributeNames.toArray(result);
  }


  @Override
  public void setAttribute(String name, Object value) {
    session.setAttribute(name, value);
  }


  @Override
  public void putValue(String name, Object value) {
    setAttribute(name, value);
  }


  @Override
  public void removeAttribute(String name) {
    session.removeAttribute(name);
  }


  @Override
  public void removeValue(String name) {
    removeAttribute(name);
  }


  @Override
  public void invalidate() {
    session.invalidate();
  }


  @Override
  public boolean isNew() {
    return session.isNew();
  }
}
