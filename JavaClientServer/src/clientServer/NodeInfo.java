package clientServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class NodeInfo implements Serializable{

    // IP of the client
    String ip;
    // port of the client
    String port;
    // logical name of the client
    String name;
    
    // constructor
    public NodeInfo(String ip, String port) {
        
    	this.ip = ip;
        this.port = port;    
        
        BufferedReader reader;
        ArrayList<String> names = new ArrayList<String>();
        
		try {
			reader = new BufferedReader(new FileReader("src/clientServer/names.txt"));
			String line = reader.readLine();

			while (line != null) {

				//read each name from the file
				line = reader.readLine();
				
				//add the name to the arraylist of names
				names.add(line);
				
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.name = names.get((int)(Math.random() * names.size()));
        		
    }
    
    // overloaded constructor to allow passing names file name
    public NodeInfo(String ip, String port, String namesFile) {
        
    	this.ip = ip;
        this.port = port;    
        
        BufferedReader reader;
        ArrayList<String> names = new ArrayList<String>();
        
		try {
			reader = new BufferedReader(new FileReader(namesFile));
			String line = reader.readLine();

			while (line != null) {

				//read each name from the file
				line = reader.readLine();
				
				//add the name to the arraylist of names
				names.add(line);
				
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        this.name = names.get((int)(Math.random() * names.size()));
        		
    }

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
