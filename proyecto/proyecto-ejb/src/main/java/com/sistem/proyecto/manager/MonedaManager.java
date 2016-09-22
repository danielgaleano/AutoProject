/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Moneda;
import javax.ejb.Local;

/**
 *
 * @author Miguel
 */
@Local
public interface MonedaManager extends GenericDao<Moneda, Long>{
    

}
