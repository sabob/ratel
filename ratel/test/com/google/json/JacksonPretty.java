/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.json;

import com.google.dao.*;
import com.google.ratel.deps.jackson.databind.*;
import com.google.ratel.deps.jackson.databind.node.ObjectNode;
import com.google.ratel.deps.jackson.databind.node.TextNode;
import java.util.*;

/**
 *
 */
public class JacksonPretty {

    ObjectMapper mapper;

    public void testToJson() {
        try {
            Customer customer = new Customer();
            customer.setFirstName("Bob");
            customer.setLastName("Moore");
            customer.setBirthDate(new Date());

            String json = "{\"name\":\"Bob\",\"age\":\"23\"}";
            ObjectNode node = mapper.createObjectNode();
            node.set("val", new TextNode(json));
            System.out.println(json);
            //JsonNode node = mapper.readTree(json);
            json = mapper.writeValueAsString(node);
            //json = mapper.writeValueAsString(customer);
            System.out.println(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) {
        JacksonPretty test = new JacksonPretty();
        test.mapper = new ObjectMapper();

        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        //test.mapper.setDateFormat(format);
        // Exclude nulls by default
        test.mapper.enable(SerializationFeature.INDENT_OUTPUT);


        test.testToJson();
    }
}
