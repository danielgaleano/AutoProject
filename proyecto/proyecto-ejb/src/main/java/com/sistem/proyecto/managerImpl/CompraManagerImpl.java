/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.manager.CompraManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class CompraManagerImpl extends GenericDaoImpl<Compra, Long>
		implements CompraManager{
        @Override
	protected Class<Compra> getEntityBeanType() {
		return Compra.class;
	}
}
