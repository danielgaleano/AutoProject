package com.sistem.proyecto.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Miguel
 */
@Entity
public class DetalleVenta extends Base {
    
    private static final long serialVersionUID = 798618560888L;   
    
    @Column(name = "devuelto")
    private boolean devuelto;
    
    @Column(name = "precio")
    private Double precio;
    
    @Column(name = "total")
    private Double total;
    
    @Column(name = "porcentaje_descuento")
    private String porcentajeDescuento;
    
    @Column(name = "monto_descuento")
    private Double montoDescuento;
    
    @Column(name = "neto")
    private Double neto;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda;
    
    @Column(name = "cambio_dia")
    private Double cambioDia;
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;   
    
    @ManyToOne
    @JoinColumn(name = "vehiculo")
    private Vehiculo vehiculo;
    
    @ManyToOne
    @JoinColumn(name = "venta")
    private Venta venta;
    
    @Transient
    private String nroFactura;
    
    public DetalleVenta() {

    }

    /**
     * @param id
     *            el id de Rol
     */
    public DetalleVenta(Long id) {
            super(id);
    }

    /**
     * @return the precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * @return the porcentajeDescuento
     */
    public String getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    /**
     * @param porcentajeDescuento the porcentajeDescuento to set
     */
    public void setPorcentajeDescuento(String porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * @return the MontoDescuento
     */
    public Double getMontoDescuento() {
        return montoDescuento;
    }

    /**
     * @param montoDescuento the MontoDescuento to set
     */
    public void setMontoDescuento(Double montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    /**
     * @return the neto
     */
    public Double getNeto() {
        return neto;
    }

    /**
     * @param neto the neto to set
     */
    public void setNeto(Double neto) {
        this.neto = neto;
    }

    /**
     * @return the moneda
     */
    public Moneda getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the cambioDia
     */
    public Double getCambioDia() {
        return cambioDia;
    }

    /**
     * @param cambioDia the cambioDia to set
     */
    public void setCambioDia(Double cambioDia) {
        this.cambioDia = cambioDia;
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
     * @return the vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * @param vehiculo the vehiculo to set
     */
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    /**
     * @return the venta
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
    
    
    
}
