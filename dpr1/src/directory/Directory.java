/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author david
 */
public class Directory implements Runnable{
    
    private volatile ConcurrentHashMap<String,ArrayList<Service>> services;
    private ArrayList<String> servers;
    private ServerSocket server;
    private volatile ArrayList<Gatekeeper> gatekeepers;

    public void run_updater()
    {
        
        
    }   
    
    public void setServers(ArrayList<String> servers) {
        this.servers = servers;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public ArrayList<Gatekeeper> getGatekeepers() {
        return gatekeepers;
    }

    public void setGatekeepers(ArrayList<Gatekeeper> gatekeepers) {
        this.gatekeepers = gatekeepers;
    }
    
    public Directory() {
        this.services = new ConcurrentHashMap<>();
        this.servers= new ArrayList<>();
        this.gatekeepers=new ArrayList<>();
    }
    
    public void start() throws IOException 
    {
        System.out.println("Estoy entrando donde debo entrar tambien");
        this.server = new ServerSocket(6666);
        HiloCliente hc = new HiloCliente(this);
        hc.start();
        while(true)
        {
            try{
                Socket actual;
                actual = this.server.accept();
                Gatekeeper gk = new Gatekeeper(actual, this);
                gk.start();
                this.gatekeepers.add(gk);
                
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void UpdateGatekeepers(){
        ArrayList<Gatekeeper> toDelete = new ArrayList<>();
        for (Gatekeeper g : this.gatekeepers) {
                    synchronized(g){
                        g.notify();
                    }
        }
        for(Gatekeeper g: gatekeepers){
            if(g.getListener() == null){
                toDelete.add(g);
            }
        }
        gatekeepers.removeAll(toDelete);
                
    }
    /*
    Método que retorna el hashmap con los servicios disponibles
    */
    public ConcurrentHashMap<String, ArrayList<Service>> getServices() {
       // Map<String,ArrayList<Service>> syncServices= Collections.synchronizedMap(this.services);
        return services;
    }

    /*public void setServices(HashMap<String, ArrayList<Service>> services) {
        this.services = services;
    }
    /*
    Método que agrega un servicio al directorio
    */
    public void addService(String ip, String port, String name)
    {
        ArrayList<Service> list= getServices().get(name);
        if(list==null)
        {
            getServices().put(name, new ArrayList());
        }
        list=getServices().get(name);
        boolean exist = false;
        for (Service service : list) {
            if(service.getIp().equals(ip)&& service.getPort().equals(port))
            {
                exist = true;
            }
        }
        if(!exist)
        {
            Service serv= new Service(name, ip, port);
            this.services.get(name).add(serv);
        }
        addServer(ip);
        System.out.println("Servicio Agregado");
        
    }
    /*
    Método que elimina los objetos de tipo Services del hashmap que tengan la ip pasada por parámetro
    */
    public void deleteServicesFromServer(String ip)
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

    @Override
    public void run() {
        try {
            while(true)
            {
                System.out.println("Ejecutando rutina de actualizaciòn de direcotrio");
                UpdateGatekeepers();
                Thread.sleep(7000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
