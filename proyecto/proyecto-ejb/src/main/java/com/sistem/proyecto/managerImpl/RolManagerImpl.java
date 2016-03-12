/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Rol;
import com.sistem.proyecto.manager.RolManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class RolManagerImpl extends GenericDaoImpl<Rol, Long>
		implements RolManager{
        @Override
	protected Class<Rol> getEntityBeanType() {
		return Rol.class;
	}
        
        
}
