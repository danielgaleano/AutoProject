/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Modelo;
import com.sistem.proyecto.manager.ModeloManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class ModeloManagerImpl extends GenericDaoImpl<Modelo, Long> 
            implements ModeloManager{
    @Override
    protected Class<Modelo> getEntityBeanType(){
        return Modelo.class;
    }
}
