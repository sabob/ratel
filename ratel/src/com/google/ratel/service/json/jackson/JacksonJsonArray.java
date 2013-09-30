/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.service.json.jackson;

import com.google.ratel.deps.jackson.databind.JsonNode;
import com.google.ratel.deps.jackson.databind.node.ArrayNode;
import com.google.ratel.service.json.*;

/**
 *
 */
public class JacksonJsonArray implements JsonArray {

    protected ArrayNode jsonArray;
    
    public JacksonJsonArray(ArrayNode jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public JsonElement get(int index) {
        JsonNode jsonNode = jsonArray.get(index);
        JsonElement wrapper = new JacksonJsonElement(jsonNode);
        return wrapper;
    }

    @Override
    public int size() {
        int size = jsonArray.size();
        return size;
    }
}
