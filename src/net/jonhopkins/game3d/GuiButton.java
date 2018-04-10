package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class GuiButton extends Gui {
	public GuiButton(Game3D main, GuiMenu menu, int x, int y, String s) {
		super(main);
		xpos = x;
		ypos = y;
		offsetx = menu.offsetx;
		offsety = menu.offsety;
		text = s;
	}
	public void draw() {
		Graphics g = main.getGraphics();
		
		g.setColor(new Color(0x555555));
		g.fillRect(offsetx + xpos, offsety + ypos, width, height);
		
		g.setColor(Color.white);
		g.drawString(text, offsetx + xpos + (width - text.length() * 6) / 2, offsety + ypos + height / 2);
	}
	
	public boolean isPressed(int x, int y) {
		return (x >=  offsetx + xpos && x <= offsetx + xpos + width 
				&& y >= offsety + ypos && y <= offsety + ypos + height);
	}
	
	int xpos = 0;
	int ypos = 0;
	int width = 100;
	int height = 30;
	String text = "";
}