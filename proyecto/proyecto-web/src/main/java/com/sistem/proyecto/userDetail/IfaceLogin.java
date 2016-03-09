/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.userDetail;

import com.sistem.proyecto.entity.Usuario;





/**
 *
 * @author miguel ojeda
 */
public interface IfaceLogin {
    
    Usuario validarLogin(Usuario obj) throws Exception;
}
