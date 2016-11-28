package com.sistem.proyecto.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Miguel Ojeda
 *
 */
@Entity
public class Vehiculo extends Base {

    /**
     *
     */
    public static final String PENDIENTE = "PENDIENTE";
    public static final String MANTENIMIENTO = "MANTENIMIENTO";
    public static final String STOCK = "STOCK";
    public static final String VENDIDA = "VENDIDA";
    
    private static final long serialVersionUID = 1L;

    @Column(name = "codigo")
    private String codigo;
    
    @ManyToOne
    @JoinColumn(name = "tipo")
    private Tipo tipo;
    
    @ManyToOne
    @JoinColumn(name = "marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "modelo")
    private Modelo modelo;

    @NotNull
    @NotEmpty
    @Column(name = "transmision")
    private String transmision;

    @NotNull
    @NotEmpty
    @Column(name = "color")
    private String color;

    @NotNull
    @NotEmpty
    @Column(name = "anho")
    private String anho;
    
    @Column(name = "caracteristica")
    private String caracteristica;

    @ManyToOne
    @JoinColumn(name = "proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Column(name = "chapa")
    private String chapa;
    
    @Column(name = "chasis")
    private String chasis;
    
    @Column(name = "motor")
    private String motor;
    
    @Column(name = "kilometraje")
    private String kilometraje;
    
    @Column(name = "precio_venta")
    private Double precioVenta;
    
    @Column(name = "precio_costo")
    private Double precioCosto;
    
    @Column(name = "precio_mantenimiento")
    private Double precioMantenimiento;
    
    @Column(name = "fecha_matenimiento")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaMantenimiento;
    
    @Column(name = "cedula_verde")
    private Boolean cedulaVerde;
    
    @Column(name = "titulo")
    private Boolean titulo;
    
    @Column(name = "estado")
    private String estado;

    public Vehiculo() {
    }

    public Vehiculo(Long id) {
        setId(id);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    
    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
     public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }
    
     public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }
    
    /**
     * @return la empresa del pedido
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa la empresa del pedido
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the tipo
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    
    public String getChapa() {
        return chapa;
    }

    public void setChapa(String chapa) {
        this.chapa = chapa;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }
    
    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }
    
    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }
    
    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public Date getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(Date fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }
    
    public Boolean getCedulaVerde() {
        return cedulaVerde;
    }

    public void setCedulaVerde(Boolean cedulaVerde) {
        this.cedulaVerde = cedulaVerde;
    }
    
    public Boolean getTitulo() {
        return titulo;
    }

    public void setTitulo(Boolean titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(Double precioCosto) {
        this.precioCosto = precioCosto;
    }

    public Double getPrecioMantenimiento() {
        return precioMantenimiento;
    }

    public void setPrecioMantenimiento(Double precioMantenimiento) {
        this.precioMantenimiento = precioMantenimiento;
    }
    
    

}
