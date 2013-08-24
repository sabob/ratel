package com.google.ratel.util;

import com.google.ratel.deps.lang3.builder.ReflectionToStringBuilder;
import com.google.ratel.deps.lang3.builder.ToStringBuilder;

public class Organisation {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }
}
