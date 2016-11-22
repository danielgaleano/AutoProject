/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */
@Entity
public class Movimiento extends Base {
    
    private static long serialVersionUID = -959605420407250259L;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @ManyToOne
    @JoinColumn(name = "compra")
    private Compra compra;
    
    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;
    
    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "caja")
    private Caja caja;
    
    @Column(name = "fecha_ingreso")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaIngreso;
    
    @NotNull
    @NotEmpty
    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda;
    
    @Column(name = "cotizacion")
    private Double cotizacion;
    
    @Column(name = "importe")
    private Double importe;
    
    @Column(name = "saldo")
    private Double saldo;
    
    @Column(name = "vuelto")
    private Double vuelto;
    
    @Column(name = "fecha_vencimiento")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    
    @Column(name = "cuota")
    private Long cuota;
    
    @Column(name = "interes")
    private Double interes;
    
    public Movimiento() {

    }

    public Movimiento(Long id) {
            this.setId(id);
    }
    
}
