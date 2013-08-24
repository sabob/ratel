package co.za.mom.tests;

import org.apache.commons.lang.builder.*;

public class Organisation {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }
}
