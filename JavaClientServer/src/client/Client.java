package client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import message.Message;

public class Client {

	public static void main(String args[]) throws Exception{
				
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

		ArrayList<String> clientNames = new ArrayList<String>();
		
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("names.txt"));
			String line = reader.readLine();

			while (line != null) {

				clientNames.add(line);
				// read next line
				line = reader.readLine();
				
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
		String clientName = clientNames.get((int) (Math.random() * clientNames.size()));
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String command = "";
		
		while (true) {
		
			System.out.print("Enter Command: ");
			command = stdin.readLine();
			
			if (command.equalsIgnoreCase("JOIN")) {

				Socket sk = new Socket(serverIp, serverPort);
				
				ObjectOutputStream sout = new ObjectOutputStream(sk.getOutputStream());
				ObjectInputStream sin = new ObjectInputStream(sk.getInputStream());		
		
				
				Message msg = new Message(Message.JOIN, command);
				
				System.out.println("Joined chat at: " + sk);
				
				Thread receiver = new Thread( new Runnable() {
					
					String msg = clientName + ": ";
					
					@Override
					public void run() {
						
						try {
							
							String s;
							//String s = (String) sin.readObject();
							
							while(true) {
							
								s = (String) sin.readObject();
								System.out.print("Server: " + s + '\n');
								s = (String) sin.readObject();
								
							}
						
							
						}
						
						catch (IOException e) {
							
							e.printStackTrace();
							
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});

				receiver.start();
				
				while (true){
										
					System.out.print("Enter Command: ");
					command = stdin.readLine();
					
					if (command.equalsIgnoreCase("JOIN")) {
						
						System.out.println("Already in a chat!");
						
					}
					
					else if (command.equalsIgnoreCase("Leave")) {
						
						System.out.println("Leaving the chat..");
						msg = new Message(Message.LEAVE, clientName + ": " + command);
						sout.writeObject(msg);
						
						sin.close();
						sout.close();
						sk.close();						
						
						break;
						
					}
						
					else if (command.equalsIgnoreCase("Shutdown")) {
						
						System.out.println("Shutting down client..");
						msg = new Message(Message.SHUTDOWN, clientName + ": " + command);
						sout.writeObject(msg);
						
						sin.close();
						sout.close();
						sk.close();
						stdin.close();
						
						System.exit(0);
						
						
					}
					
					else if (command.equalsIgnoreCase("Shutdown_all")) {
						
						System.out.println("Shutting down whole chat..");
						msg = new Message(Message.SHUTDOWN_ALL,clientName + ": " +  command);
						sout.writeObject(msg);
						
						sk.close();
						sin.close();
						sout.close();
						stdin.close();						
						
					}
					
					else {
						
						
						msg = new Message(Message.NOTE, clientName + ": " + command);
						sout.writeObject(msg);
						
					}
	
					//stdin.close();
			
				}
			}
			
			else {
				
				System.out.println("You need to join a chat first!");
				
			}
			
		}			
		
	}
	
}

//Socket socket1;
//int portNumber = 8989;
//String str = "";
//
//socket1 = new Socket(InetAddress.getLocalHost(), portNumber);
//
//System.out.println("here1");
//
//ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
//
//System.out.println("here2");
//ObjectInputStream ois = new ObjectInputStream(socket1.getInputStream());
//System.out.println("here3");
//str = "initialize";
//oos.writeObject(str);
//
//while ((str = (String) ois.readObject()) != null) {
//  System.out.println(str);
//  oos.writeObject("bye");
//
//  if (str.equals("bye bye"))
//    break;
//}
//
//ois.close();
//oos.close();
//socket1.close();

