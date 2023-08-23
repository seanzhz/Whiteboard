/**
Class: Server
Purpose: open server and establish connection
Author: Hongzhuan Zhu, 1223535
**/
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	//To hold list of user name
	public static List<String> usernames = new ArrayList<>();
	public static List<Connection> connections = new ArrayList<>();
	private static int clientUID = 0;
	
	public static void launchServer(int port, String username) {
		Connection connection = null;
		ServerSocket serverSocket = null;
		usernames.add(username);
		
		try {
			serverSocket = new ServerSocket(port);
			Socket client;
			while (true) {
				client = serverSocket.accept();
				clientUID++;
				connection = new Connection(client);
				connection.start();
				connections.add(connection);
				System.out.println("Client(UID:" + clientUID + ") has been connected.");
			}
		} catch (IOException e) {
			System.out.println("Connection Failed");
		}
	}
}
