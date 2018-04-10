package net.jonhopkins.game3d;

public class GuiMenu extends Gui {
	public GuiMenu(Game3D main) {
		super(main);
		
		offsetx = main.getWidth() / 2 - width / 2;
		offsety = main.getHeight() / 2 - height / 2;
		
		button1 = new GuiButton(main, this, 20, 200, "Play game");
		button2 = new GuiButton(main, this, 180, 200, "Exit");
		fields.add(button1);
		fields.add(button2);
		fields.add(new GuiLabel(main, this, 40, 100, "This is a game."));
	}
	
	public void click(int w, int h, int x, int y) {
		if (button2.isPressed(x, y)) {
			System.exit(0);
		} else if (button1.isPressed(x, y)) {
			main.setGameRunning(true);
		}
	}
	
	GuiButton button1;
	GuiButton button2;
}