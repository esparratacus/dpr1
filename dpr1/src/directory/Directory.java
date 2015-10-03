package directory;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author esparratacus
 */
public class Directory {
    
    private static String ARCHIVO="directorio.ermi";
    private Map<String,ArrayList<Service>> services=new  HashMap<>();
    private ArrayList<String> servers;
    private ServerSocket server;
    private ArrayList<Gatekeeper> gatekeepers;

    public Directory() {
        
        this.services = services;
        this.servers= new ArrayList<>();
        this.gatekeepers=new ArrayList<>();
        
    }
    
    public void start() throws IOException 
    {
        this.server = new ServerSocket(6666);
        while(true)
        {
            try{
                
                Socket actual;
                actual = this.server.accept();
                Gatekeeper gk = new Gatekeeper(actual, this);
                gk.start();
                this.gatekeepers.add(gk);
                System.out.println("Alguien se ha conectado");
                for (int i = 0; i < this.gatekeepers.size(); i++) {
                    Gatekeeper g= gatekeepers.get(i);
                    synchronized(g){
                        g.notify();
                    }
                    
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    /*
    Método que retorna el hashmap con los servicios disponibles
    */
    public synchronized Map<String, ArrayList<Service>> getServices() throws FileNotFoundException, IOException, ClassNotFoundException {
       // Map<String,ArrayList<Service>> syncServices= Collections.synchronizedMap(this.services);
        File f= new File(ARCHIVO);
                if(!f.exists())
                {
                    f.createNewFile();
                }
        if(f.length()<5)
        {
            return services;
        }
        FileInputStream fin = new FileInputStream(ARCHIVO);
        ObjectInputStream oin = new ObjectInputStream(fin);
        this.services = (Map)oin.readObject();
        oin.close();
        fin.close();
        System.out.println("tamanio arch:"+ f.length());
        return services;
    }

    /*public void setServices(HashMap<String, ArrayList<Service>> services) {
        this.services = services;
    }
    /*
    Método que agrega un servicio al directorio
    */
    public synchronized void addService(String ip, String port, String name) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        List<Service> list= getServices().get(name);
        if(list==null)
        {
            synchronized(getServices())
            {
                getServices().put(name, new ArrayList());
            }
            
        }
        list=getServices().get(name);
        boolean exist = false;
        if(list!=null){
        for (Service service : list) {
            if(service.getIp().equals(ip)&& service.getPort().equals(port))
            {
                exist = true;
            }
        }}
        if(!exist)
        {
            Service serv= new Service(name, ip, port);
            synchronized(getServices()){
                getServices().get(name).add(serv);
                File f= new File(ARCHIVO);
                if(!f.exists())
                {
                    f.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(f,false);
                ObjectOutputStream out;
                out = new ObjectOutputStream(fout);
                out.writeObject(getServices());
                out.close();
                fout.close();
                
            }
            System.out.println("Servicio agregado al directorio");
        }
        
        addServer(ip);
        
    }
    /*
    Método que elimina los objetos de tipo Services del hashmap que tengan la ip pasada por parámetro
    */
    public void deleteServicesFromServer(String ip) throws IOException, FileNotFoundException, ClassNotFoundException
    {
      Iterator<Map.Entry<String, ArrayList<Service>>> iterator =getServices().entrySet().iterator();
      while(iterator.hasNext())
      {
        Map.Entry<String, ArrayList<Service>> entry = iterator.next();
        ArrayList<Service> arr = entry.getValue();
          for (Service serv : arr) {
              if (serv.getIp().equals(ip))
              {
                  arr.remove(serv);
              }
          }
      }
      removeServer(ip);
    }
    
    
    /*
    Método que retorna la lista de servidores disponibles (esto será reeplazado al implementar nuestra clase HiloServidor)
    */
    public List<String> getServers()
    {
       List<String> synclist= Collections.synchronizedList(this.servers);
       return synclist;
    }
    
    
    /*
    Método que nos dice si una ip existe en nuestra lista de servidores (A ser reemplazdo cuando se implemente HiloServidor)
    */
    private boolean serverExist(String server)
    {
        List<String> servs= getServers();
        boolean existe=false;
        for (int i = 0; i < servs.size()&& !existe; i++) {
            String actual= servs.get(i);
            if(server.equals(actual))
            {
                existe = true;
            }
        }
        return existe;
    }
    
    /*
    Método que agrega una ip a la lista de servidores disponibles (deprecated)
    */
    public void addServer(String server)
    {
        if(!serverExist(server))
            getServers().add(server);
    }
    
    /*
    Método que elimina una ip a la lista de servidores disponibles (deprecated)
    */
    
    public void removeServer(String server)
    {
        boolean removed = false;
        List<String> servs= getServers();
        for (int i = 0; i < servs.size(); i++) 
        {
            if(servs.get(i).equals(server))
            {
                servs.remove(i);
                removed=true;
            }
        }
 
    }
}
