/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.json;

import org.apache.commons.lang.builder.*;

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
