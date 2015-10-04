/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.Serializable;

/**
 *
 * @author david
 */
public class Message implements Serializable{
    public final static String NO_DISPONIBLE = "no disponible";
    public final static String RESPUESTA = "respuesta";
    public final static String DISTRIBUIDO = "distribuido";
    public final static String TRADUCCION = "traducción";
    
    private String message;
    private String tipo;
    private Integer sec;

    public Integer getSec() {
        return sec;
    }

    public void setSec(Integer sec) {
        this.sec = sec;
    }
    
    public Message(String message, String tipo) {
        this.message = message;
        this.tipo = tipo;
        sec = 0;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
