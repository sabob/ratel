package com.google.ratel.util;

import com.google.ratel.deps.lang3.builder.ReflectionToStringBuilder;
import com.google.ratel.deps.lang3.builder.ToStringBuilder;

/**
 *
 */
public class Person {

    private String firstname;

    private String lastname;
    
    private Organisation org;
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }

    public Organisation getOrg() {
        return org;
    }

    public void setOrg(Organisation org) {
        this.org = org;
    }
}
