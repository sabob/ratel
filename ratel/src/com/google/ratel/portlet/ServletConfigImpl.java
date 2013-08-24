package com.google.ratel.portlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class ServletConfigImpl implements ServletConfig {
  private final ServletContext servletContext;
  private final String servletName;

  public ServletConfigImpl(ServletContext servletContext, String servletName) {
    this.servletContext = servletContext;
    this.servletName = servletName;
  }


  @Override
  public String getServletName() {
    return servletName;
  }


  @Override
  public ServletContext getServletContext() {
    return servletContext;
  }


  @Override
  public String getInitParameter(String name) {
    return servletContext.getInitParameter(name);
  }


  @Override
  public Enumeration getInitParameterNames() {
    return servletContext.getInitParameterNames();
  }
}
