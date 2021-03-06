package com.google.dao;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Customer")
public class Customer implements Comparable, Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private String state;
    private Date birthDate;

    public int compareTo(Object rhs) {
        Customer cust = (Customer) rhs;
        return id.compareTo(cust.getId());
    }
    
    public boolean equals(Object rhs) {
        if (rhs instanceof Customer) {
            return compareTo((Customer) rhs) == 0;
        }
        return false;
    }

    public int hashCode() {
        return id.hashCode();
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("{Customer: id=").append(id).append(", ");
        builder.append("firstName=").append(firstName).append(", ");
        builder.append("lastName=").append(lastName).append(", ");
        builder.append("state=").append(state).append(", ");
        builder.append("birthDate=").append(birthDate).append("}");
        return builder.toString();
    }
}
