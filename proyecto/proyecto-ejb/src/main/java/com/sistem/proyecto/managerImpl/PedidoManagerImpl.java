/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Pedido;
import com.sistem.proyecto.manager.PedidoManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class PedidoManagerImpl extends GenericDaoImpl<Pedido, Long>
		implements PedidoManager{
        @Override
	protected Class<Pedido> getEntityBeanType() {
		return Pedido.class;
	}
}
