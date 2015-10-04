/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directory;
import directory.Service;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author david
 */
public class Gatekeeper extends Thread implements Runnable{
    
    private Socket listener;
    private volatile Directory directorio;

    public Gatekeeper(Socket listener, Directory directorio) {
        this.listener = listener;
        this.directorio = directorio;
    }
    
    @Override
    public void run()
    {
        
        try {
            ObjectInputStream oi= new ObjectInputStream(listener.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(listener.getOutputStream());
            Service n = (Service)oi.readObject();
            System.out.println("Estoy recibiendo");
            System.out.println(n.toString());
            this.directorio.addService(n.getIp(), n.getPort(), n.getName());
            directorio.UpdateGatekeepers();
            while(true){
                out.flush();
                out.reset();
                System.out.println("Envío mapa de TAM "+directorio.getServices().size());
                out.writeObject(directorio.getServices());
                synchronized(this){
                    wait();
                }
            
            }
        } catch (IOException ex) {
            System.out.println("Se desconectó un cliente");
            directorio.getGatekeepers().remove(this);
            System.out.println("Ahora hay "+ directorio.getGatekeepers().size()+ "Hilos activos");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
