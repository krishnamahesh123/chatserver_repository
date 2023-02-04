package clientServer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import message.Message;

public class Server {

	int port;
	ServerSocket server = null;
	Socket client = null;
	ExecutorService pool = null;
	int clientCount = 0;
	static ArrayList<Socket> clients = new ArrayList<Socket>(); 
	
	public static void main(String args[]) {
	
		Server server = new Server();
		
		try {
			
			server.startServer();
		
		}
		catch (IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public static void func(String content) throws IOException {
		
		
		for(int i = 0; i < clients.size(); i++) {
			
			Socket client = clients.get(i);

			PrintWriter out = new PrintWriter(client.getOutputStream());

			out.println(content);
		
			System.out.println("Sent message to: " + client);
			
//			out.close();
//			client.close();
			
		}

		

	}
	
	Server(){
		
		int serverPort = 0;
		int numClients = 0;
		
		Properties prop = new Properties();
		try {
			
			// load a properties file for reading
			prop.load(new FileInputStream("serverDetails.properties"));
			
			serverPort = Integer.parseInt(prop.getProperty("port"));
			numClients = Integer.parseInt(prop.getProperty("maxClients"));
			
			System.out.println("Server port read from configuration file is: " + Integer.toString(serverPort));

			
		} catch (IOException ex) {
			ex.printStackTrace();
		}


		this.port = serverPort;
		pool = Executors.newFixedThreadPool(numClients);
		
	}
	
	public void startServer() throws IOException{
		
		server = new ServerSocket(this.port);
		System.out.println("Server booted!");
		
		while(true) {
			
			client = server.accept();
			clientCount++;
			clients.add(client);
			ServerThread runnable = new ServerThread(client, clientCount, this);			
			pool.execute(runnable);
			
		}		
		
	}
	
	private static class ServerThread implements Runnable {
	
		Server server = null;
		Socket client = null;
		ObjectInputStream cin;
		ObjectOutputStream cout;
		Scanner sc = new Scanner(System.in);
		int id;
		Message msg;
		
		
		ServerThread(Socket client, int count, Server server) throws IOException{
			
			this.client = client;
			this.server = server;
			this.id = count;
			
			System.out.println("Connection " + id + " established with client " + client);
			
			cin = new ObjectInputStream(client.getInputStream());
			cout = new ObjectOutputStream(client.getOutputStream());
			
			
		}
		
		@Override 
		public void run() {
			
			int x = 1;
			
			try {
				
				while(true) {
					
					msg = (Message)cin.readObject();
					
					if (msg.getType() == Message.NOTE) {
						
						String content = (String) msg.getContent();
						System.out.println(content);						

						Server.func(content);
						
					}
					
					else if(msg.getType() == Message.LEAVE) {
						
						client.close();
						cin.close();
						cout.close();
					}
					else if(msg.getType() == Message.SHUTDOWN) {
						
						client.close();
						cin.close();
						cout.close();
					}					
					else if(msg.getType() == Message.SHUTDOWN_ALL) {
						
						cin.close();
						cout.close();
						client.close();
						server.server.close();
						
						System.exit(0);
						
					}
					else if(msg.getType() == Message.JOIN) {
						
						String s = "Connection Established!";
						cout.writeObject(s);
						
					}
					
				}
			
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
