/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.za.mom.tests;

import com.google.ratel.deps.gson.Gson;
import com.google.ratel.deps.gson.GsonBuilder;

/**
 *
 */
public class SAPersonService extends PersonService {
    
    public String getNoArgs() {
        return "OK";
    }
    
    public Integer getInteger(Integer i) {
        System.out.println("getInteger() called: " + i);
        return i;
    }
    
    public long getPrimitiveLong(long l) {
        System.out.println("getPrimitiveLong() called: " + l);
        return l;
    }
    
    public boolean getPrimitiveBoolean(boolean bool) {
        System.out.println("getPrimitiveBoolean() called: " + bool);
        return bool;
    }

    public String getJson(String args) {
        System.out.println("json() called: " + args);
        return args;
    }

    public Person getPojo(Person person) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(person);
        System.out.println("getPerson() called with: " + json);
         Person result = new Person();
         result.setFirstname(person.getFirstname());
         result.setLastname(person.getLastname());
         return result;
    }
    
    public Person getNestedPojo(Person person) {
        System.out.println("getNestedPojo() called with: person: " + person);

         Person pojo = new Person();
         pojo.setFirstname(person.getFirstname());
         pojo.setLastname(person.getLastname());
         
         if (person.getOrg() != null) {
         Organisation pojo2 = new Organisation();
         pojo2.setName(person.getOrg().getName());
         }
         
         return person;
    }
    
    public Object[] getArray(int i, long l, boolean b, Person person) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(person);
        System.out.println("getArray() called with: " + json);
         Person result = new Person();
         result.setFirstname(person.getFirstname());
         result.setLastname(person.getLastname());
         Object[] ar = new Object[4];
         ar[0] = i;
         ar[1] = l;
         ar[2] = b;
         ar[3] = person;
         return ar;
    }
}
