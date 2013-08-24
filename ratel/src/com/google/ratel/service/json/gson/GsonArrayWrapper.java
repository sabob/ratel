/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.service.json.gson;

import com.google.ratel.deps.gson.JsonArray;
import com.google.ratel.deps.gson.JsonElement;
import com.google.ratel.service.json.*;

/**
 *
 */
public class GsonArrayWrapper implements JsonArrayWrapper {

    protected JsonArray jsonArray;
    
    public GsonArrayWrapper(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public JsonElementWrapper get(int index) {
        JsonElement jsonElement = jsonArray.get(index);
        JsonElementWrapper wrapper = new GsonElementWrapper(jsonElement);
        return wrapper;
    }

    @Override
    public int size() {
        int size = jsonArray.size();
        return size;
    }
}
