package com.google.ratel.service.json.jackson;

import com.google.ratel.deps.jackson.annotation.JsonInclude;
import com.google.ratel.deps.jackson.core.JsonProcessingException;
import com.google.ratel.deps.jackson.databind.JsonNode;
import com.google.ratel.deps.jackson.databind.ObjectMapper;
import com.google.ratel.deps.jackson.databind.SerializationFeature;
import javax.servlet.*;
import com.google.ratel.service.json.*;
import java.io.*;

/**
 *
 */
public class JacksonService implements JsonService {

    private boolean prettyPrint;

    private boolean serializeNulls;

    ObjectMapper mapper;

    public JacksonService() {
    }

    @Override
    public void onInit(ServletContext servletContext) {
        mapper = new ObjectMapper();

        // Exclude nulls by default
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if (isSerializeNulls()) {
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        }

        if (isPrettyPrint()) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    @Override
    public void onDestroy(ServletContext servletContext) {
    }

    @Override
    public String toJson(Object obj) {

        try {
            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public String toJson(JsonElementWrapper wrapper) {

        try {
            Object obj = wrapper.unwrap();
            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void toJson(Object obj, Writer writer) {
        try {
            mapper.writeValue(writer, obj);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public <T> T fromJson(Reader reader, Class<T> type) {
        try {
            T result = mapper.readValue(reader, type);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> T fromJson(JsonElementWrapper wrapper, Class<T> type) {
        JsonNode node = (JsonNode) wrapper.unwrap();
        try {
            //T result = reader.readValue(node);
            T result = mapper.reader(type).readValue(node);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public JsonElementWrapper parseJson(String str) {
        try {
            //JsonNode node = reader.readTree(str);
            JsonNode node = mapper.readTree(str);
            JacksonElementWrapper wrapper = new JacksonElementWrapper(node);
            return wrapper;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public JsonElementWrapper parseJson(Reader reader) {
        try {
            JsonNode node = mapper.readTree(reader);
            JacksonElementWrapper wrapper = new JacksonElementWrapper(node);
            return wrapper;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return the prettyPrint
     */
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * @param prettyPrint the prettyPrint to set
     */
    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    /**
     * @return the serializeNulls
     */
    public boolean isSerializeNulls() {
        return serializeNulls;
    }

    /**
     * @param serializeNulls the serializeNulls to set
     */
    public void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }
}
