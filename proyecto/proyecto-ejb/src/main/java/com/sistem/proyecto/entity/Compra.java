/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Miguel
 */
public class Compra extends Base{
    
    @Column(name = "nro_factura", nullable = false)
    private Long nroFactura;
    
    @Column(name = "fechaCompra")
    private Timestamp fechaCompra;
    
    @Column(name = "tipo_compra")
    private String tipoCompra;
    
    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
}
