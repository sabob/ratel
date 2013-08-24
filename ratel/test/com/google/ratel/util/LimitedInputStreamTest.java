/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.util;

import com.google.ratel.deps.io.IOUtils;
import java.io.*;
import static java.lang.String.format;
import java.nio.charset.Charset;

/**
 *
 */
public class LimitedInputStreamTest {
    
    public static int DEFAULT_BUFFER_SIZE = 1024;

    public static void main(String args[]) {
        int pSizeMax = 10;

        String data = "12345678900";
        ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes());

        LimitedInputStream limitedStream = new LimitedInputStream(input, pSizeMax) {
            @Override
            protected void raiseError(long pSizeMax, long pCount) throws IOException {

                String msg = format("the request was rejected because its size (%s) exceeds the configured maximum (%s)",
                                    pCount, pSizeMax);

                throw new IllegalArgumentException(msg);

            }
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copyLarge(limitedStream, output, new byte[DEFAULT_BUFFER_SIZE]);
            System.out.println("output:" + output.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }



    }
}
