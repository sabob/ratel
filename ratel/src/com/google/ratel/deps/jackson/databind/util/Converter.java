package com.google.ratel.deps.jackson.databind.util;

import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.type.TypeFactory;

/**
 * Helper interface for things that convert Objects of
 * one type to another.
 *<p>
 * NOTE: implementors are strongly encouraged to extend {@link StdConverter}
 * instead of directly implementing {@link Converter}, since that can
 * help with default implementation of typically boiler-plate code.
 *
 * @param <IN> Type of values converter takes
 * @param <OUT> Result type from conversion
 * 
 * @see com.google.ratel.deps.jackson.databind.ser.std.StdDelegatingSerializer
 * 
 * @since 2.1
 */
public interface Converter<IN,OUT>
{
    /**
     * Main conversion method.
     */
    public OUT convert(IN value);

    /**
     * Method that can be used to find out actual input (source) type; this
     * usually can be determined from type parameters, but may need
     * to be implemented differently from programmatically defined
     * converters (which can not change static type parameter bindings).
     * 
     * @since 2.2
     */
    public JavaType getInputType(TypeFactory typeFactory);

    /**
     * Method that can be used to find out actual output (target) type; this
     * usually can be determined from type parameters, but may need
     * to be implemented differently from programmatically defined
     * converters (which can not change static type parameter bindings).
     * 
     * @since 2.2
     */
    public JavaType getOutputType(TypeFactory typeFactory);
    
    /*
    /**********************************************************
    /* Helper class(es)
    /**********************************************************
     */

    /**
     * This marker class is only to be used with annotations, to
     * indicate that <b>no converter is to be used</b>.
     *<p>
     * Specifically, this class is to be used as the marker for
     * annotation {@link com.google.ratel.deps.jackson.databind.annotation.JsonSerialize},
     * property <code>converter</code> (and related)
     * 
     * @since 2.2
     */
    public abstract static class None
        implements Converter<Object,Object> { }
}
