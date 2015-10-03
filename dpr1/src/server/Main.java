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
        try {
            // TODO code application logic here
            Scanner sc = new Scanner(System.in);
            String linea = sc.nextLine();
            Server server = new Server(linea, InetAddress.getLocalHost().toString(), "4540");
            server.start();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }
    
}
