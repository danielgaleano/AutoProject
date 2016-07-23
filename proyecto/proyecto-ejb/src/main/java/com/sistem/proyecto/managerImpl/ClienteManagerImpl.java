/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Cliente;
import com.sistem.proyecto.manager.ClienteManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class ClienteManagerImpl extends GenericDaoImpl<Cliente, Long>
		implements ClienteManager{
        @Override
	protected Class<Cliente> getEntityBeanType() {
		return Cliente.class;
	}
}
