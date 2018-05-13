package net.jonhopkins.game3d.gui;

import java.awt.Graphics;

import net.jonhopkins.game3d.Game3D;

public class Menu extends GUI {
	private Game3D main;
	private Button button1;
	private Button button2;
	
	public Menu(Game3D main, Graphics g) {
		super(g);
		
		this.main = main;
		
		offsetx = main.getWidth() / 2 - width / 2;
		offsety = main.getHeight() / 2 - height / 2;
		
		button1 = new Button(g, this, 20, 200, "Play game");
		button2 = new Button(g, this, 180, 200, "Exit");
		fields.add(button1);
		fields.add(button2);
		fields.add(new Label(g, this, 40, 100, "This is a game."));
	}
	
	public void click(int w, int h, int x, int y) {
		if (button2.isPressed(x, y)) {
			System.exit(0);
		} else if (button1.isPressed(x, y)) {
			main.setGameRunning(true);
		}
	}
}
