package org.json;

import com.google.ratel.deps.io.IOUtils;
import com.google.ratel.deps.jackson.core.type.TypeReference;
import com.google.ratel.deps.jackson.databind.*;
import com.google.ratel.deps.jackson.databind.node.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class PojoTest {

    public static void main(String args[]) throws Exception {
        testPojoGraph();
        testPojoInList();
        //testArray();
        testPrimitives();
    }

    public static void testPrimitives() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String input = "{55}";
        String json = mapper.writeValueAsString(input);

        System.out.println("input : " + input);
        System.out.println("json : " + json);
        System.out.println("Compare: " + input.equals(json) + " json length: " + json.length() + " input.length: " + input.length());
        //json = "{55}";

        mapper = new ObjectMapper();
        Integer b = mapper.readValue("55", Integer.class);
        System.out.println("output : " + b);
    }

    public static void testArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Person person = new Person();
        person.setName("Moopok");

        Object[] ar = new Object[4];
        ar[0] = 3;
        ar[1] = 2000000l;
        ar[2] = true;
        ar[3] = person;

        String json = mapper.writeValueAsString(ar);

        System.out.println("input : " + json);

        JsonNode node = mapper.readTree(json);
        if (node.isArray()) {

            ArrayNode jsonArray = (ArrayNode) node;
            Object[] result = mapper.reader(Object[].class).readValue(jsonArray);

            System.out.println("output : " + Arrays.toString(result));
        }

    }

    public static void testPojoGraph() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream is = PojoTest.class.getResourceAsStream("PojoTest.json");
        String json = IOUtils.toString(is);

        System.out.println(json);

        Person p = mapper.readValue(json, Person.class);

        System.out.println(
            "Result: " + p);
    }

    public static void testPojoInList() throws Exception{
        Person person = new Person();
        person.setName("Bob Schellink");
        person.setAge(35);
        person.setHappy(true);

        Organisation org = new Organisation();
        org.setOrgName("Big corp");
        person.setOrg(org);

        List<Person> list = new ArrayList<Person>();
        list.add(person);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(json);

        TypeReference collectionType = new TypeReference<List<Person>>(){};  
        //Type collectionType = new TypeToken<List<Person>>() {}.getType();
        list = mapper.readValue(json, collectionType);
        Person p = list.get(0);
        System.out.println("Result: " + p);
    }
}
