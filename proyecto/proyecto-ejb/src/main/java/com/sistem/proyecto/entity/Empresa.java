/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author daniel
 */
@Entity
public class Empresa extends Base{
    
    private static final long serialVersionUID = 79861856088L;

    @NotNull
    @NotEmpty
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "ruc")
    private String ruc;
    
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "telefono_movil")
    private String telefonoMovil;
    
    @Column(name = "nombre_contacto")
    private String nombreContacto;
    
    @Column(name = "telefono_contacto")
    private String telefonoContacto;
    
    @Column(name = "telefono_movil_contacto")
    private String telefonoMovilContacto;

    @Column(name = "email")
    private String email;

    
    public Empresa() {

    }

    public Empresa(Long id) {
            super(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getTelefonoMovilContacto() {
        return telefonoMovilContacto;
    }

    public void setTelefonoMovilContacto(String telefonoMovilContacto) {
        this.telefonoMovilContacto = telefonoMovilContacto;
    }
    
    
}
