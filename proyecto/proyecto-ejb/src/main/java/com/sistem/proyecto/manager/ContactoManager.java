/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Contacto;
import javax.ejb.Local;




/**
 *
 * @author Miguel Ojeda
 */
@Local
public interface ContactoManager extends GenericDao<Contacto, Long> {
    
}
