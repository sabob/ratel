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
public class JacksonJsonElement implements JsonElement {

    protected JsonNode jsonNode;

    public JacksonJsonElement(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public boolean isJsonArray() {
        boolean isArray = jsonNode.isArray();
        return isArray;
    }

    @Override
    public JsonArray getAsJsonArray() {
        ArrayNode jsonArray = (ArrayNode) jsonNode;
        JsonArray wrapper = new JacksonJsonArray(jsonArray);
        return wrapper;
    }
   
    @Override
    public JsonNode unwrap() {
        return jsonNode;
    }
}
