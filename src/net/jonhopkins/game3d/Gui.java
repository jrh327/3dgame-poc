package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Gui {
	private final Color GUI_BG_COLOR = new Color(0x777777);
	
	protected int width = 400;
	protected int height = 300;
	protected int offsetx;
	protected int offsety;
	protected Game3D main;
	protected ArrayList<Gui> fields = new ArrayList<Gui>();
	
	public Gui(Game3D main) {
		this.main = main;
	}
	
	public void draw() {
		Graphics g = main.getGraphics();
		
		g.setColor(GUI_BG_COLOR);
		g.fillRect(offsetx, offsety, width, height);
		
		for (int i = 0; i < fields.size(); i++) {
			fields.get(i).draw();
		}
	}
}
