/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.json;

import com.google.ratel.deps.lang3.builder.ReflectionToStringBuilder;
import com.google.ratel.deps.lang3.builder.ToStringBuilder;

/**
 *
 */
public class Organisation {

    private String orgName;

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    @Override
    public String toString() {
        ToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }
}
