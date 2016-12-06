/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author daniel
 */
@Entity
public class NumeracionFactura implements Serializable {

    private static final long serialVersionUID = 15465L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "valor")
    private String valor;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public NumeracionFactura(Long id) {
        this.setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private String finFecha;

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


}
