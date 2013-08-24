package com.google.ratel.service.json.gson;

import javax.servlet.*;
import com.google.ratel.deps.gson.Gson;
import com.google.ratel.deps.gson.GsonBuilder;
import com.google.ratel.deps.gson.JsonElement;
import com.google.ratel.deps.gson.JsonParser;
import com.google.ratel.deps.gson.JsonSyntaxException;
import com.google.ratel.service.json.*;
import java.io.*;
import java.io.Writer;

/**
 *
 */
public class GsonService implements JsonService {

    private boolean prettyPrint;

    private boolean serializeNulls;

    private Gson gson;
    
    private JsonParser jsonParser;

    public GsonService() {
    }

    @Override
    public void onInit(ServletContext servletContext) {
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls()) {
            builder.serializeNulls();
        }
        if (isPrettyPrint()) {
            builder.setPrettyPrinting();
        }
        gson = builder.create();
    }

    @Override
    public void onDestroy(ServletContext servletContext) {
    }

    @Override
    public String toJson(Object obj) {
        String json = gson.toJson(obj);
        return json;
    }
    
    @Override
    public String toJson(JsonElementWrapper wrapper) {
        Object obj  = wrapper.unwrap();
        String json = gson.toJson(obj);
        return json;
    }
    
    @Override
    public void toJson(Object obj, Writer writer) {
        gson.toJson(obj, writer);
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            T result = gson.fromJson(json, type);
            return result;

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("The input Json is not syntactically correct!", e);
        }
    }
    
    @Override
    public <T> T fromJson(Reader reader, Class<T> type) {
        try {
            T result = gson.fromJson(reader, type);
            return result;

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("The input Json is not syntactically correct!", e);
        }
    }

    @Override
    public <T> T fromJson(JsonElementWrapper wrapper, Class<T> type) {
        JsonElement element = (JsonElement) wrapper.unwrap();
        T result = gson.fromJson(element, type);
        return result;
    }

    @Override
    public JsonElementWrapper parseJson(String str) {
        JsonElement element = jsonParser.parse(str);
        JsonElementWrapper wrapper = new GsonElementWrapper(element);
        return wrapper;
    }
    
    @Override
    public JsonElementWrapper parseJson(Reader reader) {
        JsonElement element = jsonParser.parse(reader);
        JsonElementWrapper wrapper = new GsonElementWrapper(element);
        return wrapper;
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
