/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.service.json;

/**
 *
 */
public interface JsonElementWrapper {

    public boolean isJsonArray();
    
    public JsonArrayWrapper getAsJsonArray();

    public Object unwrap();
}
