/**
 * Class: Printer
 * Purpose: used to repaint canvas based on the draw records
 * Author: Hongzhuan Zhu, 1223535
 * **/

package server;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Printer extends JPanel {
	// -4855269534203900800
	private static final long serialVersionUID = 1L;
	private ArrayList<String> drawHistory = new ArrayList<String>();

	public void setList(ArrayList<String> drawHistory) {
		this.drawHistory = drawHistory;
	}

	@Override
	public void paint(Graphics gr) {
		super.paint(gr);
		draw((Graphics2D) gr, this.drawHistory);
	}

	public void draw(Graphics2D graphics2d, ArrayList<String> drawHistory) {
		// convert
		String[] drawArray = drawHistory.toArray(new String[drawHistory.size()]);
		// Break
		for (String commandLine : drawArray) {

			String[] command = commandLine.split(":");
			//System.out.println(commandLine);
			int fromX, fromY, toX, toY;
			//int thickness;
			int red, green, blue;
			String text;
			Color color;
			/*if(command[1].equals("^")) {
				continue;
			}*/

			switch (command[0]) {
			case "Line": {
				red = Integer.parseInt(command[1]);
				green = Integer.parseInt(command[2]);
				blue = Integer.parseInt(command[3]);
				color = new Color(red, green, blue);
				graphics2d.setColor(color);
				fromX = Integer.parseInt(command[4]);
				fromY = Integer.parseInt(command[5]);
				toX = Integer.parseInt(command[6]);
				toY = Integer.parseInt(command[7]);
				graphics2d.drawLine(fromX, fromY, toX, toY);
				break;
				}
			case "Circle":{
				red = Integer.parseInt(command[1]);
				green = Integer.parseInt(command[2]);
				blue = Integer.parseInt(command[3]);
				color = new Color(red, green, blue);
				graphics2d.setColor(color);
				fromX = Integer.parseInt(command[4]);
				fromY = Integer.parseInt(command[5]);
				toX = Integer.parseInt(command[6]);
				toY = Integer.parseInt(command[7]);
				graphics2d.drawOval(Math.min(fromX, toX), Math.min(fromY, toY), Math.abs(fromX - toX),
						Math.abs(fromY - toY));
				break;
				
			}
			case "Rectangle":{
				red = Integer.parseInt(command[1]);
				green = Integer.parseInt(command[2]);
				blue = Integer.parseInt(command[3]);
				color = new Color(red, green, blue);
				graphics2d.setColor(color);
				fromX = Integer.parseInt(command[4]);
				fromY = Integer.parseInt(command[5]);
				toX = Integer.parseInt(command[6]);
				toY = Integer.parseInt(command[7]);
				graphics2d.drawRect(Math.min(fromX, toX), Math.min(fromY, toY), Math.abs(fromX - toX),
						Math.abs(fromY - toY));
				break;
				
			}
			case "Text":{
				Font font = new Font(null, Font.PLAIN, 12);
				graphics2d.setFont(font);
				red = Integer.parseInt(command[1]);
				green = Integer.parseInt(command[2]);
				blue = Integer.parseInt(command[3]);
				color = new Color(red, green, blue);
				graphics2d.setColor(color);
				text = command[4];
				toX = Integer.parseInt(command[5]);
				toY = Integer.parseInt(command[6]);
				graphics2d.drawString(text, toX, toY);
				break;
			}
			
			case "Triangle":{
				Font font = new Font(null, Font.PLAIN, 12);
				graphics2d.setFont(font);
				red = Integer.parseInt(command[1]);
				green = Integer.parseInt(command[2]);
				blue = Integer.parseInt(command[3]);
				color = new Color(red, green, blue);
				graphics2d.setColor(color);
				int x1 = Integer.parseInt(command[4]);
				int x2 = Integer.parseInt(command[5]);
				int x3 = Integer.parseInt(command[6]);
				int x[] = { x1,x2,x3};
				int y1 = Integer.parseInt(command[7]);
				int y2 = Integer.parseInt(command[8]);
				int y3 = Integer.parseInt(command[9]);
				int y[] = { y1,y2,y3};
				graphics2d.drawPolygon(x, y, 3);
				break;
			}

			default:
				System.out.println("Unknow command - Printer class");
				break;
			}
		}

	}

}
