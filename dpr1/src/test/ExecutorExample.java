/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sala-c
 */
public class ExecutorExample {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Integer variableCompartida;
    public ExecutorExample(){
        variableCompartida = 15;
    }
    
    public void ejecutar(){
        for (int i = 0; i < 15; i++) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Tarea asícrona, voy a modificar a la varibale");
                while(true){
                    if(variableCompartida%3==0){
                        variableCompartida+=10;
                    }else{
                        variableCompartida=variableCompartida/2;
                    }
                }
            }
        });
        
        while(true){
            try {
                Thread.sleep(500);
                System.out.println("VAR: "+ variableCompartida);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExecutorExample.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
        
        
    }
    
}
