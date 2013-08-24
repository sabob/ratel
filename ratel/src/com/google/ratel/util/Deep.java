/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.ratel.deps.lang3.builder.ReflectionToStringBuilder;

/**
 *
 */
public class Deep {

    int i;
    
    long l;
    
    boolean b;
    
    double d;

    Deep parent;
    
    Person person1;

    Person person;
    Person[] persons;
    
    int[] ii;
    
    Integer I;
    
    Long L;
    
    Double D;
    
    Map map;
    
    List list;

    Set set;
    
    
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
