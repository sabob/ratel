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
public class JacksonElementWrapper implements JsonElementWrapper {

    protected JsonNode jsonNode;

    public JacksonElementWrapper(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public boolean isJsonArray() {
        boolean isArray = jsonNode.isArray();
        return isArray;
    }

    @Override
    public JsonArrayWrapper getAsJsonArray() {
        ArrayNode jsonArray = (ArrayNode) jsonNode;
        JsonArrayWrapper wrapper = new JacksonArrayWrapper(jsonArray);
        return wrapper;
    }
   
    @Override
    public JsonNode unwrap() {
        return jsonNode;
    }
}
