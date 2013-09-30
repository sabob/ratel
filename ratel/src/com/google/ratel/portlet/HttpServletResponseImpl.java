package com.google.ratel.portlet;

import javax.portlet.ResourceResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public class HttpServletResponseImpl implements HttpServletResponse {
  private ResourceResponse response;


  public HttpServletResponseImpl(ResourceResponse response) {
    this.response = response;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., addCookie,...
   */
  @Override
  public void addCookie(Cookie cookie) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The containsHeader method of the HttpServletResponse must return false.
   */
  @Override
  public boolean containsHeader(String name) {
    return false;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., encodeURL, ...
   */
  @Override
  public String encodeURL(String url) {
    return response.encodeURL(url);
  }



  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must return null: ..., encodeRedirectURL, ...
   */
  @Override
  public String encodeRedirectURL(String url) {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., encodeUrl, ...
   */
  @Override
  public String encodeUrl(String url) {
    return response.encodeURL(url);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must return null: ..., encodeRedirectUrl, ...
   */
  @Override
  public String encodeRedirectUrl(String url) {
    return null;
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., sendError,...
   */
  @Override
  public void sendError(int sc, String msg) throws IOException {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., sendError,...
   */
  @Override
  public void sendError(int sc) throws IOException {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., sendRedirect,...
   */

  @Override
  public void sendRedirect(String location) throws IOException {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setDateHeader,...
   */
  @Override
  public void setDateHeader(String name, long date) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., addDateHeader,...
   */
  @Override
  public void addDateHeader(String name, long date) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setHeader,...
   */
  @Override
  public void setHeader(String name, String value) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., addHeader,...
   */
  @Override
  public void addHeader(String name, String value) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setIntHeader,...
   */
  @Override
  public void setIntHeader(String name, int value) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., addIntHeader,...
   */
  @Override
  public void addIntHeader(String name, int value) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setStatus,...
   */
  @Override
  public void setStatus(int sc) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setStatus,...
   */
  @Override
  public void setStatus(int sc, String sm) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., getCharacterEncoding, ...
   */
  @Override
  public String getCharacterEncoding() {
    return response.getCharacterEncoding();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setContentType,...
   */

  @Override
  public String getContentType() {
    return response.getContentType();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., getOutputStream, ...
   */
  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    OutputStream out = response.getPortletOutputStream();
    return (out == null) ? null : new ServletOutputStreamImpl(out);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., getOutputStream, ...
   */
  @Override
  public PrintWriter getWriter() throws IOException {
    return response.getWriter();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setCharacterEncoding,...
   */
  @Override
  public void setCharacterEncoding(String charset) {
      response.setCharacterEncoding(charset);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setContentLength,...
   */
  @Override
  public void setContentLength(int len) {
      response.setContentLength(len);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setContentType,...
   */
  @Override
  public void setContentType(String type) {
      response.setContentType(type);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., setBufferSize, ...
   */
  @Override
  public void setBufferSize(int size) {
    response.setBufferSize(size);
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., getBufferSize, ...
   */
  @Override
  public int getBufferSize() {
    return response.getBufferSize();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., flushBuffer, ...
   */
  @Override
  public void flushBuffer() throws IOException {
    response.flushBuffer();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., resetBuffer, ...
   */
  @Override
  public void resetBuffer() {
    response.resetBuffer();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., isCommitted, ...
   */
  @Override
  public boolean isCommitted() {
    return response.isCommitted();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., reset, ...
   */
  @Override
  public void reset() {
    response.reset();
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must perform no operations: ..., setLocale,...
   */
  @Override
  public void setLocale(Locale loc) {
  }


  /*
   * PORTLETSPEC_20 PLT.19.3.5
   * The following methods of the HttpServletResponse must be equivalent to the methods of the ResourceResponse of
   * similar name: ..., getLocale, ...
   */
  @Override
  public Locale getLocale() {
    return response.getLocale();
  }


  public ResourceResponse getResourceResponse() {
    return response;
  }
}
