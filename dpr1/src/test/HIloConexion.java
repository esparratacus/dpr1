/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import directory.Service;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sala-c
 */
public class HIloConexion implements Runnable{
    private  Socket s;
    
    private Directory dir;
    public HIloConexion(Socket s, Directory d){
        this.s = s;
        dir = d;
    }

   @Override
public void run() {
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(s.getInputStream());
            Service nuevo = (Service) oin.readObject();
            HashSet<Service> conjunto = dir.getMapa().get(nuevo.getName());
            if(conjunto == null){
                conjunto = new HashSet<>();
                conjunto.add(nuevo);
                dir.getMapa().put(nuevo.getName(), conjunto);
            }else{
                conjunto.add(nuevo);
            }
        } catch (IOException ex) {
            Logger.getLogger(HIloConexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HIloConexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Class not found");
        }
    }
}

