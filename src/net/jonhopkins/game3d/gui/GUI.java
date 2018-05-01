package net.jonhopkins.game3d.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import net.jonhopkins.game3d.Game3D;

public abstract class GUI {
	private static final Color DEFAULT_BG_COLOR = new Color(0x777777);
	
	protected int width = 400;
	protected int height = 300;
	protected int offsetx;
	protected int offsety;
	protected Game3D main;
	protected ArrayList<GUI> fields = new ArrayList<GUI>();
	
	public GUI(Game3D main) {
		this.main = main;
	}
	
	public void draw() {
		Graphics g = main.getGraphics();
		
		g.setColor(DEFAULT_BG_COLOR);
		g.fillRect(offsetx, offsety, width, height);
		
		for (int i = 0; i < fields.size(); i++) {
			fields.get(i).draw();
		}
	}
}
