/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.DocumentoCobrar;
import com.sistem.proyecto.manager.DocumentoCobrarManager;
import javax.ejb.Stateless;

/**
 *
 * @author daniel
 */
@Stateless
public class DocumentoCobrarManagerImpl extends GenericDaoImpl<DocumentoCobrar, Long>
		implements DocumentoCobrarManager{
        @Override
	protected Class<DocumentoCobrar> getEntityBeanType() {
		return DocumentoCobrar.class;
	}
}
