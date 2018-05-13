package net.jonhopkins.game3d.gui;

import java.awt.Color;
import java.awt.Graphics;

public class Button extends GUI {
	private final Color BUTTON_BG_COLOR = new Color(0x555555);
	
	private int xpos = 0;
	private int ypos = 0;
	private int width = 100;
	private int height = 30;
	private String text = "";
	
	public Button(Graphics g, Menu menu, int x, int y, String s) {
		super(g);
		xpos = x;
		ypos = y;
		offsetx = menu.offsetx;
		offsety = menu.offsety;
		text = s;
	}
	
	public void draw() {
		g.setColor(BUTTON_BG_COLOR);
		g.fillRect(offsetx + xpos, offsety + ypos, width, height);
		
		g.setColor(Color.white);
		g.drawString(text, offsetx + xpos + (width - text.length() * 6) / 2, offsety + ypos + height / 2);
	}
	
	public boolean isPressed(int x, int y) {
		return (x >=  offsetx + xpos && x <= offsetx + xpos + width 
				&& y >= offsety + ypos && y <= offsety + ypos + height);
	}
}
