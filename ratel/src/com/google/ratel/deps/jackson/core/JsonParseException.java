/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 */

package com.google.ratel.deps.jackson.core;

/**
 * Exception type for parsing problems, used when non-well-formed content
 * (content that does not conform to JSON syntax as per specification)
 * is encountered.
 */
public class JsonParseException
    extends JsonProcessingException
{
    final static long serialVersionUID = 123; // Stupid eclipse...

    public JsonParseException(String msg, JsonLocation loc)
    {
        super(msg, loc);
    }

    public JsonParseException(String msg, JsonLocation loc, Throwable root)
    {
        super(msg, loc, root);
    }
}
