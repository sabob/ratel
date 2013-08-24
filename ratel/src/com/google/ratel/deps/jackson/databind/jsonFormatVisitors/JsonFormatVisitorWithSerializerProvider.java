/**
 * 
 */
package com.google.ratel.deps.jackson.databind.jsonFormatVisitors;

import com.google.ratel.deps.jackson.databind.SerializerProvider;

/**
 * @author jphelan
 */
public interface JsonFormatVisitorWithSerializerProvider {
    public SerializerProvider getProvider();
    public abstract void setProvider(SerializerProvider provider);
}
