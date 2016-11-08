/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Empleo;
import com.sistem.proyecto.manager.EmpleoManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class EmpleoManagerImpl extends GenericDaoImpl<Empleo, Long>
		implements EmpleoManager{
        @Override
	protected Class<Empleo> getEntityBeanType() {
		return Empleo.class;
	}
}
