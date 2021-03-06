package com.google.ratel.deps.jackson.databind.deser.std;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import com.google.ratel.deps.jackson.databind.*;
import com.google.ratel.deps.jackson.databind.deser.KeyDeserializers;
import com.google.ratel.deps.jackson.databind.introspect.AnnotatedMethod;
import com.google.ratel.deps.jackson.databind.util.ClassUtil;
import com.google.ratel.deps.jackson.databind.util.EnumResolver;

/**
 * Helper class used to contain simple/well-known key deserializers.
 * Following kinds of Objects can be handled currently:
 *<ul>
 * <li>Primitive wrappers (Boolean, Byte, Char, Short, Integer, Float, Long, Double)</li>
 * <li>Enums (usually not needed, since EnumMap doesn't call us)</li>
 * <li>{@link java.util.Date}</li>
 * <li>{@link java.util.Calendar}</li>
 * <li>{@link java.util.UUID}</li>
 * <li>{@link java.util.Locale}</li>
 * <li>Anything with constructor that takes a single String arg
 *   (if not explicitly @JsonIgnore'd)</li>
 * <li>Anything with {@code static T valueOf(String)} factory method
 *   (if not explicitly @JsonIgnore'd)</li>
 *</ul>
 */
public class StdKeyDeserializers
    implements KeyDeserializers, java.io.Serializable
{
    private static final long serialVersionUID = 923268084968181479L;

    /**
     * @deprecated Since 2.2, just call <code>StdKeyDeserializer.StringKD</code> directly
     */
    @Deprecated
    public static KeyDeserializer constructStringKeyDeserializer(DeserializationConfig config,
            JavaType type) {
        return StdKeyDeserializer.StringKD.forType(type.getRawClass());
    }
    
    public static KeyDeserializer constructEnumKeyDeserializer(EnumResolver<?> enumResolver) {
        return new StdKeyDeserializer.EnumKD(enumResolver, null);
    }

    public static KeyDeserializer constructEnumKeyDeserializer(EnumResolver<?> enumResolver,
            AnnotatedMethod factory) {
        return new StdKeyDeserializer.EnumKD(enumResolver, factory);
    }
    
    public static KeyDeserializer constructDelegatingKeyDeserializer(DeserializationConfig config,
            JavaType type, JsonDeserializer<?> deser)
    {
        return new StdKeyDeserializer.DelegatingKD(type.getRawClass(), deser);
    }
    
    public static KeyDeserializer findStringBasedKeyDeserializer(DeserializationConfig config,
            JavaType type)
    {
        /* We don't need full deserialization information, just need to
         * know creators.
         */
        BeanDescription beanDesc = config.introspect(type);
        // Ok, so: can we find T(String) constructor?
        Constructor<?> ctor = beanDesc.findSingleArgConstructor(String.class);
        if (ctor != null) {
            if (config.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(ctor);
            }
            return new StdKeyDeserializer.StringCtorKeyDeserializer(ctor);
        }
        /* or if not, "static T valueOf(String)" (or equivalent marked
         * with @JsonCreator annotation?)
         */
        Method m = beanDesc.findFactoryMethod(String.class);
        if (m != null){
            if (config.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(m);
            }
            return new StdKeyDeserializer.StringFactoryKeyDeserializer(m);
        }
        // nope, no such luck...
        return null;
    }
    
    /*
    /**********************************************************
    /* KeyDeserializers implementation
    /**********************************************************
     */
    
    @Override
    public KeyDeserializer findKeyDeserializer(JavaType type,
            DeserializationConfig config, BeanDescription beanDesc)
        throws JsonMappingException
    {
        Class<?> raw = type.getRawClass();
        // First, common types; String/Object/UUID, Int/Long, Dates
        if (raw == String.class || raw == Object.class) {
            return StdKeyDeserializer.StringKD.forType(raw);
        }
        if (raw == UUID.class) {
            return new StdKeyDeserializer.UuidKD();
        }
        
        // 23-Apr-2013, tatu: Map primitive types, just in case one was given
        if (raw.isPrimitive()) {
            raw = ClassUtil.wrapperType(raw);
        }
        
        if (raw == Integer.class) {
            return new StdKeyDeserializer.IntKD();
        }
        if (raw == Long.class) {
            return new StdKeyDeserializer.LongKD();
        }
        if (raw == Date.class) {
            return new StdKeyDeserializer.DateKD();
        }
        if (raw == Calendar.class) {
            return new StdKeyDeserializer.CalendarKD();
        }
        
        // then less common ones...
        if (raw == Boolean.class) {
            return new StdKeyDeserializer.BoolKD();
        }
        if (raw == Byte.class) {
            return new StdKeyDeserializer.ByteKD();
        }
        if (raw == Character.class) {
            return new StdKeyDeserializer.CharKD();
        }
        if (raw == Short.class) {
            return new StdKeyDeserializer.ShortKD();
        }
        if (raw == Float.class) {
            return new StdKeyDeserializer.FloatKD();
        }
        if (raw == Double.class) {
            return new StdKeyDeserializer.DoubleKD();
        }
        if (raw == Locale.class) {
            return new StdKeyDeserializer.LocaleKD();
        }
        return null;
    }
}
