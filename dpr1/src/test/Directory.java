/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import directory.Service;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author sala-c
 */
public class Directory {
    private volatile ConcurrentMap<String, HashSet<Service>> mapa = new ConcurrentHashMap<>();
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    HashSet<ConexionServidor> conexiones;
   
    public Directory(){
        conexiones = new HashSet<>();
    }

    public ConcurrentMap<String, HashSet<Service>> getMapa() {
        return mapa;
    }

    public void setMapa(ConcurrentMap<String, HashSet<Service>> mapa) {
        this.mapa = mapa;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    public void notificarCambio(){
        Socket s = null;
        try {
            for(ConexionServidor conexion : conexiones){
                s = conexion.getSocket();
                ObjectOutputStream oos = conexion.getOos();
                oos.writeObject(mapa);
                oos.flush();
                oos.reset();
                
            }
        }catch (IOException ex) {
                /* Se desconectó el Socket */
        conexiones.remove(s);
        System.out.println("Ahora hay "+conexiones.size());
        notificarCambio();
        }
    }
    
    public void start() throws IOException{
        ServerSocket server = new ServerSocket(6666);
        ArrayList<HIloConexion> runa = new ArrayList<>();
        while(true){
             System.out.println("Espero conexión...");
             Socket s = server.accept();
             runa.add(new HIloConexion(s,this));
             executorService.execute(runa.get(runa.size()-1));
             ConexionServidor nueva = new ConexionServidor(s);
             conexiones.add(nueva);
             System.out.println("Tamanio "+conexiones.size());
             notificarCambio();
             /* Sería Mejor idea en lugar de guardar los hilos, guardar una refencia a todos los socket que sigan activos */
        }
        
      
    }
}
