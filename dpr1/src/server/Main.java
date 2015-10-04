/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el nombre del servicio");
        String linea = sc.nextLine();
        System.out.println("Ingrese la dirección IP");
        String ip = sc.nextLine();
        System.out.println("Ingrese el puerto");
        String puerto = sc.nextLine();
        Server server = new Server(linea, ip, puerto);
        server.start();
    }
    
}
