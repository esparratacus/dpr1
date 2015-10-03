/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import directory.Service;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sala-c
 */
public class Run implements Runnable{
    private Socket s;
    private Dir dir;
    public Run(Socket s, Dir d){
        this.s = s;
        dir = d;
    }

   @Override
public void run() {
    System.out.println("Tarea asícrona, voy a modificar a la varibale");
  
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
            dir.getMapa().put(hashCode()+"", new ArrayList<Service>());
            System.out.println("Soy UN HILO y TAM es "+dir.getMapa().size());
 
    }
}

