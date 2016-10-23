/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Compra;
import javax.ejb.Local;


/**
 *
 * @author Miguel
 */
@Local
public interface CompraManager extends GenericDao<Compra, Long> {
    
}
