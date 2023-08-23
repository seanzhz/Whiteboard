/**
 * Class: ManagerLogin
 * Author: Hongzhuan Zhu
 * Purpose: Allow manager to Login and Start a server
 * 
 * **/

package manager;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import server.Server;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManagerLogin {

	static String address;
	private static int port;
	static String username;

	public static ManagerBoard managerBoard;
	private JFrame frmLogin;
	private JTextField usernameTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		// Check whether port within the valid range
		if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49152) {
			System.out.println("Invalid Port Number");
			System.exit(-1);
		}else {
			address = args[0];
			port = Integer.parseInt(args[1]);
			username = args[2];
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerLogin window = new ManagerLogin();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// TODO: start Server (pass port and address)
		Server.launchServer(port, username);
	}
	/**
	 * Create the application.
	 */
	public ManagerLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Manager Log in");
		frmLogin.setBounds(100, 100, 450, 250);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		JLabel loginLb = new JLabel("Shared White Board");
		loginLb.setBounds(93, 20, 269, 50);
		loginLb.setFont(new Font("Lucida Grande", Font.BOLD, 26));
		frmLogin.getContentPane().add(loginLb);

		JLabel usernameLB = new JLabel("Username");
		usernameLB.setBounds(75, 97, 70, 16);
		frmLogin.getContentPane().add(usernameLB);

		usernameTF = new JTextField();
		usernameTF.setEditable(false);
		usernameTF.setBounds(176, 92, 186, 26);
		usernameTF.setToolTipText("Enter your username here");
		frmLogin.getContentPane().add(usernameTF);
		usernameTF.setColumns(10);
		usernameTF.setText(username);
		JButton loginBtn = new JButton("Log in");
		loginBtn.setBounds(113, 148, 226, 56);
		loginBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frmLogin.dispose();
				managerBoard = new ManagerBoard(username);
			}
		});
		frmLogin.getContentPane().add(loginBtn);

		JLabel lblNewLabel = new JLabel("Server status:");
		lblNewLabel.setBounds(305, 6, 94, 16);
		frmLogin.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("✅");
		lblNewLabel_1.setBounds(401, 6, 21, 16);
		frmLogin.getContentPane().add(lblNewLabel_1);
	}
}
