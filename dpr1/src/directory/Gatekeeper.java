/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author esparratacus
 */
public class Gatekeeper extends Thread implements Runnable {
    
    private Socket listener;
    private Directory directorio;

    public Gatekeeper(Socket listener, Directory directorio) {
        this.listener = listener;
        this.directorio = directorio;
    }
    
    public void run()
    {
        
        try {
            ObjectInputStream oi= new ObjectInputStream(this.listener.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(this.listener.getOutputStream());
            Service n = (Service)oi.readObject();
            System.out.println("Estoy recibiendo");
            System.out.println(n.toString());
            this.directorio.addService(n.getIp(), n.getPort(), n.getName());
            System.out.println("El servicio "+n.getName()+" ha sido agregado correctamente al directorio");
            while(true)
            {
                out.writeObject(directorio.getServices());
                System.out.println("tamaño de servicios: "+ directorio.getServices().size());
                synchronized(this)
                {
                    wait();
                }
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
