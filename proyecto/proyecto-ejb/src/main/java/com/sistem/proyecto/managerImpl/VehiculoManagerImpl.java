/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Vehiculo;
import com.sistem.proyecto.manager.VehiculoManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class VehiculoManagerImpl extends GenericDaoImpl<Vehiculo, Long> 
            implements VehiculoManager{
    @Override
    protected Class<Vehiculo> getEntityBeanType(){
        return Vehiculo.class;
    }
}
