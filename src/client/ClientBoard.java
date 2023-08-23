/*
 * Class: ClientBoard
 * Author: Hongzhuan Zhu
 * Purpose: Provides main interface of clinet allow clincl to draw, chat in this interface.
 * */

package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import server.Printer;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientBoard {

	private JFrame frame;
	public static Printer canvas;
	public static Listener listener;
	public static ClientBoard clientBoard;
	public CommandProcessor connection;
	public String lineString;
	public JTextArea chatBoard;
	public static int x, y;
	@SuppressWarnings("rawtypes")
	public JList list;
	private String username;
	private boolean show = false;
	private boolean first = true;
	private JTextField messageInput;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */

	public ClientBoard(CommandProcessor connection, String name) {
		this.connection = connection;
		this.username = name;
		initialize();
	}

	public String getUsernmae() {
		return username;
	}

	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Shared White Board: " + username);
		frame.setBounds(100, 100, 650, 770);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		listener = new Listener(frame);

		// Load content:
		try {
			FileInputStream fileInputStream = new FileInputStream("record.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				listener.update(line);
			}
			fileInputStream.close();
			bufferedReader.close();
		} catch (Exception e) {
			System.out.println("Drawing file error");
		}

		// Canvas Setting
		canvas = new Printer();
		canvas.setBorder(null);
		canvas.setBounds(10, 96, 624, 416);
		canvas.setBackground(Color.white);
		canvas.setList(listener.getRecord());
		frame.getContentPane().add(canvas);
		canvas.setLayout(null);
		frame.setVisible(true);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
		listener.setGraphics(canvas.getGraphics());

		// Line button
		JButton lineBtn = new JButton("Line");
		lineBtn.setBounds(10, 39, 95, 40);
		lineBtn.setIcon(new ImageIcon(ClientBoard.class.getResource("/icon/line.png")));
		lineBtn.setActionCommand("Line");
		lineBtn.addActionListener(listener);
		frame.getContentPane().add(lineBtn);

		// Circle button
		JButton circleBtn = new JButton("Circle");
		circleBtn.setBounds(438, 39, 95, 40);
		circleBtn.setIcon(new ImageIcon(ClientBoard.class.getResource("/icon/circle.png")));
		circleBtn.setActionCommand("Circle");
		circleBtn.addActionListener(listener);
		frame.getContentPane().add(circleBtn);

		// Triangle button
		JButton triangleBtn = new JButton("Triangle");
		triangleBtn.setBounds(117, 39, 95, 40);
		triangleBtn.setIcon(new ImageIcon(ClientBoard.class.getResource("/icon/triangle.png")));
		triangleBtn.setActionCommand("Triangle");
		triangleBtn.addActionListener(listener);
		frame.getContentPane().add(triangleBtn);

		// Rectangle button
		JButton rectangleBtn = new JButton("Rectangle");
		rectangleBtn.setBounds(224, 39, 95, 40);
		rectangleBtn.setIcon(new ImageIcon(ClientBoard.class.getResource("/icon/rectangular.png")));
		rectangleBtn.setActionCommand("Rectangle");
		rectangleBtn.addActionListener(listener);
		frame.getContentPane().add(rectangleBtn);

		JButton textBtn = new JButton("Text");
		textBtn.setBounds(331, 39, 95, 40);
		textBtn.setIcon(new ImageIcon(ClientBoard.class.getResource("/icon/text.png")));
		textBtn.setActionCommand("Text");
		textBtn.addActionListener(listener);
		frame.getContentPane().add(textBtn);

		JButton colorWheelBtn = new JButton("Color");
		colorWheelBtn.setBounds(545, 39, 96, 40);
		colorWheelBtn.setIcon(new ImageIcon(ClientBoard.class.getResource("/icon/colorWheel.png")));
		colorWheelBtn.setActionCommand("Color");
		colorWheelBtn.setPreferredSize(new Dimension(27, 27));
		colorWheelBtn.addActionListener(listener);
		frame.getContentPane().add(colorWheelBtn);

		JLabel lblNewLabel = new JLabel("Tool bar");
		lblNewLabel.setBounds(10, 11, 95, 26);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		frame.getContentPane().add(lblNewLabel);

		JLabel currentUserLB = new JLabel("Users:");
		currentUserLB.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		currentUserLB.setBounds(10, 524, 57, 16);
		frame.getContentPane().add(currentUserLB);

		JLabel chatLB = new JLabel("Chat Board:");
		chatLB.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		chatLB.setBounds(161, 524, 95, 16);
		frame.getContentPane().add(chatLB);

		JButton sendBtn = new JButton("Send Message");
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String content = username + ": " + messageInput.getText();
				try {
					connection.outputStream.writeUTF("sendchat:" + content);
					connection.outputStream.flush();
				} catch (IOException e1) {
					System.out.println("Unable to send your message.");
				}
				messageInput.setText("");
			}
		});

		sendBtn.setBounds(501, 692, 133, 33);
		frame.getContentPane().add(sendBtn);

		list = new JList();
		frame.getContentPane().add(list);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(10, 552, 125, 173);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane);
		scrollPane.setVisible(false);

		messageInput = new JTextField();
		messageInput.setBounds(161, 692, 322, 33);
		frame.getContentPane().add(messageInput);
		messageInput.setColumns(10);

		JButton freshBtn = new JButton("Show");
		freshBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (show == false) {
					scrollPane.setVisible(true);
					freshBtn.setText("Hide");
					show = true;
					if (first == true) {
						try {
							connection.outputStream.writeUTF("requestUser");
							connection.outputStream.flush();
							first = false;
						} catch (Exception e1) {
							System.out.println("Unable to update user list.");
						}
					}
				} else if (show == true) {
					scrollPane.setVisible(false);
					freshBtn.setText("Show");
					show = false;
				}
			}
		});
		freshBtn.setBounds(70, 516, 65, 33);
		frame.getContentPane().add(freshBtn);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(161, 552, 473, 128);
		frame.getContentPane().add(scrollPane_1);

		chatBoard = new JTextArea();
		scrollPane_1.setViewportView(chatBoard);
		chatBoard.setLineWrap(true);
		chatBoard.setEditable(false);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					connection.outputStream.writeUTF("over");
					connection.outputStream.flush();
				} catch (IOException e1) {
					System.out.print("Unable to close connection.");
				}

			}
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					connection.outputStream.writeUTF("begin:");
					connection.outputStream.flush();
				} catch (Exception e5) {
					System.out.println("Unable to load user list");
				}
			}
		});
		
	}
}
