/*
 * 	Class: ClientLogin
 *  Author: Hongzhuan Zhu
 *  Purpose: Connect to the command processor and launch the log in window 
 * */

package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import manager.ManagerBoard;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientLogin {

	// Connection setting
	static String address;
	static int port;
	static String username;
	// Object setting
	public static CommandProcessor connection;
	public static ClientBoard clientBoard;
	public static Socket socket;
	private JFrame frame;
	public static ClientLogin clientLogin;
	private JTextField usernameTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// Check whether port within the valid range
		if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49152) {
			System.out.println("Invalid Port Number");
			System.exit(-1);
		} else {
			address = args[0];
			port = Integer.parseInt(args[1]);
			username = args[2];
		}

		try {
			socket = new Socket(address, port);

		} catch (Exception e) {
			System.out.println("Connection failed");
			System.exit(1);
		}

		connection = new CommandProcessor(socket);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogin window = new ClientLogin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("Unable to open login Window");
				}
			}
		});

		connection.start();
	}

	/**
	 * Create the application.
	 */
	public ClientLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		client.Listener listener = new client.Listener();
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton connectionBtn = new JButton("Connect");
		connectionBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/join.png")));
		connectionBtn.addActionListener(listener);
		connectionBtn.addActionListener(e -> {
			if (e.getActionCommand().equals("Connect")) {
				try {
					username = usernameTF.getText();

					if (username.contains(":") || username.contains("^") || username.equalsIgnoreCase("Manager")) {
						JOptionPane.showMessageDialog(frame,
								"Inlegall name, name rule: no : ^ character and do not named as Manager");
						return;
					}
					// Send request
					connection.outputStream.writeUTF("request:" + username);
					connection.outputStream.flush();
					// Setting a Timer
					int time = 0;
					while (connection.getStatus().equals("wait") && time < 100000) {
						TimeUnit.MILLISECONDS.sleep(100);
						time += 100;
					}
					String joinCommand = connection.getStatus();
					if (joinCommand.equals("no")) {
						JOptionPane.showMessageDialog(frame,
								"Username already exsited, please change another name to join.");
						connection.setStatus("wait");
					}

					else if (joinCommand.equals("reject")) {
						JOptionPane.showMessageDialog(frame, "Manager reject your join request");
						connection.outputStream.writeUTF("reject");
						connection.outputStream.flush();
						

					} else if (joinCommand.equals("yes")) {
						frame.dispose();
						try {
							if (clientBoard == null) {
								clientBoard = new ClientBoard(connection, username);
							}
						} catch (Exception e2) {
							System.out.println("Unable to open client board");
						}

					} else {
						JOptionPane.showMessageDialog(frame, "Timeout.");
						try {
							socket.close();
							System.exit(1);
						} catch (Exception e3) {
							System.out.println("Error in time system");
						}
						frame.dispose();
					}
				} catch (Exception e4) {
					System.out.println("Unable to connect");
				}
			}

		});

		connectionBtn.setBounds(133, 175, 176, 61);
		frame.getContentPane().add(connectionBtn);

		JLabel lblNewLabel = new JLabel("Shared White Board");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 26));
		lblNewLabel.setBounds(91, 25, 268, 72);
		frame.getContentPane().add(lblNewLabel);

		usernameTF = new JTextField();
		usernameTF.setBounds(168, 102, 204, 33);
		frame.getContentPane().add(usernameTF);
		usernameTF.setColumns(10);
		usernameTF.setText(username);

		JLabel usernameLB = new JLabel("Username:");
		usernameLB.setBounds(91, 109, 71, 16);
		frame.getContentPane().add(usernameLB);

	}
}
