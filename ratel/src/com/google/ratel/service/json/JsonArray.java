/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.service.json;

/**
 *
 */
public interface JsonArray {

    public JsonElement get(int index);
    
    public int size();
}
