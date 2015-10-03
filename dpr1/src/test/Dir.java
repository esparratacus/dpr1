/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import directory.Service;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author sala-c
 */
public class Dir {
    private volatile ConcurrentMap<String, ArrayList<Service>> mapa = new ConcurrentHashMap<String, ArrayList<Service>>();
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    public Dir(){
        
    }

    public ConcurrentMap<String, ArrayList<Service>> getMapa() {
        return mapa;
    }

    public void setMapa(ConcurrentMap<String, ArrayList<Service>> mapa) {
        this.mapa = mapa;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    
    
    public void start() throws IOException{
        ServerSocket server = new ServerSocket(6666);
        ArrayList<Run> runa = new ArrayList<Run>();
        while(true){
             System.out.println("Espero conexión...");
             Socket s = server.accept();
             runa.add(new Run(s,this));
             executorService.execute(runa.get(runa.size()-1));
             System.out.println("Tamanio "+mapa.size());
             for (Run runables : runa) {
                 executorService.execute(runables);
             }
             /* Recorrero los runnable de cada uno y despertar */
        }
        
      
    }
}
