package com.google.ratel.deps.jackson.databind.introspect;

public interface WithMember<T>
{
    public T withMember(AnnotatedMember member);
}
