
/**
 * Class: Connection
 * Author: Hongzhuan Zhu
 * Purpose: Process incoming command and response
 * ***/

package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import manager.ManagerTool;

public class Connection extends Thread {

	public Socket socket;
	public String name;
	public DataInputStream inputStream;
	public DataOutputStream outputStream;
	public boolean kick = false;

	public Connection(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			inputStream = new DataInputStream(input);
			outputStream = new DataOutputStream(output);

			while (true) {
				String incoming = inputStream.readUTF();
				if (incoming != null) {
					String[] response = incoming.split(":", 2);
					if (response[0].equals("begin")) {
						// Send broadcast to all client to update userList
						String namelist = "updateUser";
						for (String userName : Server.usernames) {
							namelist += ":" + userName;
						}
						ManagerTool.broadcast(namelist);
						// Update manage UI
						String[] temp = namelist.split(":", 2);
						String[] clients = temp[1].split(":");
						ManagerTool.addUser(clients);
					} else if (response[0].equals("request")) {
						String currentname = response[1];
						name = response[1];
						if (Server.usernames.contains(currentname)) {
							outputStream.writeUTF("feedback:no");
							outputStream.flush();
							Server.connections.remove(this);
						} else {
							int reply = JOptionPane.showConfirmDialog(null, name + " wants to join in.", "Join request",
									JOptionPane.YES_NO_OPTION);

							if (reply == 0) {
								Server.usernames.add(currentname);
								outputStream.writeUTF("feedback:yes");
								outputStream.flush();
								System.out.println("Current user:" + Server.usernames);
							} else {
								outputStream.writeUTF("feedback:reject");
								outputStream.flush();
							}
						}
					} else if (response[0].equals("draw")) {
						ManagerTool.broadcast(incoming);
						ManagerTool.canvasRepaint(response[1]);
					} else if (response[0].equals("sendchat")) {
						String chatCommand = "postchat:" + response[1];
						ManagerTool.broadcast(chatCommand);
					} else if (response[0].equals("requestUser")) {
						String string = "updateUser";
						for (String userName : Server.usernames) {
							string += ":" + userName;
						}
						ManagerTool.broadcast(string);
					} else if (response[0].equals("over")) {
						clientOut();
						this.interrupt();
						socket.close();
						break;
					} else if (response[0].equals("reject")) {
						Server.connections.remove(this);
						this.interrupt();
						socket.close();
						break;
					} else {
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out.print("User:" + this.name + " Connection interrupted.");
			// whether kick out by manager
			if (!kick) {
				clientOut();
			}
		}
	}

	public void clientOut() {
		Server.connections.remove(this);
		Server.usernames.remove(name);
		String string = "clientout:" + name;
		for (String userNmae : Server.usernames) {
			string += ":" + userNmae;
		}
		ManagerTool.broadcast(string);
		// Update UI
		String temp = string.split(":", 2)[1];
		ManagerTool.clientOut(temp);
	}
}
