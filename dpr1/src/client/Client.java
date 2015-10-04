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
    static Scanner scanner = new Scanner(System.in); 
    static int select = -1; 
    private String directorio="45.55.68.26";
    private int puerto=3333;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
       	String palabras= new String();
        String opcion= new String();
        Message mes=new Message();
        Directory dir = null;
        String servi=new String();
      
        // servicios=dir.getServices():
        System.out.println("Bienvenido al servicio de traducción");
		while(select != 0){
	
			try{
				System.out.println("Elige opción:\n1.- Palabras" +
						"\n0.- Salir\n" );
				
				select = Integer.parseInt(scanner.nextLine()); 
	
				switch(select){
				case 1: 
                            		System.out.println("Ingrese Porfavor la(s) palabra(s) a traducir: ");
                                        palabras= scanner.nextLine();
                                        System.out.println("Desea ingresar mas palabras? (S/N): ");
                                        opcion = scanner.nextLine();
                                        if(opcion.equalsIgnoreCase("S"))
                                        {
                                            System.out.println("Ingrese Porfavor la(s) palabra(s) a traducir: ");
                                            palabras.concat(scanner.nextLine());
                                            mes.setMessage(palabras);
                                 
                                        }else if(opcion.equalsIgnoreCase("N"))
                                        {
                                            mes.setMessage(palabras);
                                            
                                        }
                                        //despues se supone le muestra los idiomas disponibles 
                                        System.out.println("Seleccione idioma para traducir: "/* + /*variable para mostrar los servicios*/);
                                        servi=scanner.nextLine();
                                        
                                       /* try {
                                           Message message = new Message("day night food in the sun", Message.DISTRIBUIDO);
                                            Socket socket = new Socket("45.55.68.26", 3333);
                                            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                                            ConcurrentHashMap<String, ArrayList<Service>> mapa  = (ConcurrentHashMap<String, ArrayList<Service>>) ois.readObject();
            
                                                System.out.println("Me llega el mapa de tam");
                                                ArrayList<Service> servicios;
                                            for (String key : mapa.keySet()) {
                                                servicios = mapa.get(key);
                                                   System.out.println("Servicio "+ key);
                                                    for (Service servicio : servicios) {
                                                         System.out.println("    "+ servicio.getIp() + ":"+ servicio.getPort());
                                                    }
                                            }
                                            /* 
                                                 oos.writeObject(message);
           
                                            Message respuesta =(Message) ois.readObject();
                                            System.out.println("Traducción es:" +respuesta.getMessage());
                                            ois.close();
                                            oos.close();
                                            socket.close();
                                            */
            
                                    /*} catch (IOException ex) {
                                        Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
                                    }*/
                                        
					break;
				case 0: 
					System.out.println("Adios!");
					break;
				default:
					System.out.println("Número no reconocido");break;
				}
				
				System.out.println("\n"); //Mostrar un salto de línea en Java
				
			}catch(Exception e){
				System.out.println("Uoop! Error!");
			}
		}

	}
}
