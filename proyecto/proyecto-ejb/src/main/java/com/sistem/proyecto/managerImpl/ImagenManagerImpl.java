/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.manager.ImagenManager;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class ImagenManagerImpl extends GenericDaoImpl<Imagen, Long> implements
		ImagenManager {

	
    @Override
    protected Class<Imagen> getEntityBeanType() {
            return Imagen.class;
    }

}
