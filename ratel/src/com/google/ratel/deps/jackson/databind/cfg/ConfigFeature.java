package com.google.ratel.deps.jackson.databind.cfg;

/**
 * Interface that actual SerializationFeature enumerations used by
 * {@link MapperConfig} implementations must implement.
 * Necessary since enums can not be extended using normal
 * inheritance, but can implement interfaces
 */
public interface ConfigFeature
{
    /**
     * Accessor for checking whether this feature is enabled by default.
     */
    public boolean enabledByDefault();
    
    /**
     * Returns bit mask for this feature instance
     */
    public int getMask();
}
