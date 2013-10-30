/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.ratel.core;

import java.lang.annotation.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)

public @interface Path {

    String value();

}
