package com.google.ratel.portlet;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServletOutputStreamImpl extends ServletOutputStream {

  private OutputStream out;


  public ServletOutputStreamImpl(OutputStream out) {
    this.out = out;
  }


  @Override
  public void write(int b) throws IOException {
    out.write(b);
  }


  public OutputStream getOutputStream() {
    return out;
  }
}
