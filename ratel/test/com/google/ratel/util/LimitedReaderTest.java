/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.util;

import com.google.ratel.deps.io.IOUtils;
import java.io.*;
import static java.lang.String.format;
import java.util.logging.*;

/**
 *
 */
public class LimitedReaderTest {
    
    public static int DEFAULT_BUFFER_SIZE = 1024;

    public static void main(String args[]) {
        int pSizeMax = 20;

        Reader reader = new StringReader("12345678900");

        LimitedReader limitedReader = new LimitedReader(reader, pSizeMax) {
            @Override
            protected void raiseError(long pSizeMax, long pCount) throws IOException {

                String msg = format("the request was rejected because its size (%s) exceeds the configured maximum (%s)",
                                    pCount, pSizeMax);

                throw new IllegalArgumentException(msg);

            }
        };

        StringWriter writer = new StringWriter();
        try {
            IOUtils.copyLarge(limitedReader, writer, new char[DEFAULT_BUFFER_SIZE]);
            System.out.println("output:" + writer.toString());
        } catch (IOException ex) {
            ex.printStackTrace();;
        }



    }
}
