/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import directory.Service;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class HiloDirectory extends Thread implements Runnable{
    private Socket conexion;
    private Server server;
    public HiloDirectory(Server server){
        this.server = server;
    }
    
    private synchronized void wait_() throws InterruptedException{
       
         wait();
         System.out.println("Fin de la espera");
    }
    
    public void leerServidores(ObjectInputStream in) throws IOException, ClassNotFoundException{
        System.out.println("Respuesta del Servidor:");
        
        ConcurrentHashMap<String, ArrayList<Service>> mapa  = (ConcurrentHashMap<String, ArrayList<Service>>) in.readObject();
        ArrayList<Service> servicios;
        for (String key : mapa.keySet()) {
                servicios = mapa.get(key);
                System.out.println("Servicio "+ key);
                for (Service servicio : servicios) {
                    System.out.println("    "+ servicio.getIp() + ":"+ servicio.getPort());
                }
        }
        HashMap<String,ArrayList<Service>> hashMap = new HashMap<>(mapa);
        server.setServices(hashMap);
    }
    
    @Override
    public void run(){
        
        System.out.println("Estableciendo conexión con el directorio...");
        try {
            conexion = new Socket(Server.DIRECTORY_IP,Server.DIRECTORY_PORT);
            ObjectOutputStream out =new ObjectOutputStream(conexion.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(conexion.getInputStream());
            
            System.out.println("Intento escribir en el objeto");
            out.writeObject(server.getService());
            
            while(true){// leo el objeto mapa
                leerServidores(in);
            }
                
        } catch (IOException ex) {
            ex.printStackTrace(); // tengo que re intentar que se conecte
            try {
                System.out.println("Se perdió la conexión con el directorio, re intentando en 2 segundos");
                Thread.sleep(2000);
                run();
            } catch (InterruptedException ex1) {
                Logger.getLogger(HiloDirectory.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            try {
                System.out.println("Se perdió la conexión con el directorio, re intentando en 2 segundos");
                Thread.sleep(2000);
                run();
            } catch (InterruptedException ex1) {
                Logger.getLogger(HiloDirectory.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
            try {
                conexion.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloDirectory.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
    }
    
}

