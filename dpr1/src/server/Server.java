/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import directory.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author david
 */
public class Server {
    public final static String DIRECTORY_IP = "10.5.53.210";
    public final static Integer DIRECTORY_PORT = 6666;
    private final static Integer TIEMPO_RECONEXION = 5000;
    private HashMap<String,ArrayList<Service>> services;

    public HashMap<String, ArrayList<Service>> getServices() {
        return services;
    }

    public void setServices(HashMap<String, ArrayList<Service>> services) {
        this.services = services;
    }
    
    
    public HiloDirectory getHiloDirectory() {
        return hiloDirectory;
    }
    public static void notify(Thread t){
        t.notifyAll();
    }
    private Service service;
    HiloDirectory hiloDirectory;

    public Service getService() {
        return service;
    }
    
    
    public void resetDirectory(){
        try {
            Thread.sleep(TIEMPO_RECONEXION);
            System.out.println("Se ha perdido la conexión con el directorio, re intentando en "+(double) TIEMPO_RECONEXION/1000 +" segundos");
            hiloDirectory.interrupt();
            hiloDirectory.start();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        hiloDirectory.interrupt();
        hiloDirectory.start();
    }
    public Server( String name, String ip, String port){
        service = new Service(name, ip, port);
       
    }
    
    public void start(){
        System.out.println("EMPIEZA");
        hiloDirectory = new HiloDirectory(this);
        hiloDirectory.start();
                
        
    }
}
