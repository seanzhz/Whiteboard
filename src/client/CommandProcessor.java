/*
 * Class: CommandProcessor
 * Author: Hongzhuan Zhu
 * Purpose: Process incoming stream and update into the client side
 * */

package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

public class CommandProcessor extends Thread {

	private Socket socket;
	public DataInputStream inputStream = null;
	public DataOutputStream outputStream = null;

	private String status;
	private boolean kick = false;
	public String line = null;

	public CommandProcessor(Socket socket) {
		setStatus("wait");
		try {
			this.socket = socket;
			outputStream = new DataOutputStream(this.socket.getOutputStream());
			inputStream = new DataInputStream(this.socket.getInputStream());

		} catch (Exception e) {
			System.out.println("Unable to complete sock setting.");
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		try {
			while (true) {
				String response = null;
				if ((response = inputStream.readUTF()) != null) {
					String[] splitedResponse = response.split(":", 2);

					if (splitedResponse[0].equals("draw")) {
						ClientBoard.listener.update(splitedResponse[1]);
						ClientBoard.canvas.repaint();
					}
					if (splitedResponse[0].equals("postchat")) {
						if (splitedResponse[1].split(":")[0].equals(ClientLogin.clientBoard.getUsernmae())) {
							ClientLogin.clientBoard.chatBoard.append("Me:");
							ClientLogin.clientBoard.chatBoard.append(splitedResponse[1].split(":")[1]);
							ClientLogin.clientBoard.chatBoard.append("\n");
						} else {
							ClientLogin.clientBoard.chatBoard.append(splitedResponse[1]);
							ClientLogin.clientBoard.chatBoard.append("\n");
						}
					}
					if (splitedResponse[0].equals("updateUser") && ClientLogin.clientBoard != null) {
						ClientLogin.clientBoard.list.setListData(splitedResponse[1].split(":"));
					}

					if (splitedResponse[0].equals("kick")) {
						kick = true;
						JOptionPane.showMessageDialog(ClientLogin.clientBoard.getFrame(),
								"Exit, You have been remove from manager.");
					}
					if (splitedResponse[0].equals("feedback")) {
						this.status = splitedResponse[1];
					}

					if (splitedResponse[0].equals("clientout")) {
						String[] users = splitedResponse[1].split(":", 2);
						String[] userList = users[1].split(":");
						ClientLogin.clientBoard.list.setListData(userList);
					}

					if (splitedResponse[0].equals("new")) {
						ClientBoard.canvas.removeAll();
						ClientBoard.canvas.updateUI();
						ClientBoard.listener.clearRecord();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Connection end");
			try {
				inputStream.close();
				outputStream.close();
				socket.close();
				System.out.println("Socket closed.");
			} catch (IOException e1) {
				System.out.println("Unable to close connection");
			}

			try {
				if (!kick) {
					JOptionPane.showMessageDialog(ClientLogin.clientBoard.getFrame(), "Disconnected with server");
				}
			} catch (Exception e2) {
				System.out.println("End");

			}
			System.exit(0);
		}
	}
}
