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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author brintoul
 */
@Entity
@Table(name = "customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findByCustomerId", query = "SELECT c FROM Customer c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "Customer.findByFirstName", query = "SELECT c FROM Customer c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "Customer.findByLastName", query = "SELECT c FROM Customer c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "Customer.findByUser", query = "SELECT c FROM Customer c WHERE c.user.userId = :userId"), 
    @NamedQuery(name = "Customer.findByGroup", query = "SELECT c FROM Customer c JOIN c.messageGroups groups WHERE groups.groupId = :groupId"),     
    @NamedQuery(name = "Customer.findByNumberText", query = "SELECT c FROM Customer c WHERE c.numberText = :numberText")
})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "number_text")
    private String numberText;
    @Column(name = "specific_id_for_user")
    private String reservedSpecificId;
    @ManyToMany(mappedBy = "customerCollection", fetch=FetchType.LAZY)
    private Set<MessageGroup> messageGroups;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Customer() {
    }

    public Customer(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    public String getNumberText() {
        return numberText;
    }

    public void setNumberText(String numberText) {
        this.numberText = numberText;
    }

    public String getReservedSpecificId() {
        return reservedSpecificId;
    }

    public void setReservedSpecificId(String reservedSpecificId) {
        this.reservedSpecificId = reservedSpecificId;
    }

    @XmlTransient
    public Collection<MessageGroup> getMessageGroupCollection() {
        return messageGroups;
    }

    public void setMessageGroupCollection(Set<MessageGroup> messageGroups) {
        this.messageGroups = messageGroups;
    }

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
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bananamodel.Customer[ customerId=" + customerId + " ]";
    }
    
}
