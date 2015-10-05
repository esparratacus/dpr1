/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.Message;
import directory.Service;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author david
 */
public class HiloRespuesta extends Thread implements Runnable{
    private Socket socket;
    private Server server;
    public HiloRespuesta(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }
    
    
    public Future distribuirMensaje(final Message nuevo, final Service escogido, ExecutorService executorService){
       return   executorService.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                        Message message = nuevo;
                        Socket socket = new Socket(escogido.getIp(), Integer.parseInt(escogido.getPort()));
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        oos.writeObject(message);
                        Message respuesta =(Message) ois.readObject();
                        ois.close();
                        oos.close();
                        socket.close();
                        return respuesta;
                    }
            });
    }
    
    
    @Override
    public void run(){
        try {
            /* Leo el mensaje que me llega */
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Message message = (Message) ois.readObject();
            switch(message.getTipo()){
                case Message.DISTRIBUIDO:
                    System.out.println("LLEGA un mensaje para distribuir");
                    /* Debo mandar el mensaje por diferentes servidores para que cada uno haga lo que debe */
                    String[] palabras = message.getMessage().split(" ");
                    Future[] futuros = new Future[palabras.length];
                    Message[] respuesta = new Message[palabras.length];
                    ArrayList<Service> disponibles;
                    if(server.getServices() ==  null){
                        System.out.println("MIERDA HORRIBLE NO ACTUALIZA EL MAPA");
                    }
                    disponibles = server.getServices().get(server.getService().getName());
                    ExecutorService executorService = Executors.newFixedThreadPool(100);
                    String palabra;
                    for (int i = 0; i < palabras.length ; i++) {
                        palabra = palabras[i];
                        /* Creo un Mensaje nuevo de tipo traducción, el cual se le va a enviar a un servidor */
                        final Message nuevo = new Message(palabra, Message.TRADUCCION);
                        nuevo.setSec(i);
                        /* A cada uno de los disponibles le asigno una palabra */
                        final int servidorEscogido =(int) ((Math.random()*disponibles.size())% disponibles.size());
                        final Service escogido = disponibles.get(servidorEscogido);
                        futuros[i] =  distribuirMensaje(nuevo, escogido, executorService);
                    }
                    
                    String mensajeCompleto = "";
                    for (int i = 0; i < futuros.length; i++) {
                        if(futuros[i].isDone()){
                            try {
                                respuesta[i] = (Message) futuros[i].get();
                                mensajeCompleto += respuesta[i].getMessage() + " ";
                                System.out.println("Parte del mensaje  "+  respuesta[i].getMessage());
                            
                            }catch (Exception ex) {
                                /* Toca Reenviar el Mensaje i */
                                final Message nuevo = new Message(palabras[i], Message.TRADUCCION);
                                final int servidorEscogido =(int) ((Math.random()*disponibles.size())% disponibles.size());
                                disponibles = server.getServices().get(server.getService().getName()); // traigo a todos mis pares
                                final Service escogido = disponibles.get(servidorEscogido);
                                futuros[i] = distribuirMensaje(nuevo, escogido, executorService);
                                i--;
                            }
                        }else{
                            i--; /* No está Listo */
                        } 
                    }
                    Message fin = new Message(mensajeCompleto, Message.RESPUESTA);
                    oos.writeObject(fin);
                    oos.close();
                    ois.close();
                    socket.close();
                    executorService.shutdown();
                    break;
                case Message.TRADUCCION:
                    System.out.println("LLEGA un mensaje para traducir");
                    String palabraTraducida = server.traducir(message.getMessage());
                    /* Debo traducir y retornar la cadena apenas pueda */
                    Message nuevo = new Message(palabraTraducida, Message.RESPUESTA);
                    oos.writeObject(nuevo);
                    oos.close();
                    socket.close();
                    break;
                default:
                    /* paila, no funciona */
                    Message error = new Message(null, Message.NO_DISPONIBLE);
                    oos.writeObject(error);
                    oos.close();
                    socket.close();
                    System.out.println("Operación NO sopoertada");
            }
            
            
        } catch (IOException ex) {
            System.out.println("Se perdió la conexión con el cliente, muere el hilo");
        } catch (ClassNotFoundException ex) {
            System.out.println("El cliente mandó algo bien raro");
        }
        
        
    }
}
