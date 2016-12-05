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
import javax.persistence.Transient;

/**
 *
 * @author daniel
 */
@Entity
public class NumeracionFactura extends Base {
    
    private static final long serialVersionUID = 15465L;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;
    
    @Column(name = "valor")
    private String valor;
    
//    @Column(name = "inicio", nullable = false, length = 3)
//    private String inicio;
//    
//    @Column(name = "medio", nullable = false, length = 3)
//    private String medio;
//    
//    @Column(name = "fin", nullable = false, length = 3)
//    private String fin;
//    
//    @Column(name = "timbrado")
//    private String timbrado;
//    
//    @Column(name = "fecha_inicio")
//    @Temporal(javax.persistence.TemporalType.DATE)
//    private Date fechaInicio;
//    
//    @Column(name = "fecha_fin")
//    @Temporal(javax.persistence.TemporalType.DATE)
//    private Date fechaFin;
//
//    @Column(name = "por_defecto")
//    private Boolean porDefecto;
//    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;

    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
//    
//    @Transient
//    private String inicioFecha;
//    
//    @Transient
//    private String finFecha;
    
    public NumeracionFactura() {
    }

//    public NumeracionFactura(String nombre, String inicio, String medio, String fin) {
//        this.nombre = nombre;
//        this.inicio = inicio;
//        this.medio = medio;
//        this.fin = fin;
//    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getValor() {
        return valor;
    }
    
    public void setValor(String valor) {
        this.valor = valor;
    }

//    public String getInicio() {
//        return inicio;
//    }
//
//    public void setInicio(String inicio) {
//        this.inicio = inicio;
//    }
//
//    public String getMedio() {
//        return medio;
//    }
//
//    public void setMedio(String medio) {
//        this.medio = medio;
//    }
//
//    public String getFin() {
//        return fin;
//    }
//
//    public void setFin(String fin) {
//        this.fin = fin;
//    }
//
//    public String getTimbrado() {
//        return timbrado;
//    }
//
//    public Date getFechaInicio() {
//        return fechaInicio;
//    }
//
//    public Date getFechaFin() {
//        return fechaFin;
//    }
//
//    public Boolean isPorDefecto() {
//        return porDefecto;
//    }
//
//    public String getInicioFecha() {
//        return inicioFecha;
//    }
//
//    public String getFinFecha() {
//        return finFecha;
//    }
//    
//    public Boolean getPorDefecto() {
//        return porDefecto;
//    }
//    
//    public void setFechaInicio(Date fechaInicio) {
//        this.fechaInicio = fechaInicio;
//    }
//
//    public void setFechaFin(Date fechaFin) {
//        this.fechaFin = fechaFin;
//    }
//
//    public void setPorDefecto(Boolean porDefecto) {
//        this.porDefecto = porDefecto;
//    }
//
//    public void setInicioFecha(String inicioFecha) {
//        this.inicioFecha = inicioFecha;
//    }
//
//    public void setFinFecha(String finFecha) {
//        this.finFecha = finFecha;
//    }
//    
//    public void setTimbrado(String timbrado) {
//        this.timbrado = timbrado;
//    }
    
}
