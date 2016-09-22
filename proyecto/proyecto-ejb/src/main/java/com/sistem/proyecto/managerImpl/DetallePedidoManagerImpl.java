/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.DetallePedido;
import com.sistem.proyecto.manager.DetallePedidoManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class DetallePedidoManagerImpl extends GenericDaoImpl<DetallePedido, Long>
		implements DetallePedidoManager{
        @Override
	protected Class<DetallePedido> getEntityBeanType() {
		return DetallePedido.class;
	}
        
        
}
