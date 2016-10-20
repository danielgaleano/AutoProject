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
    
    //public static final String APROBADO = "APROBADO";
    //public static final String PENDIENTE = "PENDIENTE";
    //public static final String RECHAZADO = "RECHAZADO";
    
    //@EmbeddedId
    //private DetalleVentaPK detalleVentaPK;
    
    //@MapsId("serialVersionUID")
    //@ManyToOne
    //private Venta venta;
    
    @ManyToOne
    @JoinColumn(name = "venta")
    private Venta venta;
    
    @OneToOne(optional = false)
    @JoinColumn(name = "vehiculo", referencedColumnName = "id")
    private Vehiculo vehiculo;
    
    @NotNull
    @Column(name = "cantidad")
    private Long cantidad;
    
    @NotNull
    @Column(name = "precio")
    private Double precio;
    
    
    @Column(name = "cambio_dia")
    private Double cambioDia;
    
    @NotNull
    @Column(name = "total")
    private Double total;
    
    @ManyToOne
    @JoinColumn(name = "moneda")
    private Moneda moneda; 
    
    @ManyToOne
    @JoinColumn(name = "empresa")
    private Empresa empresa;
    
    @Transient
    private String fecha;
    
    //@ManyToOne
    //@JoinColumn(name = "venta")
    //private Venta venta;
    
    public DetalleVenta() {

    }

    /**
     * @param ventaId
     * @param ordenId 
     *            
     */
    public DetalleVenta(Long id ) {
            super(id);
            //detalleVentaPK = new DetalleVentaPK(ventaId, ordenId);
    }

//    public DetalleVentaPK getVentaDetallePK() {
//        return detalleVentaPK;
//    }
//
//    public void setDetalleVentaPK(DetalleVentaPK detalleVentaPK) {
//        this.detalleVentaPK = detalleVentaPK;
//    }
    
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
     * @return the cantidad
     */
    public Long getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

   
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
     * @return the pedido
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
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }    
    
}
