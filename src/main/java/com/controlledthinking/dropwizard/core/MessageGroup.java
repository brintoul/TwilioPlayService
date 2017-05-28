/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author brintoul
 */
@Entity
@Table(name = "message_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessageGroup.findAll", query = "SELECT m FROM MessageGroup m"),
    @NamedQuery(name = "MessageGroup.findByGroupIdWithCustomers", query = "SELECT m FROM MessageGroup m LEFT JOIN FETCH m.customerCollection WHERE m.groupId = :groupId"),
    @NamedQuery(name = "MessageGroup.findByUserId", query = "SELECT m FROM MessageGroup m WHERE m.user.userId = :userId"),
    @NamedQuery(name = "MessageGroup.findByGroupName", query = "SELECT m FROM MessageGroup m WHERE m.groupName = :groupName")})
public class MessageGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "group_name")
    private String groupName;
    @JoinTable(name = "cust_group", joinColumns = {
        @JoinColumn(name = "group_id", referencedColumnName = "group_id")}, 
        inverseJoinColumns = {
            @JoinColumn(name = "cust_id", referencedColumnName = "customer_id")
        })
    @ManyToMany
    private Set<Customer> customerCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    public MessageGroup() {
    }

    public MessageGroup(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @JsonProperty
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonIgnore
    public Collection<Customer> getCustomerCollection() {
        return customerCollection;
    }

    public void setCustomerCollection(Set<Customer> customerCollection) {
        this.customerCollection = customerCollection;
    }

    @XmlTransient
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupId != null ? groupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessageGroup)) {
            return false;
        }
        MessageGroup other = (MessageGroup) object;
        if ((this.groupId == null && other.groupId != null) || (this.groupId != null && !this.groupId.equals(other.groupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bananamodel.MessageGroup[ groupId=" + groupId + " ]";
    }

}
