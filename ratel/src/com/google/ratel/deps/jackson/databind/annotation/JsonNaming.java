package com.google.ratel.deps.jackson.databind.annotation;

import java.lang.annotation.*;

import com.google.ratel.deps.jackson.databind.PropertyNamingStrategy;

/**
 * Annotation that can be used to indicate a {@link PropertyNamingStrategy}
 * to use for annotated class. Overrides the global (default) strategy.
 * 
 * @since 2.1
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@com.google.ratel.deps.jackson.annotation.JacksonAnnotation
public @interface JsonNaming
{
    public Class<? extends PropertyNamingStrategy> value();
}
