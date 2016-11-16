/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sistem.proyecto.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author daniel
 */
@Entity
public class PagoProveedor extends Base {
    
    @ManyToOne
    @JoinColumn(name = "documento_pagar")
    private DocumentoPagar documentoPagar;
    
    @Column(name = "monto_pago")
    private String montoPago;
    
    @Column(name = "monto_pendiente")
    private String montoPendiente;
    
    @Column(name = "fecha_pago")
    private Timestamp fechaPago;
    
    @Column(name = "medio_pago")
    private String medioPago;
    
    
    /**
     * @return the documentoPagar
     */
    public DocumentoPagar getDocumentoPagar() {
        return documentoPagar;
    }

    /**
     * @param documentoPagar the documentoPagar to set
     */
    public void setDocumentoPagar(DocumentoPagar documentoPagar) {
        this.documentoPagar = documentoPagar;
    }
    
    /**
     * @return the montoPago
     */
    public String getMontoPago() {
        return montoPago;
    }

    /**
     * @param montoPago the montoPago to set
     */
    public void setMontoPago(String montoPago) {
        this.montoPago = montoPago;
    }
    
    /**
     * @return the montoPendiente
     */
    public String getMontoPendiente() {
        return montoPendiente;
    }

    /**
     * @param montoPendiente the montoPendiente to set
     */
    public void setMontoPendiente(String montoPendiente) {
        this.montoPendiente = montoPendiente;
    }
    
    /**
     * @return the fechaPago
     */
    public Timestamp getFechaPago() {
        return fechaPago;
    }

    /**
     * @param fechaPago the fechaPago to set
     */
    public void setFechaPago(Timestamp fechaPago) {
        this.fechaPago = fechaPago;
    }    
  
    /**
     * @return the medioPago
     */
    public String getMedioPago() {
        return medioPago;
    }

    /**
     * @param medioPago the medioPago to set
     */
    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }
    
}
