package com.google.ratel.deps.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.ratel.deps.jackson.core.JsonGenerationException;
import com.google.ratel.deps.jackson.core.JsonGenerator;
import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.JsonMappingException;
import com.google.ratel.deps.jackson.databind.JsonNode;
import com.google.ratel.deps.jackson.databind.SerializerProvider;
import com.google.ratel.deps.jackson.databind.annotation.JacksonStdImpl;
import com.google.ratel.deps.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

/**
 * Serializer used for primitive boolean, as well as java.util.Boolean
 * wrapper type.
 *<p>
 * Since this is one of "native" types, no type information is ever
 * included on serialization (unlike for most scalar types as of 1.5)
 */
@JacksonStdImpl
public final class BooleanSerializer
    extends NonTypedScalarSerializerBase<Boolean>
{
    /**
     * Whether type serialized is primitive (boolean) or wrapper
     * (java.lang.Boolean); if true, former, if false, latter.
     */
    final boolean _forPrimitive;

    public BooleanSerializer(boolean forPrimitive)
    {
        super(Boolean.class);
        _forPrimitive = forPrimitive;
    }

    @Override
    public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeBoolean(value.booleanValue());
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        return createSchemaNode("boolean", !_forPrimitive);
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException
    {
        if (visitor != null) {
            visitor.expectBooleanFormat(typeHint);
        }
    }
}