/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import directory.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
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
        System.out.println("Esperando por la respuesta del ");
        Map<String, ArrayList<Service>> mapa  = (Map<String, ArrayList<Service>>) in.readObject();
        for (String key : mapa.keySet()) {
               ArrayList<Service> servicios = mapa.get(key);
               for (Service servicio : servicios) {
                   System.out.println("Servicio "+servicio.getName());
                }
        }
    }
    
    @Override
    public void run(){
        
        System.out.println("Estableciendo conexión con el directorio...");
        try {
            conexion = new Socket(Server.DIRECTORY_IP,Server.DIRECTORY_PORT);
            ObjectOutputStream out =new ObjectOutputStream(conexion.getOutputStream());
            System.out.println("Intento de envío");
            out.writeObject(server.getService());
            System.out.println("Envía datos");
            ObjectInputStream in = new ObjectInputStream(conexion.getInputStream());
            Service servicio  = server.getService();
            out.writeObject(servicio);
            
            while(true){// leo el objeto mapa
                leerServidores(in);
            }
                
        } catch (IOException ex) {
            ex.printStackTrace(); // tengo que re intentar que se conecte
            server.resetDirectory();// intento que se vuelva a correr de nuevo
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
}

