package com.google.ratel.deps.jackson.databind.util;

import com.google.ratel.deps.jackson.core.io.SerializedString;

import com.google.ratel.deps.jackson.databind.*;
import com.google.ratel.deps.jackson.databind.cfg.MapperConfig;
import com.google.ratel.deps.jackson.databind.introspect.AnnotatedClass;
import com.google.ratel.deps.jackson.databind.type.ClassKey;

/**
 * Helper class for caching resolved root names.
 */
public class RootNameLookup
    implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * For efficient operation, let's try to minimize number of times we
     * need to introspect root element name to use.
     */
    protected LRUMap<ClassKey,SerializedString> _rootNames;

    public RootNameLookup() { }

    public SerializedString findRootName(JavaType rootType, MapperConfig<?> config)
    {
        return findRootName(rootType.getRawClass(), config);
    }

    public synchronized SerializedString findRootName(Class<?> rootType, MapperConfig<?> config)
    {
        ClassKey key = new ClassKey(rootType);

        if (_rootNames == null) {
            _rootNames = new LRUMap<ClassKey,SerializedString>(20, 200);
        } else {
            SerializedString name = _rootNames.get(key);
            if (name != null) {
                return name;
            }
        }
        BeanDescription beanDesc = config.introspectClassAnnotations(rootType);
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        AnnotatedClass ac = beanDesc.getClassInfo();
        PropertyName pname = intr.findRootName(ac);
        String nameStr;
        // No answer so far? Let's just default to using simple class name
        if (pname == null || !pname.hasSimpleName()) {
            // Should we strip out enclosing class tho? For now, nope:
            nameStr = rootType.getSimpleName();
        } else {
            nameStr = pname.getSimpleName();
        }
        SerializedString name = new SerializedString(nameStr);
        _rootNames.put(key, name);
        return name;
    }
}
