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

/**
 * Compared to regular {@link java.util.Date} serialization, we do use String
 * representation here. Why? Basically to truncate of time part, since
 * that should not be used by plain SQL date.
 */
@JacksonStdImpl
public class SqlDateSerializer
    extends StdScalarSerializer<java.sql.Date>
{
    public SqlDateSerializer() { super(java.sql.Date.class); }

    @Override
    public void serialize(java.sql.Date value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException
    {
        jgen.writeString(value.toString());
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        //todo: (ryan) add a format for the date in the schema?
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