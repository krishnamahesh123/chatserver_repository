package clientServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {

	public static void main(String args[]) {
		
		
		File file = new File(".");
		for(String fileNames : file.list()) System.out.println(fileNames);
		
		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;  
		final Scanner sc = new Scanner(System.in);
	
		String serverIp = "";
		int serverPort = 0;
		Properties prop = new Properties();
		try {
			
			// load a properties file for reading
			prop.load(new FileInputStream("serverDetails.properties"));
			
			serverIp = prop.getProperty("ip");
			serverPort = Integer.parseInt(prop.getProperty("port"));
			
			System.out.println("Server port read from configuration file is: " + Integer.toString(serverPort));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		

		
		try {
			
			clientSocket = new Socket(serverIp, serverPort);
			
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			Thread sender = new Thread(new Runnable() {
				
				String msg;
				
				@Override
				public void run() {
					
					while(true) {
						
						msg = sc.nextLine();
						out.println(msg);
						out.flush();
						
					}
					
				}
				
			});
			
			sender.start();
			Thread receiver = new Thread(new Runnable() {
				
				String msg;
				@Override
				public void run() {
					
					try {
						
						msg = in.readLine();
						
						while(msg != null) {
							
							System.out.println("Server: " + msg);
							msg = in.readLine();
							
						} 
						
					} catch (IOException e) {
						
						e.printStackTrace();
						
					}
					
				}
			});
			
			receiver.start();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
}
