/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycorp.domain;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT")
public class Client {

    private Long id;
    
    private String firstname;
    
    private String lastname;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    
}
