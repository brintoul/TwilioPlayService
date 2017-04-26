/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author brintoul
 */
@Entity
@Table(name = "phone_numbers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneNumber.findAll", query = "SELECT p FROM PhoneNumber p"),
    @NamedQuery(name = "PhoneNumber.findByPhoneNumberId", query = "SELECT p FROM PhoneNumber p WHERE p.phoneNumberId = :phoneNumberId"),
    @NamedQuery(name = "PhoneNumber.findByUserId", query = "SELECT p FROM PhoneNumber p INNER JOIN p.userId u WHERE u.userId = :userId")
})
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "phone_number_id")
    private Integer phoneNumberId;
    @Size(max = 32)
    @Column(name = "number_text")
    private String numberText;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User userId;

    public PhoneNumber() {
    }

    public PhoneNumber(Integer phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }
    
    @JsonProperty
    public Integer getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(Integer phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    @JsonProperty
    public String getNumberText() {
        return numberText;
    }

    public void setNumberText(String numberText) {
        this.numberText = numberText;
    }

    @JsonIgnore
    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (phoneNumberId != null ? phoneNumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber other = (PhoneNumber) object;
        if ((this.phoneNumberId == null && other.phoneNumberId != null) || (this.phoneNumberId != null && !this.phoneNumberId.equals(other.phoneNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.controlledthinking.dropwizard.db.PhoneNumber[ phoneNumberText=" + numberText + " ]";
    }
    
}
