/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    public void run(){
        
        System.out.println("Estableciendo conexión con el directorio...");
        try {
            conexion = new Socket(Server.DIRECTORY_IP,Server.DIRECTORY_PORT);
            ObjectOutputStream out =new ObjectOutputStream(conexion.getOutputStream());
            BufferedReader in = new BufferedReader( new InputStreamReader(conexion.getInputStream()));
            System.out.println("Intento de envío");
            out.writeObject(server.getService());
            System.out.println("Envía datos");
        } catch (IOException ex) {
            ex.printStackTrace(); // tengo que re intentar que se conecte
            server.resetDirectory();// intento que se vuelva a correr de nuevo
        }
        while(true){
            
            try {
               wait_();
               
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloDirectory.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            if(conexion.isConnected()){
                
            }else{
               //validar reconexión
            }
        }
        
    }
    
}

