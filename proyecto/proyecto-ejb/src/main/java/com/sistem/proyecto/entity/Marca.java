/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */
@Entity
public class Marca extends Base{
    
    private static long serialVersionUID = -959680520407250259L;
    
    @NotNull
    @NotEmpty
    @Column(name = "nombre")
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marca")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Modelo> modeloCollection;
    
    
    public Marca() {

    }

    public Marca(Long id) {
            this.setId(id);
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the modeloCollection
     */
    public Collection<Modelo> getModeloCollection() {
        return modeloCollection;
    }

    /**
     * @param modeloCollection the modeloCollection to set
     */
    public void setModeloCollection(Collection<Modelo> modeloCollection) {
        this.modeloCollection = modeloCollection;
    }
    
    
}
