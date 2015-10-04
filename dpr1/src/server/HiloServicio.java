/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class HiloServicio extends Thread implements Runnable {
    private volatile Server server;
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    
    public HiloServicio(Server server){
        this.server = server;
    }
    
    
    @Override
    public void run(){

        try {
            ServerSocket sSocket = new ServerSocket(Integer.parseInt(server.getService().getPort()));
            while(true){
                Socket socket;
                socket = sSocket.accept();
                /* Hilo Respuesta es el encargado de Responderle al Cliente */
                HiloRespuesta respuesta = new HiloRespuesta(socket, server);
                respuesta.start();  
            }
            
        } catch (IOException ex) {
            Logger.getLogger(HiloServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
