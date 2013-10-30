package com.google.ratel.service.resolver;

/**
 *
 */
public class DefaultServiceResolverTest {

    
    public static void main(String args[]) {
        DefaultServiceResolver r = new DefaultServiceResolver();
        String service = r.resolveServicePath("/class/method");
        System.out.println(service);

        String method = r.resolveMethodPath("/class/method");
        System.out.println(method);
    }
}
