/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.manager;

import com.sistem.proyecto.entity.DetalleVenta;
import com.sistem.proyecto.entity.Venta;
import com.sistem.proyecto.manager.utils.MensajeDTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author daniel
 */
@Local
public interface VentaManager extends GenericDao<Venta, Long> {
    
    public MensajeDTO guardarVenta(List<Long> itemVentas, Venta venta, String nroFactura, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception;
    
    public MensajeDTO editarVenta(List<Long> itemVentas, Venta venta, Long idVenta, String formaPgo, String tipoPago, Long idEmpresa, Long idUsuario) throws Exception;
    
}
