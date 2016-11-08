package com.sistem.proyecto.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 * @author mojeda
 *
 */
@Entity
public class Empleo extends Base {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "nombre_empresa", nullable = false, length = 128)
    private String nombreEmpresa;

    @Column(name = "cargo", nullable = false)
    private String cargo;
    
    @Column(name = "antiguedad", nullable = false)
    private String antiguedad;
    
    @Column(name = "salario")
    private String salario;
    
    @Column(name = "telefono")
    private String telefono;    
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "actividad")
    private String actividad;

    @Column(name = "comentario")
    private String comentario;
    

    public Empleo() {
    }

    public Empleo(Long id) {
        setId(id);
    }

    

    /**
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo the cargo to set
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the antiguedad
     */
    public String getAntiguedad() {
        return antiguedad;
    }

    /**
     * @param antiguedad the antiguedad to set
     */
    public void setAntiguedad(String antiguedad) {
        this.antiguedad = antiguedad;
    }

    /**
     * @return the salario
     */
    public String getSalario() {
        return salario;
    }

    /**
     * @param salario the salario to set
     */
    public void setSalario(String salario) {
        this.salario = salario;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the actividad
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * @param actividad the actividad to set
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * @return the nombreEmpresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * @param nombreEmpresa the nombreEmpresa to set
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    
}
