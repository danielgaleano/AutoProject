/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Proveedor;
import com.sistem.proyecto.manager.ProveedorManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class ProveedorManagerImpl extends GenericDaoImpl<Proveedor, Long>
		implements ProveedorManager{
        @Override
	protected Class<Proveedor> getEntityBeanType() {
		return Proveedor.class;
	}
}
