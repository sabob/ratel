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
package com.google.ratel.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a HTML element StringBuffer for rendering, automatically escaping string values. HtmlStringBuffer is used by Click controls for
 * HTML rendering.
 * <p/>
 * For example the following code:
 * <pre class="codeJava">
 * <span class="kw">public</span> String toString() { HtmlStringBuffer buffer = <span class="kw">new</span> HtmlStringBuffer();
 *
 * buffer.elementStart(<span class="st">"input"</span>); buffer.appendAttribute(<span class="st">"type"</span>, <span
 * class="st">"text"</span>); buffer.appendAttribute(<span class="st">"name"</span>, getName()); buffer.appendAttribute(<span
 * class="st">"value"</span>, getValue()); buffer.elementEnd();
 *
 * <span class="kw">return</span> buffer.toString(); } </pre>
 *
 * Would render:
 *
 * <pre class="codeHtml">
 * &lt;input type="text" name="address" value="23 Holt's Street"/&gt; </pre>
 *
 * <h4>Synchronization</h4>
 *
 * To improve performance in Click's thread safe environment this class does not synchronize append operations. Internally this class uses a
 * character buffer adapted from the JDK 1.5 <tt>AbstractStringBuilder</tt>.
 */
public class JsonStringBuffer {

    private final static Map<Class<?>, Object> unquotedValues = new HashMap<Class<?>, Object>();

    static {
        unquotedValues.put(int.class, 0);
        unquotedValues.put(Integer.class, 0);
        unquotedValues.put(float.class, 0F);
        unquotedValues.put(Float.class, 0F);
        unquotedValues.put(long.class, 0L);
        unquotedValues.put(Long.class, 0L);
        unquotedValues.put(double.class, 0D);
        unquotedValues.put(Double.class, 0D);
        unquotedValues.put(short.class, 0);
        unquotedValues.put(Short.class, 0);
        unquotedValues.put(byte.class, 0);
        unquotedValues.put(Byte.class, 0);
        unquotedValues.put(boolean.class, false);
        unquotedValues.put(Boolean.class, Boolean.FALSE);
    }
    // ----------------------------------------------------- Instance Variables
    /**
     * The character storage array.
     */
    protected char[] characters;

    /**
     * The count is the number of characters used.
     */
    protected int count;

    // ----------------------------------------------------------- Constructors
    /**
     * Create a new HTML StringBuffer with the specified initial capacity.
     *
     * @param length the initial capacity
     */
    public JsonStringBuffer(int length) {
        characters = new char[length];
    }

    /**
     * Create a new HTML StringBuffer with an initial capacity of 128 characters.
     */
    public JsonStringBuffer() {
        characters = new char[128];
    }

    // --------------------------------------------------------- Public Methods
    public JsonStringBuffer objectBegin() {
        append("{");
        return this;
    }

    public JsonStringBuffer objectEnd() {
        append("}");
        return this;
    }

    /**
     * Append the double value to the buffer.
     *
     * @param value the double value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer append(String key, Object value) {
        append("\"");
        append(key);
        append("\":");
        append("\"");
        append(value);
        append("\"");
        return this;
    }

    public JsonStringBuffer json(String key, String value) {
        append("\"");
        append(key);
        append("\":");
        append(value);
        return this;
    }

    public JsonStringBuffer json(String value) {
        append(value);
        append(value);
        return this;
    }

    public JsonStringBuffer sep() {
        append(",");
        return this;
    }
    
    public JsonStringBuffer comma() {
        append(",");
        return this;
    }

    public JsonStringBuffer key(String key) {
        append("\"");
        append(key);
        append("\":");
        return this;
    }

    public JsonStringBuffer val(Object value) {
        append("\"");
        append(value);
        append("\"");
        return this;
    }

    public JsonStringBuffer arrayBegin() {
        append("[");
        return this;
    }

    public JsonStringBuffer arrayEnd() {
        append("]");
        return this;
    }

    /**
     * Append the char value to the buffer.
     *
     * @param value the char value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer append(String key, char value) {
        append("\"");
        append(key);
        append("\":");
        append("\"");
        append(value);
        append("\"");
        return this;
    }

    /**
     * Append the integer value to the buffer.
     *
     * @param value the integer value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(char value) {
        append("\"");
        append(value);
        append("\"");
        return this;
    }

    public JsonStringBuffer append(String key, int value) {
        append("\"");
        append(key);
        append("\":");
        append(String.valueOf(value));
        return this;
    }

    public JsonStringBuffer val(int value) {
        append(String.valueOf(value));

        return this;
    }

    public JsonStringBuffer append(String key, long value) {
        append("\"");
        append(key);
        append("\":");
        append(String.valueOf(value));
        return this;
    }

    /**
     * Append the long value to the buffer.
     *
     * @param value the long value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(long value) {
        append(String.valueOf(value));

        return this;
    }

    public JsonStringBuffer append(String key, byte value) {
        append("\"");
        append(key);
        append("\":");
        append(value);
        return this;
    }

    /**
     * Append the long value to the buffer.
     *
     * @param value the long value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(byte value) {
        append(value);

        return this;
    }

    public JsonStringBuffer append(String key, short value) {
        append("\"");
        append(key);
        append("\":");
        append(value);
        return this;
    }

    /**
     * Append the integer value to the buffer.
     *
     * @param value the integer value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(short value) {
        append(value);
        return this;
    }

    public JsonStringBuffer append(String key, float value) {
        append("\"");
        append(key);
        append("\":");
        append(value);
        return this;
    }

    /**
     * Append the integer value to the buffer.
     *
     * @param value the integer value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(float value) {
        append(value);
        return this;
    }

    public JsonStringBuffer append(String key, double value) {
        append("\"");
        append(key);
        append("\":");
        append(value);
        return this;
    }

    /**
     * Append the integer value to the buffer.
     *
     * @param value the integer value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(double value) {
        append(value);
        return this;
    }

    public JsonStringBuffer append(String key, boolean value) {
        append("\"");
        append(key);
        append("\":");
        append(value);
        return this;
    }

    /**
     * Append the integer value to the buffer.
     *
     * @param value the integer value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer val(boolean value) {
        append(value);
        return this;
    }

    /**
     * Append the raw object value of the given object to the buffer.
     *
     * @param value the object value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer append(Object value) {
        String string = String.valueOf(value);
        int length = string.length();

        int newCount = count + length;
        if (newCount > characters.length) {
            expandCapacity(newCount);
        }
        //append("\"");
        string.getChars(0, length, characters, count);
        //append("\"");
        count = newCount;

        return this;
    }

    /**
     * Append the raw string value of the given object to the buffer.
     *
     * @param value the string value to append
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     */
    public JsonStringBuffer append(String value) {
        String string = (value != null) ? value : "null";
        int length = string.length();

        int newCount = count + length;
        if (newCount > characters.length) {
            expandCapacity(newCount);
        }
        string.getChars(0, length, characters, count);
        count = newCount;

        return this;
    }

