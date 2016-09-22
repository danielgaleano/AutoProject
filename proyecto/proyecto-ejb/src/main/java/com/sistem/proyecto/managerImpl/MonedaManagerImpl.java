/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Moneda;
import com.sistem.proyecto.manager.MonedaManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class MonedaManagerImpl extends GenericDaoImpl<Moneda, Long>
		implements MonedaManager{
        @Override
	protected Class<Moneda> getEntityBeanType() {
		return Moneda.class;
	}
}
