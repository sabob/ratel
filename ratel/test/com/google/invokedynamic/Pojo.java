/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.invokedynamic;

/**
 *
 */
public class Pojo {
    
    private int count;

    public void moo() {
        //System.out.println("moo called");
        count++;
    }
    
    @Override
    public String toString() {
        return "" + count;
    }
}
