package com.google.ratel.deps.jackson.databind.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.google.ratel.deps.jackson.core.*;
import com.google.ratel.deps.jackson.core.io.NumberOutput;
import com.google.ratel.deps.jackson.databind.SerializerProvider;


/**
 * Numeric node that contains simple 16-bit integer values.
 */
public final class ShortNode
    extends NumericNode
{
    final short _value;

    /* 
    ************************************************
    * Construction
    ************************************************
    */

    public ShortNode(short v) { _value = v; }

    public static ShortNode valueOf(short l) { return new ShortNode(l); }

    /* 
    ************************************************
    * Overridden JsonNode methods
    ************************************************
    */

    @Override public JsonToken asToken() { return JsonToken.VALUE_NUMBER_INT; }

    @Override
    public JsonParser.NumberType numberType() { return JsonParser.NumberType.INT; }			// should be SHORT


    @Override
    public boolean isIntegralNumber() { return true; }

    @Override
    public boolean isShort() { return true; }

    @Override public boolean canConvertToInt() { return true; }
    @Override public boolean canConvertToLong() { return true; }
    
    @Override
    public Number numberValue() {
        return Short.valueOf(_value);
    }

    @Override
    public short shortValue() { return _value; }

    @Override
    public int intValue() { return _value; }

    @Override
    public long longValue() { return _value; }

    @Override
    public float floatValue() { return _value; }

    @Override
    public double doubleValue() { return _value; }

    @Override
    public BigDecimal decimalValue() { return BigDecimal.valueOf(_value); }

    @Override
    public BigInteger bigIntegerValue() { return BigInteger.valueOf(_value); }

    @Override
    public String asText() {
        return NumberOutput.toString(_value);
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return _value != 0;
    }
    
    @Override
    public final void serialize(JsonGenerator jg, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        jg.writeNumber(_value);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) { // final class, can do this
            return false;
        }
        return ((ShortNode) o)._value == _value;
    }

    @Override
    public int hashCode() {
        return _value;
    }
}
