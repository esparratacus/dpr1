/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import directory.Service;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author david
 */
public class Server {
    public final static String DIRECTORY_IP = "localhost";
    public final static Integer DIRECTORY_PORT = 6666;
    private final static Integer TIEMPO_RECONEXION = 5000;
    
    private Service service;
    HiloDirectory hiloDirectory;
    private HashMap<String,HashSet<Service>> services;
    private HashMap<String, String> traduccion;

    public HashMap<String, HashSet<Service>> getServices() {
        return services;
    }

    public void setServices(HashMap<String, HashSet<Service>> services) {
        this.services = services;
    }
    
    
    public HiloDirectory getHiloDirectory() {
        return hiloDirectory;
    }
    public static void notify(Thread t){
        t.notifyAll();
    }
    

    public Service getService() {
        return service;
    }
    

    public Server( String name, String ip, String port){
        service = new Service(name, ip, port);
       
    }
    
    public void start(){
        System.out.println("EMPIEZA");
        hiloDirectory = new HiloDirectory(this);
        hiloDirectory.start();
       /* try {
            ServerSocket server = new ServerSocket(Integer.parseInt(getService().getPort()));
            while(true){
                Socket s = server.accept();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
}
