/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.json;

import com.google.ratel.deps.gson.Gson;
import com.google.ratel.deps.gson.JsonArray;
import com.google.ratel.deps.gson.JsonElement;
import com.google.ratel.deps.gson.JsonParser;
import com.google.ratel.deps.gson.reflect.TypeToken;
import com.google.ratel.deps.io.IOUtils;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PojoTest {

    public static void main(String args[]) throws Exception {
        //testPojoGraph();
        //testPojoInList();
        //testArray();
        testPrimitives();
    }

    public static void testPrimitives() throws Exception {
        Gson gson = new Gson();

        String input = "{55}";
        String json = gson.toJson(input);

        System.out.println("input : " + input);
        System.out.println("json : " + json);
        System.out.println("Compare: " + input.equals(json) + " json length: " + json.length() + " input.length: " + input.length());
        //json = "{55}";
        gson = new Gson();
        
        Integer b = gson.fromJson("55", Integer.class);
        System.out.println("output : " + b);
    }

    public static void testArray() throws Exception {
        Gson gson = new Gson();

        Person person = new Person();
        person.setName("Moopok");

        Object[] ar = new Object[4];
        ar[0] = 3;
        ar[1] = 2000000l;
        ar[2] = true;
        ar[3] = person;

        String json = gson.toJson(ar);

        System.out.println("input : " + json);

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json);
        if (jsonElement.isJsonArray()) {

            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Object[] result = gson.fromJson(jsonArray, Object[].class);

            System.out.println("output : " + Arrays.toString(result));
        }

    }

    public static void testPojoGraph() throws Exception {
        Gson gson = new Gson();

        InputStream is = PojoTest.class
            .getResourceAsStream("PojoTest.json");
        String json = IOUtils.toString(is);

        System.out.println(json);

        /*
         Person person = new Person();
         person.setName("Bob Schellink");
         person.setAge(35);
         person.setHappy(true);

         Organisation org = new Organisation();
         org.setOrgName("Big corp");
         person.setOrg(org);

         String json = gson.toJson(person);
         System.out.println(json);
         */
        Person p = gson.fromJson(json, Person.class);

        System.out.println(
            "Result: " + p);
    }

    public static void testPojoInList() {
        Person person = new Person();
        person.setName("Bob Schellink");
        person.setAge(35);
        person.setHappy(true);

        Organisation org = new Organisation();
        org.setOrgName("Big corp");
        person.setOrg(org);

        List<Person> list = new ArrayList<Person>();
        list.add(person);

        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);

        Type collectionType = new TypeToken<List<Person>>() {
        }.getType();
        list = gson.fromJson(json, collectionType);
        Person p = list.get(0);
        System.out.println("Result: " + p);
        /*
         JsonParser parser = new JsonParser();
         JsonObject obj = parser.parse(json).getAsJsonObject();
         boolean isArray = obj.isJsonArray();
         if (isArray) {
         JsonArray array = obj.getAsJsonArray();
         }

         Set<Entry<String, JsonElement>> entrySet = obj.entrySet();
         for (Entry<String, JsonElement> entry : entrySet) {
         String key = entry.getKey();
         JsonElement el = entry.getValue();

         Class cls = null;


         if (key.equals("foo")) {
         cls = Foo.class;

         } else {
         cls = Bar.class;

         }
         Object o = gson.fromJson(el, cls);
         //System.out.println(o);
         */
    }
}
