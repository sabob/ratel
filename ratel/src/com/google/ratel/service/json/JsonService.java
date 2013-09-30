/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.google.ratel.service.json;

import java.io.*;
import javax.servlet.ServletContext;

/**
 *
 */
public interface JsonService {
    
    public void onInit(ServletContext servletContext);
    
    public void onDestroy(ServletContext servletContext);

    public String toJson(Object obj);
    
    public void toJson(Object obj, Writer writer);
    
    public String toJson(JsonElement wrapper);
    
    public <T> T fromJson(String str, Class<T> type);

    public <T> T fromJson(Reader reader, Class<T> type);
    
    public <T> T fromJson(JsonElement wrapper, Class<T> type);

    public JsonElement parseJson(String str);
    
    public JsonElement parseJson(Reader reader);
    
}
