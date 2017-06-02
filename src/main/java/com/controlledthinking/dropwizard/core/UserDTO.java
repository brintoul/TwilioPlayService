/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.core;

import com.controlledthinking.dropwizard.beans.Privilege;
import java.util.Set;

/**
 *
 * @author brintoul
 */
public class UserDTO {
    
    private int userId;
    private Set<Privilege> privileges;

    public UserDTO(Integer userId, Set<Privilege> privileges) {
        this.userId = userId;
        this.privileges = privileges;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "Privileges: " + this.privileges.toString();
    }
    
}
