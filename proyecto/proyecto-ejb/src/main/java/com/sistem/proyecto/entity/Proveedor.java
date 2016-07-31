package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author daniel
 *
 */
@Entity
public class Proveedor extends Base {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "nombre", nullable = false, length = 128)
    private String nombre;

    @Column(name = "ruc", nullable = false)
    private String ruc;
    
    @Column(name = "codigo_postal")
    private String codigoPostal;
    
    @Column(name = "fax")
    private String fax;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "pais")
    private String pais;
    
    @Column(name = "ciudad")
    private String ciudad;
    
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "comentario")
    private String comentario;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "telefono_movil")
    private String telefonoMovil;
    
    @ManyToOne
    @JoinColumn(name = "contacto")
    private Contacto contacto;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Transient
    private String nombreContacto;
    
    @Transient
    private String telefonoContacto;
    
    @Transient
    private String contactoCargo;
    
    @Transient
    private String contactoEmail;
    
    @Transient
    private String contactoComentario;
    
    @Transient
    private boolean tieneContacto;
    
    @Transient
    private Long idContacto;

    public Proveedor() {
    }

    public Proveedor(Long id) {
        setId(id);
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

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return the ruc
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * @param ruc the ruc to set
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public String getContactoCargo() {
        return contactoCargo;
    }

    public void setContactoCargo(String contactoCargo) {
        this.contactoCargo = contactoCargo;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    public String getContactoComentario() {
        return contactoComentario;
    }

    public void setContactoComentario(String contactoComentario) {
        this.contactoComentario = contactoComentario;
    }

    public boolean isTieneContacto() {
        return tieneContacto;
    }

    public void setTieneContacto(boolean tieneContacto) {
        this.tieneContacto = tieneContacto;
    }

    public Long getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Long idContacto) {
        this.idContacto = idContacto;
    }
    
    

}
