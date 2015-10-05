/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directory;

import java.io.IOException;
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
            Directory dir = new Directory();
            System.out.println("Ejecutando monitor de servidores activos");
            Thread monitor= new Thread(dir);
            monitor.start();
            System.out.println("El monitor està ahora vigilando las conecciones");
            System.out.println("Ejecutando publicaciòn de servicios de directorio");
            dir.start();
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