    /**
     * Append the given map of CSS style name and value pairs as a style attribute to the string buffer.
     *
     * @param attributes the map of CSS style names and values
     * @return a reference to this <tt>HtmlStringBuffer</tt> object
     * @throws IllegalArgumentException if attributes is null
     */
    public JsonStringBuffer map(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("Null attributes parameter");
        }

        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object obj = entry.getValue();

                if (obj.getClass().isArray()) {
                    key(entry.getKey());
                    String[] ar = (String[]) obj;
                    array((Object[]) ar);
                } else {
                    append(entry.getKey(), entry.getValue());
                }
            }
        }

        return this;
    }

    public JsonStringBuffer keyArray(String key, Object... array) {
        if (key == null) {
            throw new IllegalArgumentException("Null key parameter");
        }
        key(key);
        array(array);
        return this;
    }

    public JsonStringBuffer array(Object... array) {
        if (array == null) {
            throw new IllegalArgumentException("Null array parameter");
        }

        append("[");

        for (int i = 0; i < array.length; i++) {
            Object o = array[i];
            if (o == null) {
                append(o);
            } else {
                if (unquotedValues.containsKey(o.getClass())) {
                    append(o);
                } else {
                    val(o);
                }

            }


            if (i < array.length - 1) {
                sep();
            }
        }

        append("]");


        return this;
    }

    /**
     * Return the length of the string buffer.
     *
     * @return the length of the string buffer
     */
    public int length() {
        return count;
    }

    /**
     * @see Object#toString()
     *
     * @return a string representation of the string buffer
     */
    @Override
    public String toString() {
        return new String(characters, 0, count);
    }

    // ------------------------------------------------------ Protected Methods
    /**
     * Ensures that the capacity is at least equal to the specified minimum. If the current capacity is less than the argument, then a new
     * internal array is allocated with greater capacity. The new capacity is the larger of:
     * <ul>
     * <li>The
     * <code>minimumCapacity</code> argument.
     * <li>Twice the old capacity, plus
     * <code>2</code>.
     * </ul>
     * If the
     * <code>minimumCapacity</code> argument is non-positive, this method takes no action and simply returns.
     *
     * @param minimumCapacity the minimum desired capacity
     */
    protected void expandCapacity(int minimumCapacity) {
        int newCapacity = (characters.length + 1) * 2;

        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        } else if (minimumCapacity > newCapacity) {
            newCapacity = minimumCapacity;
        }

        char newValue[] = new char[newCapacity];
        System.arraycopy(characters, 0, newValue, 0, count);
        characters = newValue;
    }

    public static void main(String[] args) {
        JsonStringBuffer buf = new JsonStringBuffer();
        buf.objectBegin();
        buf.key("one");
        buf.append("{\"o\":\"jk\"}").sep();
        buf.json("one", "{\"o\":\"jk\"}").sep();

        Map map = new HashMap();
        map.put("m", "4");
        buf.map(map).sep();
        buf.keyArray("k", "1", null, true, 3).sep();
        buf.key("int");
        buf.val(1).sep();
        buf.key("bool");
        buf.val(true).sep();
        buf.key("double");
        buf.val(1d);
        buf.objectEnd();
        System.out.println(buf);
    }
}
