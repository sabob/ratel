/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.service.json;

/**
 *
 */
public interface JsonElement {

    public boolean isJsonArray();
    
    public JsonArray getAsJsonArray();

    public Object unwrap();
}
