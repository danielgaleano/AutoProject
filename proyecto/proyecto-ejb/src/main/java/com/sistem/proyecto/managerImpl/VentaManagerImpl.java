/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.VentaManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class VentaManagerImpl extends GenericDaoImpl<Venta, Long>
		implements VentaManager{
    @Override
	protected Class<Venta> getEntityBeanType() {
		return Venta.class;
	}
}
