package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class GuiButton extends Gui {
	private final Color BUTTON_BG_COLOR = new Color(0x555555);
	
	private int xpos = 0;
	private int ypos = 0;
	private int width = 100;
	private int height = 30;
	private String text = "";
	
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
