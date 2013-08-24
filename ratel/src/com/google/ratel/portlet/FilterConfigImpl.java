package com.google.ratel.portlet;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import javax.portlet.*;
import javax.servlet.FilterConfig;

public class FilterConfigImpl implements FilterConfig {

  private final PortletConfig portletConfig;
  
  private final ServletContext servletContext;
  
  private String filterName;

  public FilterConfigImpl(String filterName, PortletConfig portletConfig, ServletContext servletContext) {
    this.portletConfig = portletConfig;
    this.servletContext = servletContext;
    this.filterName = filterName;
  }

    @Override
    public String getFilterName() {
        return filterName;
    }

  @Override
  public ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public String getInitParameter(String name) {
    return portletConfig.getInitParameter(name);
  }


  @Override
  public Enumeration getInitParameterNames() {
    return portletConfig.getInitParameterNames();
  }
}
