/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */

@Entity
public class Rol extends Base {
    private static final long serialVersionUID = 8538760347986185608L;

    @NotNull
    @NotEmpty
    @Column(name = "nombre")
    private String nombre;
    
    
    
}
