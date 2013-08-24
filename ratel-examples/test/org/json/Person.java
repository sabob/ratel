/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.json;

import org.apache.commons.lang.builder.*;

/**
 *
 */
public class Person {

    private String name;
    
    private int age;
    
    private boolean happy;
    
    private Organisation org;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the happy
     */
    public boolean isHappy() {
        return happy;
    }

    /**
     * @param happy the happy to set
     */
    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    /**
     * @return the org
     */
    public Organisation getOrg() {
        return org;
    }

    /**
     * @param org the org to set
     */
    public void setOrg(Organisation org) {
        this.org = org;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }
}
