/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.manager.DetalleVentaManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class DetalleVentaManagerImpl extends GenericDaoImpl<DetalleVenta, Long>
		implements DetalleVentaManager{
    @Override
    protected Class<DetalleVenta> getEntityBeanType() {
	return DetalleVenta.class;
    }
}
