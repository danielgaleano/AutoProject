/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Permiso;
import com.sistem.proyecto.manager.PermisoManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class PermisoManagerImpl extends GenericDaoImpl<Permiso, Long>
		implements PermisoManager{
    @Override
    protected Class<Permiso> getEntityBeanType() {
            return Permiso.class;
    }
}
