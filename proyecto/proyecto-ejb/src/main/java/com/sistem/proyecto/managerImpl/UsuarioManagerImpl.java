/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.managerImpl;

import com.sistem.proyecto.entity.Usuario;
import com.sistem.proyecto.manager.UsuarioManager;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author Miguel
 */
@Stateless
public class UsuarioManagerImpl extends GenericDaoImpl<Usuario, Long>
		implements UsuarioManager{
        @Override
	protected Class<Usuario> getEntityBeanType() {
		return Usuario.class;
	}

}
