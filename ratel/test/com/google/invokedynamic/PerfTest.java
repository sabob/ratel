/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.invokedynamic;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 *
 */
public class PerfTest {

    int count = 1000000;

    int warmup = 5;

    Lookup lookup = MethodHandles.lookup();

    public void testNative() {
        try {


            for (int i = 0; i < warmup; i++) {
                Pojo pojo = new Pojo();
                pojo.moo();
            }

            long start = System.currentTimeMillis();

            Pojo pojo = null;
                pojo = new Pojo();
            for (int i = 0; i < count; i++) {
                pojo.moo();
            }

            long time = (System.currentTimeMillis() - start);
            System.out.println("testNative time: " + time + ", count: " + pojo);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    public void testUnreflect() {
        try {
            MethodType type = MethodType.methodType(void.class);
            Method rmethod = Pojo.class.getMethod("moo");
            MethodHandle method = lookup.unreflect(rmethod);
            
            Constructor rctor = Pojo.class.getConstructor();
            MethodHandle ctor = lookup.unreflectConstructor(rctor);

            for (int i = 0; i < warmup; i++) {
                Object o = ctor.invoke();
                method.invokeWithArguments(o);

            }

            Object pojo = null;
            long start = System.currentTimeMillis();

            Object o = ctor.invoke();
            for (int i = 0; i < count; i++) {
                method.invokeWithArguments(o);
            }

            long time = (System.currentTimeMillis() - start);
            System.out.println("testUnreflect time: " + time + ", count: " + o);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void testDynamic() {
        try {
            MethodType type = MethodType.methodType(void.class);
            MethodHandle pojoCtor = lookup.findConstructor(Pojo.class, type);
            MethodHandle method = lookup.findVirtual(Pojo.class, "moo", type);

            for (int i = 0; i < warmup; i++) {
                Object o = pojoCtor.invoke();
                method.invoke(o);

            }

            Object pojo = null;
            long start = System.currentTimeMillis();

            pojo = pojoCtor.invokeWithArguments();
            for (int i = 0; i < count; i++) {
                method.invokeWithArguments(pojo);
            }

            long time = (System.currentTimeMillis() - start);
            System.out.println("testDynamic time: " + time + ", count: " + pojo);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public void testReflection() {
        try {
            Method method = Pojo.class.getMethod("moo");


            for (int i = 0; i < warmup; i++) {
                Pojo o = Pojo.class.newInstance();
                method.invoke(o);
            }

            Object pojo = null;
            long start = System.currentTimeMillis();

            pojo = Pojo.class.newInstance();
            for (int i = 0; i < count; i++) {
                method.invoke(pojo);
            }

            long time = (System.currentTimeMillis() - start);
            System.out.println("testReflection time: " + time + ", count: " + pojo);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        PerfTest test = new PerfTest();
        test.testNative();
        test.testDynamic();
        test.testUnreflect();
        test.testReflection();
    }
}
