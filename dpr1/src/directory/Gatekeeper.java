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
    private Service myService;

    public Socket getListener() {
        return listener;
    }

    public void setListener(Socket listener) {
        this.listener = listener;
    }

    public Directory getDirectorio() {
        return directorio;
    }

    public void setDirectorio(Directory directorio) {
        this.directorio = directorio;
    }

    public Service getMyService() {
        return myService;
    }

    public void setMyService(Service myService) {
        this.myService = myService;
    }
    
    

    public Gatekeeper(Socket listener, Directory directorio) {
        this.listener = listener;
        this.directorio = directorio;
        myService = null;
    }
    
    @Override
    public void run()
    {
        
        try {
            ObjectInputStream oi= new ObjectInputStream(listener.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(listener.getOutputStream());
            Service n = (Service)oi.readObject();
            myService = n;
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
            listener = null;
            directorio.getServices().get(myService.getName()).remove(myService);
            if(directorio.getServices().get(myService.getName()).isEmpty()){
                System.out.println("Lista vacía BORRO" );
                directorio.getServices().remove(myService.getName());
            }
            directorio.getServices();
            directorio.UpdateGatekeepers();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Gatekeeper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
