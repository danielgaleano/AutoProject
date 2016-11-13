/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.DocumentoPagar;
import com.sistem.proyecto.manager.DocumentoPagarManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class DocumentoPagarManagerImpl extends GenericDaoImpl<DocumentoPagar, Long>
		implements DocumentoPagarManager{
        @Override
	protected Class<DocumentoPagar> getEntityBeanType() {
		return DocumentoPagar.class;
	}
}
