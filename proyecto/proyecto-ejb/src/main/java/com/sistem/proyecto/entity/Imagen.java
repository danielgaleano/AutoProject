/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 *
 * @author Miguel
 */
@Entity
public class Imagen extends Base{
    /**
    * 
    */
   private static final long serialVersionUID = 8043955998599228015L;

   @Lob
   @Column(name = "imagen")
   private byte[] imagen;

   @Column(name = "nombreImagen")
   private String nombreImagen;

   @Column(name = "nombreTabla")
   private String nombreTabla;

   @Column(name = "entidadId")
   private Long entidadId;

   @ManyToOne
   @JoinColumn(name = "empresa")
   private Empresa empresa;


   /**
    * Constructor vacío
    */
   public Imagen() {

   }

   /**
    * @param id
    *            el id de Imagen
    */
   public Imagen(Long id) {
           super(id);
   }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(Long entidadId) {
        this.entidadId = entidadId;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
   
   
}
