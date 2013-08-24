package com.google.ratel.portlet;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class RequestDispatcherImpl implements RequestDispatcher {
  private PortletRequestDispatcher dispatcher;


  public RequestDispatcherImpl(PortletRequestDispatcher portletDispatcher) {
    this.dispatcher = portletDispatcher;
  }


  @Override
  public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    try {
      dispatcher.forward(getPortletRequest(request), getPortletResponse(response));
    } catch (PortletException e) {
      throw new ServletException(e);
    }
  }


  @Override
  public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    try {
      dispatcher.include(getPortletRequest(request), getPortletResponse(response));
    } catch (PortletException e) {
      throw new ServletException(e);
    }
  }


  private PortletRequest getPortletRequest(ServletRequest request) {
    return ((HttpServletRequestImpl) request).getResourceRequest();
  }


  private PortletResponse getPortletResponse(ServletResponse response) {
    return ((HttpServletResponseImpl) response).getResourceResponse();
  }


  public PortletRequestDispatcher getPortletRequestDispatcher() {
    return dispatcher;
  }
}
