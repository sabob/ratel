package com.google.ratel.deps.jackson.databind.deser.std;

import java.io.IOException;

import com.google.ratel.deps.jackson.core.*;

import com.google.ratel.deps.jackson.databind.DeserializationContext;
import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.jsontype.TypeDeserializer;

/**
 * Base class for deserializers that handle types that are serialized
 * as JSON scalars (non-structured, i.e. non-Object, non-Array, values).
 */
public abstract class StdScalarDeserializer<T> extends StdDeserializer<T>
{
    private static final long serialVersionUID = 1L;

    protected StdScalarDeserializer(Class<?> vc) {
        super(vc);
    }

    protected StdScalarDeserializer(JavaType valueType) {
        super(valueType);
    }
    
    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
            TypeDeserializer typeDeserializer)
        throws IOException, JsonProcessingException
    {
        return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
    }
}
