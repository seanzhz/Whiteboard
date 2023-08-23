/**
 * Class: ManagerBoard
 * Author: Hongzhuan Zhu
 * Purpose: Allows manager to draw, and to control the client side
 * **/

package manager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.JButton;

import javax.swing.ImageIcon;
import java.awt.Font;


import javax.swing.JScrollPane;

import server.Printer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import java.awt.SystemColor;

public class ManagerBoard {

	public JFrame frameManager;
	static ManagerBoard whiteboard;
	public static Printer canvas;
	public static Listener listener;
	
	@SuppressWarnings("rawtypes")
	public JList list;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public ManagerBoard(String name) {
		initialize(name);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize(String name) {
		frameManager = new JFrame();
		frameManager.setResizable(false);
		frameManager.setTitle("Shared White Board: " + name );
		frameManager.setBounds(100, 100, 830, 550);
		frameManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameManager.getContentPane().setLayout(null);
		
		
		listener = new Listener(frameManager);
		canvas = new Printer();
		canvas.setBorder(null);
		canvas.setBounds(10, 96, 624, 416);
		canvas.setBackground(Color.white);
		canvas.setList(listener.getRecord());
		frameManager.getContentPane().add(canvas);
		canvas.setLayout(null);
		frameManager.setVisible(true);
		canvas.addMouseListener(listener);
		canvas.addMouseMotionListener(listener);
		listener.setGraphic(canvas.getGraphics());
		
		//Line button
		JButton lineBtn = new JButton("Line");
		lineBtn.setBounds(10, 39, 95, 40);
	    lineBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/line.png")));
		lineBtn.setActionCommand("Line");
		lineBtn.addActionListener(listener);
		frameManager.getContentPane().add(lineBtn);
		
		//Circle button
		JButton circleBtn = new JButton("Circle");
		circleBtn.setBounds(438, 39, 95, 40);
		circleBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/circle.png")));
		circleBtn.setActionCommand("Circle");
		circleBtn.addActionListener(listener);
		frameManager.getContentPane().add(circleBtn);
		
		JButton triangleBtn = new JButton("Triangle");
		triangleBtn.setBounds(117, 39, 95, 40);
		triangleBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/triangle.png")));
		triangleBtn.setActionCommand("Triangle");
		triangleBtn.addActionListener(listener);
		frameManager.getContentPane().add(triangleBtn);
		
		JButton rectangleBtn = new JButton("Rectangle");
		rectangleBtn.setBounds(224, 39, 95, 40);
		rectangleBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/rectangular.png")));
		rectangleBtn.setActionCommand("Rectangle");
		rectangleBtn.addActionListener(listener);
		frameManager.getContentPane().add(rectangleBtn);
		
		JButton textBtn = new JButton("Text");
		textBtn.setBounds(331, 39, 95, 40);
		textBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/text.png")));
		textBtn.setActionCommand("Text");
		textBtn.addActionListener(listener);
		frameManager.getContentPane().add(textBtn);
		
		JButton colorWheelBtn = new JButton("Color");
		colorWheelBtn.setBounds(538, 39, 87, 40);
		colorWheelBtn.setIcon(new ImageIcon(ManagerBoard.class.getResource("/icon/colorWheel.png")));
		colorWheelBtn.setActionCommand("Color");
		colorWheelBtn.addActionListener(listener);
		frameManager.getContentPane().add(colorWheelBtn);
		
		JLabel lblNewLabel = new JLabel("Tool bar");
		lblNewLabel.setBounds(10, 11, 95, 26);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		frameManager.getContentPane().add(lblNewLabel);
		
		
		list = new JList<Object>();
		frameManager.getContentPane().add(list);
		String unameString = name;
		String[] managerName = {unameString};
		list.setListData(managerName);
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		scrollPane.setBounds(656, 202, 158, 252);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frameManager.getContentPane().add(scrollPane);
		
		JButton removeBtn = new JButton("Remove");
		removeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String user= list.getSelectedValue().toString();
				if(name.equals(user)) {
					JOptionPane.showMessageDialog(frameManager, "You cannot remove yourself in this way.");
					return;
				}
				try {
					ManagerTool.removeUser(user);
					JOptionPane.showMessageDialog(frameManager, user + " has been removed.");
				} catch (Exception e2) {
					System.out.println("Remove error");
				}
				ManagerTool.updateUser("clientout:");
			}
		});
	
		removeBtn.setBounds(666, 457, 138, 40);
		frameManager.getContentPane().add(removeBtn);
		
		JLabel titleds = new JLabel("Manager Dashboard");
		titleds.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		titleds.setBounds(656, 43, 158, 29);
		frameManager.getContentPane().add(titleds);
		
		JLabel lblNewLabel_1 = new JLabel("Current users:");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblNewLabel_1.setBounds(656, 176, 106, 16);
		frameManager.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("File System:");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblNewLabel_2.setBounds(657, 96, 105, 16);
		frameManager.getContentPane().add(lblNewLabel_2);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"-----", "New", "Save", "Save as", "Open", "Close"}));
		comboBox.addActionListener(e -> {
			if(comboBox.getSelectedIndex() == 0) {
				//System.out.println("Default menu selected, Action: Nothing");		
				
					
				
			}
			else if(comboBox.getSelectedIndex() == 1) {
				System.out.println(comboBox.getSelectedItem().toString() + " selected.");
				canvas.removeAll();
				canvas.updateUI();
				listener.clearRecord();
				ManagerTool.broadcast("new");
				ManagerTool.clearRecord();
				JOptionPane.showMessageDialog(frameManager, "New canvas has been replaced.");
			}
			else if (comboBox.getSelectedIndex() == 2) {
				System.out.println(comboBox.getSelectedItem().toString());
				if (ManagerTool.savePic(canvas)) {
					JOptionPane.showMessageDialog(frameManager, "Current canvas has been saved");
				}else {
					JOptionPane.showMessageDialog(frameManager, "Unable to save image.");
				}	
			}
			else if (comboBox.getSelectedIndex() == 3) {
				System.out.println(comboBox.getSelectedItem().toString());
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save as");   
				int userSelection = fileChooser.showSaveDialog(frameManager);
				
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    if (ManagerTool.saveAs(fileToSave.getAbsolutePath(), canvas)) {
						JOptionPane.showMessageDialog(frameManager, "Current canvas has been saved");
					}
					else {
						JOptionPane.showMessageDialog(frameManager, "Unable to save image");
					}
				}		
			}
			else if (comboBox.getSelectedIndex() == 4) {
				System.out.println(comboBox.getSelectedItem().toString());
				String text = JOptionPane.showInputDialog("Please type the file name you want to open (Current found: people.txt, color.txt) ");
				if(text!=null) {
				if(ManagerTool.openDraw(text)) {
				JOptionPane.showMessageDialog(frameManager, "New canvas has been replaced.");}
				else {
					JOptionPane.showMessageDialog(frameManager, "File Error.");
				}
				}
			}
			else if (comboBox.getSelectedIndex() == 5) {
				System.out.println(comboBox.getSelectedItem().toString());
				ManagerTool.clearRecord();
				System.exit(1);
				
			}
			else {
				System.out.println("Unknow selected item");
			}
			
		});
		comboBox.setBounds(656, 123, 158, 41);
		frameManager.getContentPane().add(comboBox);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(637, 0, 193, 522);
		frameManager.getContentPane().add(panel);
		
		frameManager.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ManagerTool.clearRecord();
			}
			
			@Override
			public void windowOpened(WindowEvent e) {
				ManagerTool.clearRecord();
			}
		});
	}
	}

