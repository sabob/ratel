package com.google.ratel.deps.jackson.databind.annotation;

import java.lang.annotation.*;

import com.google.ratel.deps.jackson.databind.jsontype.TypeResolverBuilder;

/**
 * Annotation that can be used to explicitly define custom resolver
 * used for handling serialization and deserialization of type information,
 * needed for handling of polymorphic types (or sometimes just for linking
 * abstract types to concrete types)
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@com.google.ratel.deps.jackson.annotation.JacksonAnnotation
public @interface JsonTypeResolver
{
    /**
     * Defines implementation class of {@link TypeResolverBuilder} which is used to construct
     * actual {@link com.google.ratel.deps.jackson.databind.jsontype.TypeDeserializer} and {@link com.google.ratel.deps.jackson.databind.jsontype.TypeDeserializer}
     * instances that handle reading and writing addition type information needed to support polymorphic
     * deserialization.
     */
    public Class<? extends TypeResolverBuilder<?>> value();
}
