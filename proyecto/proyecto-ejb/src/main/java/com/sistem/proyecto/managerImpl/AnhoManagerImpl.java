/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Anho;
import com.sistem.proyecto.manager.AnhoManager;

/**
 *
 * @author Miguel
 */
public class AnhoManagerImpl extends GenericDaoImpl<Anho, Long>
		implements AnhoManager{
        @Override
	protected Class<Anho> getEntityBeanType() {
		return Anho.class;
	}
}
