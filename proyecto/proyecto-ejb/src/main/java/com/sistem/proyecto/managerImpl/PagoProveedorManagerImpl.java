/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.PagoProveedor;
import com.sistem.proyecto.manager.PagoProveedorManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class PagoProveedorManagerImpl extends GenericDaoImpl<PagoProveedor, Long>
		implements PagoProveedorManager{
        @Override
	protected Class<PagoProveedor> getEntityBeanType() {
		return PagoProveedor.class;
	}
}
