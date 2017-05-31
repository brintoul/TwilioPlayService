/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.core;

import com.controlledthinking.dropwizard.beans.Privilege;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.security.auth.Subject;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author brintoul
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.fetchAllCustomers", query = "SELECT u FROM User u JOIN FETCH u.customers WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")})
public class User implements Principal, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Size(max = 64)
    @Column(name = "username")
    private String username;
    @Size(max = 64)
    //TODO: DO NOT STORE IN CLEAR!!
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "user")
    @XmlTransient
    @JsonIgnore
    private Set<PhoneNumber> phoneNumbersCollection;
    @OneToMany(mappedBy = "user")
    private Set<Customer> customers;
    @Transient
    private HashSet<Privilege> privileges;
    
    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String username, HashSet<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Collection<PhoneNumber> getPhoneNumbersCollection() {
        return phoneNumbersCollection;
    }

    public void setPhoneNumbersCollection(Set<PhoneNumber> phoneNumbersCollection) {
        this.phoneNumbersCollection = phoneNumbersCollection;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Privilege> getPrivileges() {
        return this.privileges;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.controlledthinking.dropwizard.db.User[ userId=" + userId + " ]";
    }    

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
