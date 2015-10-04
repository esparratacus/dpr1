/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import directory.Service;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author david
 */
public class HiloService extends Thread implements Runnable {
    private Socket socket;
    private Server server; //para obtener la lista que quiero distribuir
    
    public HiloService(Socket s, Server serv){
        socket = s;
        server = serv;
    }
    
    
    @Override
    public void run(){
        // Acá debo distribuir el mensaje entre múltiples servidores, hago join de cada uno de los pedazos y se lo devuelvo al cliente
        HashMap<String, HashSet<Service>> servidoresDisponibles = server.getServices();
        String mensaje = "";
        String[] palabras = mensaje.split(" ");
        int cantidadPalabras = palabras.length;
        while(cantidadPalabras>0){
            // creo un hilo que se conecte con un servidor que le envíe un mensaje y quede pendiente de su respuesta, si está bien, bien, si no se le asigna la responsabilidad a otro

            cantidadPalabras--;
        }
        
    }
    
}
