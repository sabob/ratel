package com.google.ratel.deps.jackson.databind.deser.std;

import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.JsonDeserializer;

/**
 * Intermediate base deserializer class that adds more shared accessor
 * so that other classes can access information about contained (value)
 * types
 */
@SuppressWarnings("serial")
public abstract class ContainerDeserializerBase<T>
    extends StdDeserializer<T>
{
    protected ContainerDeserializerBase(Class<?> selfType)
    {
        super(selfType);
    }

    /*
    /**********************************************************
    /* Extended API
    /**********************************************************
     */

    /**
     * Accessor for declared type of contained value elements; either exact
     * type, or one of its supertypes.
     */
    public abstract JavaType getContentType();

    /**
     * Accesor for deserializer use for deserializing content values.
     */
    public abstract JsonDeserializer<Object> getContentDeserializer();
}
