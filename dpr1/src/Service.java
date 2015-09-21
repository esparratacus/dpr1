/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author esparratacus
 */
public class Service {
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
    
  
}
