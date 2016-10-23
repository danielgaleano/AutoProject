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
public class Cuotas extends Base {
    
    @Column(name = "cuota")
    private Long cuota;
    
    @Column(name = "monto_cuotas")
    private String montoCuotas;
    
    @Column(name = "saldo")
    private String saldo;
    
    @Column(name = "estado")
    private Boolean estado;
        
    
    @Column(name = "fechaVencimiento")
    private Timestamp fechaVencimiento;
    
    @ManyToOne
    @JoinColumn(name = "compra")
    private Compra compra;
    
    @ManyToOne
    @JoinColumn(name = "venta")
    private Venta venta;
    
    
    
}
