/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.google.ratel.service.log;

import com.google.ratel.util.HtmlStringBuffer;
import javax.servlet.ServletContext;

/**
 * Provides a Log Service class which will log messages to the console or
 * <tt>System.out</tt>.
 * <p/>
 * ConsoleLogService is the default {@link LogService} for Ratel.
 * <p/>
 * However you can instruct Ratel to use a different log service implementation.
 * Please see {@link LogService} for more details.
 */
public class ConsoleLogService implements LogService {

    // -------------------------------------------------------------- Constants

    /** The trace logging level. */
    public static final int TRACE_LEVEL = -1;

    /** The debug logging level. */
    public static final int DEBUG_LEVEL = 0;

    /** The info logging level. */
    public static final int INFO_LEVEL = 1;

    /** The warn logging level. */
    public static final int WARN_LEVEL = 2;

    /** The error logging level. */
    public static final int ERROR_LEVEL = 3;

    /** The level names. */
    protected static final String[] LEVELS =
    { " [trace] ", " [debug] ", " [info ] ", " [warn ] ", " [error] " };

    // ----------------------------------------------------- Instance Variables

    /** The logging level. */
    protected int logLevel = INFO_LEVEL;

    /** The log name. */
    protected String name = "Ratel";

    // --------------------------------------------------------- Public Methods

    /**
     * @see LogService#onInit(ServletContext)
     *
     * @param servletContext the application servlet context
     * @throws Exception if an error occurs while initializing the LogService
     */
    @Override
    public void onInit(ServletContext servletContext) throws Exception {
    }

    /**
     * @see LogService#onDestroy()
     */
    @Override
    public void onDestroy(ServletContext servletContext) {
    }

    /**
     * Set the logging level
     * <tt>[ TRACE_LEVEL | DEBUG_LEVEL | INFO_LEVEL | WARN_LEVEL | ERROR_LEVEL ]</tt>.
     *
     * @param level the logging level
     */
    public void setLevel(int level) {
        logLevel = level;
    }

    /**
     * @see LogService#debug(Object)
     *
     * @param message the message to log
     */
    @Override
    public void debug(Object message) {
        log(DEBUG_LEVEL, String.valueOf(message), null);
    }

    /**
     * @see LogService#debug(Object, Throwable)
     *
     * @param message the message to log
     * @param error the error to log
     */
    @Override
    public void debug(Object message, Throwable error) {
        log(DEBUG_LEVEL, String.valueOf(message), error);
    }

    /**
     * @see LogService#error(Object)
     *
     * @param message the message to log
     */
    @Override
    public void error(Object message) {
        log(ERROR_LEVEL, String.valueOf(message), null);
    }

    /**
     * @see LogService#error(Object, Throwable)
     *
     * @param message the message to log
     * @param error the error to log
     */
    @Override
    public void error(Object message, Throwable error) {
        log(ERROR_LEVEL, String.valueOf(message), error);
    }

    /**
     * @see LogService#info(Object)
     *
     * @param message the message to log
     */
    @Override
    public void info(Object message) {
        log(INFO_LEVEL, String.valueOf(message), null);
    }

    /**
     * @see LogService#info(Object, Throwable)
     *
     * @param message the message to log
     * @param error the error to log
     */
    @Override
    public void info(Object message, Throwable error) {
        log(INFO_LEVEL, String.valueOf(message), error);
    }

    /**
     * @see LogService#trace(Object)
     *
     * @param message the message to log
     */
    @Override
    public void trace(Object message) {
        log(TRACE_LEVEL, String.valueOf(message), null);
    }

    /**
     * @see LogService#trace(Object, Throwable)
     *
     * @param message the message to log
     * @param error the error to log
     */
    @Override
    public void trace(Object message, Throwable error) {
        log(TRACE_LEVEL, String.valueOf(message), error);
    }

    /**
     * @see LogService#warn(Object)
     *
     * @param message the message to log
     */
    @Override
    public void warn(Object message) {
        log(WARN_LEVEL, String.valueOf(message), null);
    }

    /**
     * @see LogService#warn(Object, Throwable)
     *
     * @param message the message to log
     * @param error the error to log
     */
    @Override
    public void warn(Object message, Throwable error) {
        log(WARN_LEVEL, String.valueOf(message), error);
    }

    /**
     * @see LogService#isDebugEnabled()
     *
     * @return true if [debug] level logging is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return logLevel <= DEBUG_LEVEL;
    }

    /**
     * @see LogService#isInfoEnabled()
     *
     * @return true if [info] level logging is enabled
     */
    @Override
    public boolean isInfoEnabled() {
        return logLevel <= INFO_LEVEL;
    }

    /**
     * @see LogService#isTraceEnabled()
     *
     * @return true if [trace] level logging is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return logLevel <= TRACE_LEVEL;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Log the given message and optional error at the specified logging level.
     *
     * @param level the logging level
     * @param message the message to log
     * @param error the optional error to log
     */
    protected void log(int level, String message, Throwable error) {
        if (level < logLevel) {
            return;
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer();

        buffer.append("[");
        buffer.append(name);
        buffer.append("]");

        buffer.append(LEVELS[level + 1]);
        buffer.append(message);

        if (error != null) {
            buffer.append(" ");
            System.out.print(buffer.toString());
            error.printStackTrace(System.out);
        } else {
            System.out.println(buffer.toString());
        }
    }

}
