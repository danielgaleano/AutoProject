/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.Compra;
import com.sistem.proyecto.entity.DetalleCompra;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Local;


/**
 *
 * @author Miguel
 */
@Local
public interface CompraManager extends GenericDao<Compra, Long> {
    
    public MensajeDTO guardarCompra(Long idCompra, DetalleCompra compra, String nroFactura, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO editarCompra(Long idCompra, Compra compra, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception;
    
}
