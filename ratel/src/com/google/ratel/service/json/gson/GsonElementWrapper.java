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
public class GsonElementWrapper implements JsonElementWrapper {

    protected JsonElement jsonElement;

    public GsonElementWrapper(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }

    @Override
    public boolean isJsonArray() {
        boolean isArray = jsonElement.isJsonArray();
        return isArray;
    }

    @Override
    public JsonArrayWrapper getAsJsonArray() {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonArrayWrapper wrapper = new GsonArrayWrapper(jsonArray);
        return wrapper;
    }
   
    @Override
    public JsonElement unwrap() {
        return jsonElement;
    }
}
