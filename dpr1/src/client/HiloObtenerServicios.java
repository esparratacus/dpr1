/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

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
import server.Server;
import server.ServerTest;

/**
 *
 * @author david
 */
public class HiloObtenerServicios extends Thread implements Runnable {
    private Client client;

    public HiloObtenerServicios(Client client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 3333);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ConcurrentHashMap<String, ArrayList<Service>> mapa = (ConcurrentHashMap<String, ArrayList<Service>>) ois.readObject();
            HashMap<String,ArrayList<Service>> hashMap = new HashMap<>(mapa);
            client.setServices(hashMap);
            ois.close();
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
