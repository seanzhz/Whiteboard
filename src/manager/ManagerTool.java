/**
 * Class: ManagerTool
 * Author: Hongzhuan Zhu
 * Purpose: Provides useful tool to manager the connection.
 * **/

package manager;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import server.Connection;
import server.Server;

public class ManagerTool {

	@SuppressWarnings("unchecked")
	public static void addUser(String[] clients) {
		ManagerLogin.managerBoard.list.setListData(clients);
	}

	@SuppressWarnings("unchecked")
	public static void clientOut(String clients) {
		String[] kStrings = clients.split(":", 2);
		JOptionPane.showMessageDialog(ManagerLogin.managerBoard.frameManager,
				"The user: " + kStrings[0] + " has been left");
		String[] clientStrings = kStrings[1].split(":");
		ManagerLogin.managerBoard.list.setListData(clientStrings);
	}

	public static void canvasRepaint(String line) {
		ManagerBoard.listener.update(line);
		ManagerBoard.canvas.repaint();
	}

	public static void broadcast(String message) {
		for (int i = 0; i < Server.connections.size(); i++) {
			Connection stConnection = Server.connections.get(i);
			try {
				stConnection.outputStream.writeUTF(message);
				stConnection.outputStream.flush();
			} catch (IOException e) {
				System.out.println("Unable to broadcast:" + message);
			}
		}
	}

	public static void removeUser(String user) {
		for (int i = 0; i < Server.connections.size(); i++) {
			Connection connection = Server.connections.get(i);
			if (user.equals(connection.name)) {
				connection.kick = true;
				try {
					connection.outputStream.writeUTF("kick:" + connection.name);
					connection.outputStream.flush();
				} catch (Exception e2) {
					System.out.println("Kick error");
				}
				try {
					connection.inputStream.close();
					connection.outputStream.close();
					connection.socket.close();
				} catch (IOException e1) {
					System.out.println("Unable to close connection");
				}
				Server.connections.remove(i);
				Server.usernames.remove(user);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void updateUser(String command) {
		String commandString = command;

		for (String userNmae : Server.usernames) {
			commandString += ":" + userNmae;
		}

		ManagerTool.broadcast(commandString);
		// Update
		String[] temp = commandString.split(":", 2);
		String[] client = temp[1].split(":");
		ManagerLogin.managerBoard.list.setListData(client);

	}

	public synchronized static void reload(String recoList) {
		try {
			BufferedWriter bwBufferedWriter = new BufferedWriter(new FileWriter("record.txt", true));

			String line = recoList;
			bwBufferedWriter.write(line);
			bwBufferedWriter.newLine();
			bwBufferedWriter.flush();
			bwBufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Error: draw record file");
		}

	}

	public static void clearRecord() {
		try {
			BufferedWriter bwBufferedWriter = new BufferedWriter(new FileWriter("record.txt"));
			bwBufferedWriter.write("");
			bwBufferedWriter.flush();
			bwBufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Unable to clear records");
		}
	}

	public static boolean savePic(JPanel component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		component.paint(g);
		try {
			ImageIO.write(image, "png", new File("save.png"));
			return true;
		} catch (IOException ex) {
			System.out.println("File error, save failed");
			return false;
		}
	}

	public static boolean saveAs(String filename, JPanel component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		component.paint(g);
		try {
			ImageIO.write(image, "png", new File(filename+".png"));
			return true;
		} catch (IOException ex) {
			System.out.println("File error, saveas failed");
			return false;
		}
	}

	@SuppressWarnings("static-access")
	public static boolean openDraw(String fileName) {
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			ManagerLogin.managerBoard.canvas.removeAll();
			ManagerLogin.managerBoard.canvas.updateUI();
			ManagerLogin.managerBoard.listener.clearRecord();
			broadcast("new");
			clearRecord();
			String command = "draw:";
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String line2;
				ManagerTool.reload(line);
				line2 = command + line;
				ManagerTool.broadcast(line2);
				ManagerTool.canvasRepaint(line);
			}
			fileInputStream.close();
			bufferedReader.close();
			return true;
		} catch (Exception e) {
			System.out.println("Open file error");
			return false;
		}
	}
}
