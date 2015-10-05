package directory;

import client.Message;
import java.io.Serializable;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author esparratacus
 */
public class Service implements Serializable {
    /*
    Nombre del servicio
    */
    private String name;
    /*
    Ip donde se encuentra el servicio
    */
    private String ip;
    /*
    Puerto del servicio
    */
    private String port;

    public Service(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }
    
    @Override
    public String toString()
    {
        return "nombre del Servicio: "+getName()+ " ip:"+getIp();
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Service){
            Service s = (Service) o;
            boolean iguales = s.getIp().equals(ip) && s.getName().equals(name) && s.getPort().equals(port);
            if(iguales){
                System.out.println("Sin iguales");
            }else{
                System.out.println("No son iguales");
            }
            return iguales;
        } 
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.name);
        hash = 73 * hash + Objects.hashCode(this.ip);
        hash = 73 * hash + Objects.hashCode(this.port);
        return hash;
    }
}