/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author daniel
 */
@Entity
public class DocumentoPagar extends Base {
    
    @Column(name = "nro_cuota", nullable = true)
    private String nroCuota;
    
    @ManyToOne
    @JoinColumn(name = "compra")
    private Compra compra;
    
    @Column(name = "monto")
    private String monto;
    
    @Column(name = "fecha")
    private Timestamp fecha;
    
    @Column(name = "estado")
    private String estado;
    
    
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
    public String getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(String monto) {
        this.monto = monto;
    }
    
    /**
     * @return the fecha
     */
    public Timestamp getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Timestamp fecha) {
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
    
}
