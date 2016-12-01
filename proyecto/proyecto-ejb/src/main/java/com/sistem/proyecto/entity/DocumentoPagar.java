/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author daniel
 */
@Entity
public class DocumentoPagar extends Base {
    
    public static final String ENTREGA = "ENTREGA";
    public static final String PARCIAL = "PARCIAL";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String CANCELADO = "CANCELADO";
    
    private static final long serialVersionUID = 1554546L;
    
    @Column(name = "nro_cuota", nullable = true)
    private String nroCuota;
    
    @ManyToOne
    @JoinColumn(name = "compra")
    private Compra compra;
    
    @Column(name = "monto")
    private Double monto;
    
    @Column(name = "saldo")
    private Double saldo;
    
    @Column(name = "montoInteres")
    private Double montoInteres;
    
    @Column(name = "fecha")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "estado")
    private String estado;
    
    public DocumentoPagar() {
    }

    public DocumentoPagar(Long id) {
        setId(id);
    }
    
    
    /**
     * @return the nroCuota
     */
    public String getNroCuota() {
        return nroCuota;
    }

    /**
     * @param nroCuota the nroCuota to set
     */
    public void setNroCuota(String nroCuota) {
        this.nroCuota = nroCuota;
    }
    
    /**
     * @return the compra
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * @param compra the compra to set
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }
    
    /**
     * @return the monto
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    
    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }    
  
    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getMontoInteres() {
        return montoInteres;
    }

    public void setMontoInteres(Double montoInteres) {
        this.montoInteres = montoInteres;
    }
    
}
