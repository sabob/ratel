/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stringspeed;

import com.eaio.stringsearch.BoyerMooreHorspoolRaita;

/**
 *
 */
public class Test {

    long count = 100000;

    private String str = "personservice/mymethod/";

    private String pattern = "/";

    public void testBoyer() {
        long start = System.currentTimeMillis();
        BoyerMooreHorspoolRaita al = new BoyerMooreHorspoolRaita();

        for (int i = 0; i < count; i++) {
            int j = al.searchString(str, pattern);
        }
        System.out.println("Time BoyerMooreHorspoolRaita(): " + (System.currentTimeMillis() - start));
    }

    public void testIndexOf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int j = str.indexOf(pattern);
        }
        System.out.println("Time indexOf(): " + (System.currentTimeMillis() - start));
    }

    public static void main(String args[]) {
        Test test = new Test();
        test.testIndexOf();
        test.testBoyer();
    }
}