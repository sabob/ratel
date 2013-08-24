package com.google.ratel.portlet;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServletInputStreamImpl extends ServletInputStream {
  private InputStream wrapped;
  
  public ServletInputStreamImpl(InputStream wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public int read() throws IOException {
    return wrapped.read();
  }

  @Override
  public int read(byte[] b) throws IOException {
    return wrapped.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return wrapped.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    return wrapped.skip(n);
  }

  @Override
  public int available() throws IOException {
    return wrapped.available();
  }

  @Override
  public void close() throws IOException {
    wrapped.close();
  }

  @Override
  public void mark(int readLimit) {
    wrapped.mark(readLimit);
  }

  @Override
  public void reset() throws IOException {
    wrapped.reset();
  }

  @Override
  public boolean markSupported() {
    return wrapped.markSupported();
  }
}
