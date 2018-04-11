package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class GuiLabel extends Gui {
	private final Color LABEL_TEXT_COLOR = Color.white;
	
	private int xpos = 0;
	private int ypos = 0;
	private int width = 100;
	private int height = 30;
	private String text = "";
	
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
		
		g.setColor(LABEL_TEXT_COLOR);
		g.drawString(text, offsetx + xpos + (width - text.length() * 6) / 2, offsety + ypos + height / 2);
	}
}
