/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.beans;

import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 *
 * @author brintoul
 */
public class UserCredentials {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public UserCredentials()
    {
    }

    public UserCredentials(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserCredentials)) return false;
        UserCredentials that = (UserCredentials) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, password);
    } 
}
