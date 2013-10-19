package com.google.ratel.deps.jackson.databind.jsonFormatVisitors;

import com.google.ratel.deps.jackson.databind.JavaType;
import com.google.ratel.deps.jackson.databind.JsonMappingException;

/**
 * Interface {@link com.google.ratel.deps.jackson.databind.JsonSerializer} implements
 * to allow for visiting type hierarchy.
 */
public interface JsonFormatVisitable
{
    /**
     * Get the representation of the schema to which this serializer will conform.
     * @param typeHint Type of element (entity like property) being visited
     *
     * @returns <a href="http://json-schema.org/">Json-schema</a> for this serializer.
     */
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException;
}