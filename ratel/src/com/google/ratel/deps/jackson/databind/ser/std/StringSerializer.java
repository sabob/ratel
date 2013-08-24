package com.google.ratel.deps.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.ratel.deps.jackson.core.*;

import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.JsonMappingException;
import com.google.ratel.deps.jackson.databind.JsonNode;
import com.google.ratel.deps.jackson.databind.SerializerProvider;
import com.google.ratel.deps.jackson.databind.annotation.JacksonStdImpl;
import com.google.ratel.deps.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

/**
 * This is the special serializer for regular {@link java.lang.String}s.
 *<p>
 * Since this is one of "native" types, no type information is ever
 * included on serialization (unlike for most scalar types as of 1.5)
 */
@JacksonStdImpl
public final class StringSerializer
    extends NonTypedScalarSerializerBase<String>
{
    public StringSerializer() { super(String.class); }

    /**
     * For Strings, both null and Empty String qualify for emptiness.
     */
    @Override
    public boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }
    
    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        return createSchemaNode("string", true);
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
            throws JsonMappingException
    {
        if (visitor != null) visitor.expectStringFormat(typeHint);
    }
}
