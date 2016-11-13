/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Caja;
import com.sistem.proyecto.manager.CajaManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class CajaManagerImpl extends GenericDaoImpl<Caja, Long>
		implements CajaManager{
        @Override
	protected Class<Caja> getEntityBeanType() {
		return Caja.class;
	}
}
