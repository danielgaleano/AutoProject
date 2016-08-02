/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Marca;
import com.sistem.proyecto.manager.MarcaManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class MarcaManagerImpl extends GenericDaoImpl<Marca, Long>
		implements MarcaManager{
        @Override
	protected Class<Marca> getEntityBeanType() {
		return Marca.class;
	}
        
        
}
