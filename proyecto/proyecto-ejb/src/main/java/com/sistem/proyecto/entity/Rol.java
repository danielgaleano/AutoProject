/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */

@Entity
public class Rol extends Base {
    private static long serialVersionUID = 85387603479861808L;

    @NotNull
    @NotEmpty
    @Column(name = "nombre")
    private String nombre;
    
    @ManyToOne
    @JoinColumn
    private Empresa empresa;

    
    public Rol() {

    }

    /**
     * @param id
     *            el id de Rol
     */
    public Rol(Long id) {
            super(id);
    }

    
    
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
}
