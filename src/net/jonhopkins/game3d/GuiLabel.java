package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class GuiLabel extends Gui{
	public GuiLabel(Game3D main, GuiMenu menu, int x, int y, String s) {
		super(main);
		xpos = x;
		ypos = y;
		offsetx = menu.offsetx;
		offsety = menu.offsety;
		text = s;
	}
	public void draw() {
		Graphics g = main.getGraphics();
		
		g.setColor(Color.white);
		g.drawString(text, offsetx + xpos + (width - text.length() * 6) / 2, offsety + ypos + height / 2);
	}
	
	int xpos = 0;
	int ypos = 0;
	int width = 100;
	int height = 30;
	String text = "";
}