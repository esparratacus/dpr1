/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        String linea = sc.nextLine();
        Server server = new Server("Linea", "localhost", "4540");
        server.start();
        
        
       
        /*
        while(true){
        try {
            Thread.sleep(3000);
            synchronized(server.getHiloDirectory()){
               server.getHiloDirectory().notify();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
                
               
        }*/
    }
    
}
