/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import javax.ejb.Local;

/**
 *
 * @author daniel
 */
@Local
public interface VentaManager extends GenericDao<Venta, Long> {
    
    public MensajeDTO guardarVenta(Long idCompra, DetalleVenta venta, String nroFactura, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO editarVenta(Long idCompra, Venta venta, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception;
    
}
