/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Tipo;
import com.sistem.proyecto.manager.TipoManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class TipoManagerImpl extends GenericDaoImpl<Tipo, Long>
		implements TipoManager{
        @Override
	protected Class<Tipo> getEntityBeanType() {
		return Tipo.class;
	}
        
        
}
