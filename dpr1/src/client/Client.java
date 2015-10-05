/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import directory.Directory;
import directory.Service;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerTest;

/**
 *
 * @author Gabriel
 */
public class Client {
    public final static int MENSAJE = 1;
    public final static int SALIR = 2;
    private HashMap<String,ArrayList<Service>> services;

    public HashMap<String, ArrayList<Service>> getServices() {
        return services;
    }

    public void setServices(HashMap<String, ArrayList<Service>> services) {
        this.services = services;
    }
    
    public Client(){
        services = new HashMap<>();
    }
    public static void mostrarMenu(){
        System.out.println("**** Seleccione una de las siguientes opciones ****");
        System.out.println("1. Traducir Mensaje");
        System.out.println("2. Salir");
    }
    
    public void mostrarServicios(){
        System.out.println("Servicios Disponibles:");
            ArrayList<Service> servicios;
            for (String key : services.keySet()) {
                System.out.println("    " + key);
            }
    }
    public void actualizarServicios(){
        try {
            HiloObtenerServicios hilo = new HiloObtenerServicios(this);
            hilo.start();
            hilo.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void traducir(String aTraducir, String seleccionado) throws Exception{
        ArrayList<Service> servicios = services.get(seleccionado);
        if(servicios == null){
            throw new Exception("Error al traducir");
        }
        int randomServer = (int) (Math.random() * servicios.size());
        Service s = servicios.get(randomServer);
        Message mensaje = new Message(aTraducir, Message.DISTRIBUIDO);
        Socket socket = new Socket(s.getIp(), Integer.parseInt(s.getPort()));
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(mensaje);
        Message resultado = (Message) ois.readObject();
        System.out.println("Traducción: ");
        System.out.println(resultado.getMessage());
        oos.close();
        ois.close();
        socket.close();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int op= Client.MENSAJE;
        Client cliente = new Client();
        Scanner sc = new Scanner(System.in);
        do{
           cliente.actualizarServicios();
           mostrarMenu();
           op = sc.nextInt();
           switch(op){
               case Client.MENSAJE:
                   System.out.println("escriba el nombre de uno de los servicios disponibles");
                   cliente.mostrarServicios();
                   String seleccionado = sc.nextLine();
                   seleccionado = sc.nextLine();
                   seleccionado = seleccionado.toLowerCase();
                   System.out.println("Escriba la cadena a traducir");
                   String aTraducir = sc.nextLine();
                   try{
                    cliente.traducir(aTraducir, seleccionado);
                   }catch(Exception e){
                       System.out.println("Servicio no Disponible");
                   }
                    break;
               case Client.SALIR:
                   System.out.println("¡Adiós!");
                   System.exit(0);
                   break;
           }
           
           
        }while(op!=Client.SALIR);
    }

    
}
