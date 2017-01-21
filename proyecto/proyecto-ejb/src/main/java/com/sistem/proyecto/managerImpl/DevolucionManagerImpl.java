/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Devolucion;
import com.sistem.proyecto.manager.DevolucionManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class DevolucionManagerImpl extends GenericDaoImpl<Devolucion, Long>
		implements DevolucionManager{
        @Override
	protected Class<Devolucion> getEntityBeanType() {
		return Devolucion.class;
	}
    
}
