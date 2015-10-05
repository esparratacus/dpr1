/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class HiloCliente extends Thread implements Runnable{
    private final static int PORT = 3333;
    private Directory dir;
    
    public HiloCliente(Directory directorio){
        dir = directorio;
    }
    
    @Override
    public void run(){
        try {
            ServerSocket server = new ServerSocket(PORT);
            while(true){
                Socket s = server.accept();
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(dir.getServices());
                oos.close();
                s.close();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
