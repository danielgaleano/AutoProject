/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.manager.EmpresaManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class EmpresaManagerImpl extends GenericDaoImpl<Empresa, Long>
		implements EmpresaManager{
        @Override
	protected Class<Empresa> getEntityBeanType() {
		return Empresa.class;
	}
}
