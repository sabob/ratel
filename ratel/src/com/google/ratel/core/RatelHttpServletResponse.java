package com.google.ratel.core;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 */
public class RatelHttpServletResponse extends HttpServletResponseWrapper {

    public static final int DEFAULT_STATUS = 0;
    
    private int httpStatus = DEFAULT_STATUS;
    
    private boolean dataWritten = false;

    public RatelHttpServletResponse(HttpServletResponse response) {
        super(response);
    }
    
    @Override
public void sendRedirect(String location) throws IOException {
    httpStatus = SC_MOVED_TEMPORARILY;
    super.sendRedirect(location);
}

    @Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        httpStatus = sc;
        super.sendError(sc, msg);
    }
    
    @Override
public void reset() {
    super.reset();
    this.httpStatus = DEFAULT_STATUS;
}

    @Override
    public void setStatus(int sc) {
        httpStatus = sc;
        super.setStatus(sc);
    }

    public int getStatus() {
        return httpStatus;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        dataWritten = true;
        return super.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        dataWritten = true;
        return super.getWriter();
    }

    /**
     * @return the dataWritten
     */
    public boolean isDataWritten() {
        return dataWritten;
    }
    
    public boolean isStatusSettable() {
        if (httpStatus == DEFAULT_STATUS && !dataWritten) {
            return true;
        } else {
            return false;
        }
    }
}
