package com.google.ratel.deps.jackson.databind.jsonFormatVisitors;

import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.JsonMappingException;
import com.google.ratel.deps.jackson.databind.SerializerProvider;

public interface JsonMapFormatVisitor extends JsonFormatVisitorWithSerializerProvider
{
    /**
     * Visit method called to indicate type of keys of the Map type
     * being visited
     */
    public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException;

    /**
     * Visit method called after {@link #keyFormat} to allow visiting of
     * the value type
     */
    public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException;
    
    /**
     * Default "empty" implementation, useful as the base to start on;
     * especially as it is guaranteed to implement all the method
     * of the interface, even if new methods are getting added.
     */
    public static class Base
        implements JsonMapFormatVisitor
    {
        protected SerializerProvider _provider;

        public Base() { }
        public Base(SerializerProvider p) { _provider = p; }

        @Override
        public SerializerProvider getProvider() { return _provider; }

        @Override
        public void setProvider(SerializerProvider p) { _provider = p; }

        @Override
        public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException { }
        @Override
        public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException { }
    }
}
