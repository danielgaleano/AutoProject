/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.NumeracionFactura;
import com.sistem.proyecto.manager.NumeracionFacturaManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */

@Stateless
public class NumeracionFacturaManagerImpl extends GenericDaoImpl<NumeracionFactura, Long> 
                implements NumeracionFacturaManager {
        @Override
	protected Class<NumeracionFactura> getEntityBeanType() {
		return NumeracionFactura.class;
	}
}
