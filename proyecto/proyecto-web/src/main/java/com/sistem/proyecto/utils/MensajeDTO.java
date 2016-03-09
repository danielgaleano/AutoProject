/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.utils;

/**
 *
 * @author Miguel
 */
public class MensajeDTO {
    
    private boolean error;
    
    private String menasje;
    
    public MensajeDTO() {
            super();
    }

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the menasje
     */
    public String getMenasje() {
        return menasje;
    }

    /**
     * @param menasje the menasje to set
     */
    public void setMenasje(String menasje) {
        this.menasje = menasje;
    }
    
    
}
