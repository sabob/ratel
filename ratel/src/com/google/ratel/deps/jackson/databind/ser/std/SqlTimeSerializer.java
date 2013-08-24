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
import com.google.ratel.deps.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.google.ratel.deps.jackson.databind.jsonFormatVisitors.JsonValueFormat;

@JacksonStdImpl
public class SqlTimeSerializer
    extends StdScalarSerializer<java.sql.Time>
{
    public SqlTimeSerializer() { super(java.sql.Time.class); }

    @Override
    public void serialize(java.sql.Time value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value.toString());
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
        JsonStringFormatVisitor v2 = (visitor == null) ? null : visitor.expectStringFormat(typeHint);
        if (v2 != null) {
            v2.format(JsonValueFormat.DATE_TIME);
        }
    }
}