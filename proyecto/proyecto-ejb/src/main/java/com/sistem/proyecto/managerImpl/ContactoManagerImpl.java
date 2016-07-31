/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Contacto;
import com.sistem.proyecto.manager.ContactoManager;
import javax.ejb.Stateless;



/**
 *
* @author Miguel Ojeda
 */
@Stateless
public class ContactoManagerImpl extends GenericDaoImpl<Contacto, Long> implements ContactoManager{

    @Override
    protected Class<Contacto> getEntityBeanType() {
        return Contacto.class;
    }
    
}
