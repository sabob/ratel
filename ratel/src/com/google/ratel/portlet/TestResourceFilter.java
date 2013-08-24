package com.google.ratel.portlet;

import java.io.*;
import javax.portlet.*;
import javax.portlet.filter.*;

/**
 *
 */
public class TestResourceFilter implements ResourceFilter, RenderFilter {

    @Override
    public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException, PortletException {
        System.out.println(this.getClass() + " Render doFilter() called");

        chain.doFilter(request, response);
    }

    @Override
    public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain) throws IOException, PortletException {
        System.out.println(this.getClass() + " Resource doFilter() called");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws PortletException {
        System.out.println(this.getClass() + " init() called");
    }

    @Override
    public void destroy() {
        System.out.println(this.getClass() + " destroy() called");
    }
}
