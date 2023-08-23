/*
 * Class: Listener
 * Author : Hongzhuan Zhu
 * Purpose: Listener the events happened in the ManagerBoard
 * */

package manager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Listener implements ActionListener, MouseMotionListener, MouseListener {

	private Graphics2D graphics2d;
	private JFrame frame;
	private int fromX, toX, fromY, toY;
	private ArrayList<Integer> triangleX = new ArrayList<>();
	private ArrayList<Integer> triangleY = new ArrayList<>();
	private Object drawCommand;
	static Color color = Color.black;
	private String rgb = "0,0,0";
	private boolean triangle = false;
	private String drawRecord;
	private String endCommand = "^";
	private String draw;
	private ArrayList<String> drawHistory = new ArrayList<>();
	private String state = null;

	public Listener(JFrame frame) {
		this.frame = frame;
	}

	public Listener() {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (state == "Triangle") {
			triangleX.add(e.getX());
			triangleY.add(e.getY());
			if (triangleX.size() == 3) {
				int x[] = { triangleX.get(0), triangleX.get(1), triangleX.get(2) };
				int y[] = { triangleY.get(0), triangleY.get(1), triangleY.get(2) };
				rgb = getColor(color);
				graphics2d.drawPolygon(x, y, 3);
				drawRecord = "Triangle" + ":" + rgb + ":" + triangleX.get(0) + ":" + triangleX.get(1) + ":"
						+ triangleX.get(2) + ":" + triangleY.get(0) + ":" + triangleY.get(1) + ":" + triangleY.get(2)
						+ ":" + endCommand;
				drawHistory.add(drawRecord);

				triangleX.clear();
				triangleY.clear();
				triangle = true;
			}
			if (triangle == true) {
				sendDraw();
				ManagerTool.reload(drawRecord);
				triangle = false;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		fromX = e.getX();
		fromY = e.getY();
		if (!graphics2d.getColor().equals(color)) {
			graphics2d.setColor(color);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		toX = e.getX();
		toY = e.getY();
		if (state != null && !state.equals("Triangle")) {
			switch (state) {
			case "Line": {
				rgb = getColor(color);
				graphics2d.drawLine(fromX, fromY, toX, toY);
				drawRecord = "Line" + ":" + rgb + ":" + fromX + ":" + fromY + ":" + toX + ":" + toY + ":" + endCommand;
				drawHistory.add(drawRecord);

				break;
			}
			case "Circle": {
				rgb = getColor(color);
				graphics2d.drawOval(Math.min(fromX, toX), Math.min(fromY, toY), Math.abs(fromX - toX),
						Math.abs(fromY - toY));
				drawRecord = "Circle" + ":" + rgb + ":" + fromX + ":" + fromY + ":" + toX + ":" + toY + ":"
						+ endCommand;
				drawHistory.add(drawRecord);

				break;
			}
			case "Triangle": {
				break;
			}
			case "Rectangle": {
				rgb = getColor(color);
				graphics2d.drawRect(Math.min(fromX, toX), Math.min(fromY, toY), Math.abs(fromX - toX),
						Math.abs(fromY - toY));
				drawRecord = "Rectangle" + ":" + rgb + ":" + fromX + ":" + fromY + ":" + toX + ":" + toY + ":"
						+ endCommand;
				drawHistory.add(drawRecord);
				break;
			}
			case "Text": {
				rgb = getColor(color);
				String text = JOptionPane.showInputDialog("Please input text: ");
				if (text != null) {
					if (text.contains(":") || text.contains(endCommand)) {
						JOptionPane.showMessageDialog(frame,
								"Invaild character deceted, Please make there is no character : and ^ in the text",
								"Error", JOptionPane.WARNING_MESSAGE);
					} else {
						Font font = new Font(null, Font.PLAIN, 12);
						graphics2d.setFont(font);
						graphics2d.drawString(text, toX, toY);
						drawRecord = "Text" + ":" + rgb + ":" + text + ":" + toX + ":" + toY + ":" + endCommand;
						drawHistory.add(drawRecord);

					}
				}
				break;
			}
			default:
				System.out.println("Unknow command: " + state);
			}
			sendDraw();
			ManagerTool.reload(drawRecord);
		}
	}

	public void sendDraw() {
		try {
			draw = "draw:" + drawRecord;
			ManagerTool.broadcast(draw);
		} catch (Exception e) {
			System.out.println("Unable to broadcast draw command");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		this.drawCommand = e.getActionCommand();

		if (drawCommand.equals("Line")) {
			Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
			frame.setCursor(cursor);
			state = (String) drawCommand;
			System.out.println("Current State: " + state);

		} else if (drawCommand.equals("Circle")) {
			Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
			frame.setCursor(cursor);
			state = (String) drawCommand;
			System.out.println("Current State: " + state);

		} else if (drawCommand.equals("Rectangle")) {
			Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
			frame.setCursor(cursor);
			state = (String) drawCommand;
			System.out.println("Current State: " + state);

		} else if (drawCommand.equals("Triangle")) {
			Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
			frame.setCursor(cursor);
			state = (String) drawCommand;
			JOptionPane.showMessageDialog(frame, "Please click three position in a row", "Triangle draw tutorial",
					JOptionPane.PLAIN_MESSAGE);
			triangleX.clear();
			triangleY.clear();
			System.out.println("Current State: " + state);

		} else if (drawCommand.equals("Text")) {
			Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			frame.setCursor(cursor);
			state = (String) drawCommand;
			System.out.println("Current State: " + state);

		} else if (drawCommand.equals("Color")) {
			colorPane();

		} else if (drawCommand.equals("Debug")) {
			System.out.println(Arrays.toString(drawHistory.toArray()));

		} else {
			System.out.println((String) drawCommand + "has been processed.");
		}
	}


	public void setGraphic(Graphics graphics) {
		this.graphics2d = (Graphics2D) graphics;
	}

	public ArrayList<String> getRecord() {
		return drawHistory;
	}

	public void update(String line) {
		drawHistory.add(line);
	}

	public void clearRecord() {
		drawHistory.clear();
	}

	public String getColor(Color color) {
		return color.getRed() + ":" + color.getGreen() + ":" + color.getBlue();
	}

	public void colorPane() {
		final JFrame colorFrame = new JFrame("Color Wheel");
		colorFrame.setSize(300, 300);
		colorFrame.setLocationRelativeTo(null);
		colorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Color currentColor = JColorChooser.showDialog(colorFrame, "Choose your color to draw", null);
		if (currentColor != null) {
			color = currentColor;
		}
	}

}
