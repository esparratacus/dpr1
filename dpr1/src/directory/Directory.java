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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private HashMap<String,ArrayList<Service>> services=new  HashMap<>();
    private ServerSocket server;
    private ArrayList<Gatekeeper> gatekeepers;
    private File dir;

    public Directory() {
        this.gatekeepers = new ArrayList<>();
        //services.put("dummy", new ArrayList<Service>());
        dir = new File(ARCHIVO);
        if(!dir.exists())
        {
            try {
                dir.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
        
    }
    
    public void run()
    {
        try {
            server = new ServerSocket(6666);
            Socket s;
            while(true)
            {
                s = server.accept();
                Gatekeeper gk = new Gatekeeper(s, this);
                gatekeepers.add(gk);
                gk.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < this.gatekeepers.size(); i++) {
                    Gatekeeper g= gatekeepers.get(i);
                    synchronized(g){
                        g.notify();
                    }
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addService(String ip, String port, String name)
    {
        Service s = new Service(name, ip, port);
        ArrayList<Service> as= services.get(name);
        if(as!=null)
        {
            as.add(s);
            
        }
        else
        {
            services.put(name, new ArrayList<Service>());
            services.get(name).add(s);
            
        }
        salvarMapa();
        
    }
    
    public HashMap<String, ArrayList<Service>> getServices() {
        cargarMapa();
        return services;
    }

    public void setServices(HashMap<String, ArrayList<Service>> services) {
        this.services = services;
    }

    public ArrayList<Gatekeeper> getGatekeepers() {
        return gatekeepers;
    }

    public void setGatekeepers(ArrayList<Gatekeeper> gatekeepers) {
        this.gatekeepers = gatekeepers;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    private void salvarMapa() {
        HashMap<String, ArrayList<Service>> servs= services;
        FileOutputStream fout=null;
        ObjectOutputStream out=null;
        try {
            fout= new FileOutputStream(ARCHIVO);
            out = new ObjectOutputStream(fout);
            out.writeObject(servs);
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                out.flush();
                out.close();
                fout.flush();
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void cargarMapa(){
        FileInputStream fin= null;
        try {
            fin = new FileInputStream(ARCHIVO);
            ObjectInputStream in = new ObjectInputStream(fin);
            services=(HashMap<String,ArrayList<Service>>)in.readObject();
            in.close();
            fin.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fin.close();
            } catch (IOException ex) {
                Logger.getLogger(Directory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    

    
}
