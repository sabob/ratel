package com.google.ratel.portlet;

import java.io.*;
import java.util.Enumeration;
import javax.portlet.*;

/**
 *
 */
public class TestPortlet extends GenericPortlet {
    
    @Override
    public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        System.out.println("Serve Resource() called ResourceId:"  + request.getResourceID());
        Enumeration en = request.getPropertyNames();
        while (en.hasMoreElements()) {
            Object o = en.nextElement();
            System.out.println("Prop : " + o);
        }
        
         en = request.getParameterNames();
        while (en.hasMoreElements()) {
            Object o = en.nextElement();
            System.out.println("Params : " + o);
        }
        PrintWriter writer = response.getWriter();
        writer.write("Test serveResource()!");
        writer.close();
    }

    
    @Override
    protected void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write("Test doEdit()!");
        writer.close();
    }
    
    
    
    public void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        ResourceURL url = response.createResourceURL();
        url.setResourceID("co.za.momentum.moopok");
        url.setParameter("param1", "value1");
        url.setProperty("prop1", "propval1");
        //PrintWriter writer = response.getWriter();
        //writer.write("Test doView()! url: <a href='" + url + "'>" + url + "</a>");
        //writer.close();
        
          PortletRequestDispatcher prd = getPortletContext().getRequestDispatcher("/WEB-INF/jsp/urls.jsp");
          prd.forward(request, response);
        
    }
}
