/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.RolPermiso;
import com.sistem.proyecto.manager.RolPermisoManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class RolPermisoManagerImpl extends GenericDaoImpl<RolPermiso, Long>
		implements RolPermisoManager{
    @Override
    protected Class<RolPermiso> getEntityBeanType() {
            return RolPermiso.class;
    }
}
