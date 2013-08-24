package com.google.ratel.deps.jackson.databind.ser.impl;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.ratel.deps.jackson.core.*;

import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.JsonMappingException;
import com.google.ratel.deps.jackson.databind.JsonNode;
import com.google.ratel.deps.jackson.databind.SerializerProvider;
import com.google.ratel.deps.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.google.ratel.deps.jackson.databind.ser.std.StdSerializer;

/**
 * Special bogus "serializer" that will throw
 * {@link JsonGenerationException} if its {@link #serialize}
 * gets invoked. Most commonly registered as handler for unknown types,
 * as well as for catching unintended usage (like trying to use null
 * as Map/Object key).
 */
public final class FailingSerializer
    extends StdSerializer<Object>
{
    final String _msg;
    
    public FailingSerializer(String msg) {
        super(Object.class);
        _msg = msg;
    }
    
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        throw new JsonGenerationException(_msg);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
        throws JsonMappingException
    {
        return null;
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
    {
        ;
    }
}
