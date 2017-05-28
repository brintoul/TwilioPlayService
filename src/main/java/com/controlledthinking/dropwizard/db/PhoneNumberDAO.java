/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controlledthinking.dropwizard.db;

import com.controlledthinking.dropwizard.core.PhoneNumber;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author brintoul
 */
public class PhoneNumberDAO extends AbstractDAO<PhoneNumber> {
    
    public PhoneNumberDAO(SessionFactory factory) {
        super(factory);
    }

    public PhoneNumber findById(Long id) {
        return get(id);
    }

    public long create(PhoneNumber number) {
        return persist(number).getPhoneNumberId();
    }

    public List<PhoneNumber> findAll() {
        return list(namedQuery("PhoneNumber.findAll"));
    }
    
    public List<PhoneNumber> findByUserId(Integer userId) {
        return list(namedQuery("PhoneNumber.findByUserId").setParameter("userId", userId));
    }
}
