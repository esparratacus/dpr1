/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import directory.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author david
 */
public class Server {
    public final static String DIRECTORY_IP = "45.55.68.26";
    public final static Integer DIRECTORY_PORT = 6666;
    private final static Integer TIEMPO_RECONEXION = 5000;
    public final static String PALABRA_NO_ENCONTRADA = "[NO ECONTRADA]";
    
    private Service service;
    private HiloDirectory hiloDirectory;
    private HashMap<String,ArrayList<Service>> services;
    private HashMap<String, String> traduccion;
    private HiloServicio hiloServicio;
    
    
    public HashMap<String, ArrayList<Service>> getServices() {
        return services;
    }

    public void setServices(HashMap<String, ArrayList<Service>> services) {
        this.services = services;
    }
    
    
    public HiloDirectory getHiloDirectory() {
        return hiloDirectory;
    }
    public String traducir(String aTraducir){
        String traducc = traduccion.get(aTraducir);
        return traducc  == null? PALABRA_NO_ENCONTRADA : traducc;
    }
    /**
     *
     * @return
     */
    public Service getService() {
        return service;
    }
    

    public Server( String name, String ip, String port){
        service = new Service(name, ip, port);
        traduccion = new HashMap<>();
       
    }
    
    public void start(){
        try{
        File archivo = new File(service.getName());
        cargarServidor(archivo);
        System.out.println("EMPIEZA");
        hiloDirectory = new HiloDirectory(this);
        hiloDirectory.start();
        hiloServicio = new HiloServicio(this);
        hiloServicio.start();
        }catch(Exception e){
            System.out.println("Error al cargar el archivo");
            System.exit(0);
        }
       /* try {
            ServerSocket server = new ServerSocket(Integer.parseInt(getService().getPort()));
            while(true){
                Socket s = server.accept();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
    
    public void cargarServidor(){
        traduccion.put("car", "carro");
        traduccion.put("animal", "animal");
        traduccion.put("eat", "come");
        traduccion.put("food", "comida");
        traduccion.put("night", "noche");
        traduccion.put("sun", "sol");
        traduccion.put("in", "en");
        traduccion.put("the", "el");
        traduccion.put("day", "día");
    }
    public void cargarServidor(File archivo) throws FileNotFoundException, IOException{
        String cadena;
        String[] trad;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null ) {
            trad= cadena.split("\\s");
            traduccion.put(trad[0],trad[1]);
        }
        b.close();
    }
    void setServices(Map<String, String> hashMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
