package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.gui.Menu;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.input.MouseInput;
import net.jonhopkins.game3d.object.MapSector;

public class Game3D extends JFrame implements Runnable {
	private static final long serialVersionUID = -429272861506526060L;
	private final Color GAME_BG_COLOR = new Color(0x999999);
	private final Color PAUSED_OVERLAY_COLOR = new Color(0, 0, 0, 150);
	
	private boolean gameIsRunning;
	
	private Menu menu;
	private MapSector s1;
	private Image buffer;
	private Graphics bufferGraphics;
	private MapEditor mapeditor;
	private Camera camera;
	private Vertex mousePosition;
	private int viewingDistance;
	private int halfScreenX;
	private int halfScreenY;
	private Renderer renderer;
	private Scene scene;
	
	public static void main(String[] args) {
		new Game3D().init();
	}
	
	public Game3D() {
		viewingDistance = 320;
		mousePosition = new Vertex();
		mapeditor = new MapEditor(0, 0);
		halfScreenX = 300;
		halfScreenY = 200;
	}
	
	public void init() {
		final Game3D game3d = this;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setUpWindow();
				setUpComponents();
				initialize();
				Thread thread = new Thread(game3d, "Main thread");
				thread.start();
			}
		});
	}
	
	public void setUpWindow() {
		setBackground(GAME_BG_COLOR);
		
		int width = halfScreenX * 2;
		int height = halfScreenY * 2;
		
		setSize(width, height);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void setUpComponents() {
		int width = halfScreenX * 2;
		int height = halfScreenY * 2;
		buffer = createImage(width, height);
		bufferGraphics = buffer.getGraphics();
		
		addKeyListener(KeyboardInput.getInstance());
		
		MouseInput mouseInput = MouseInput.getInstance();
		MouseInput.setComponent(this);
		MouseInput.setRelative(true);
		addMouseListener(mouseInput);
		addMouseMotionListener(mouseInput);
		setFocusable(true);
	}
	
	public void initialize() {
		setGameRunning(false);
		
		scene = new MainScene();
		camera = scene.getCamera();
		renderer = new Renderer(bufferGraphics);
		renderer.setCamera(camera);
		renderer.setDimensions(halfScreenX * 2, halfScreenY * 2);
		renderer.setRenderDistance(viewingDistance);
		
		s1 = MapSector.getMapSector(0, 0);
		
		menu = new Menu(this, bufferGraphics);
		menu.draw();
		
		requestFocus();
	}
	
	@Override
	public void run() {
		long lastFrame = System.currentTimeMillis();
		
		try {
			while (true) {
				KeyboardInput.poll();
				MouseInput.poll();
				
				long startOfFrame = System.currentTimeMillis();
				double timestep = (startOfFrame - lastFrame) / 1000.0;
				
				if (gameIsRunning) {
					scene.update(timestep);
					
					if (KeyboardInput.keyDown(KeyEvent.VK_ESCAPE)
							|| (KeyboardInput.keyDown(KeyEvent.VK_CONTROL) && KeyboardInput.keyDown(KeyEvent.VK_C))) {
						setGameRunning(false);
						break;
					} else if (KeyboardInput.keyDown(KeyEvent.VK_P)){
						setGameRunning(false);
						menu.draw();
						continue;
					} else {
						processInput(timestep);
					}
					
					synchronized (buffer) {
						renderer.renderScene(scene, timestep);
						repaint();
					}
					
					lastFrame = startOfFrame;
				} else {
					processInput(timestep);
				}
			}
		} finally {
			if (bufferGraphics != null) {
				bufferGraphics.dispose();
			}
		}
		
		System.exit(0);
	}
	
	public void processInput(double timestep) {
		if (gameIsRunning) {
			if (KeyboardInput.keyDown(KeyEvent.VK_PLUS)) {
				mapeditor.changeRaisePoint('+');
			} else if (KeyboardInput.keyDown(KeyEvent.VK_MINUS)) {
				mapeditor.changeRaisePoint('-');
			}
			
			if (MouseInput.buttonDown(1)) {
				mapeditor.raisePoint(mousePosition, s1, 1);
			}
			if (MouseInput.buttonDown(3)) {
				mapeditor.raisePoint(mousePosition, s1, -1);
			}
		} else {
			if (MouseInput.buttonDown(MouseInput.LEFT_BUTTON)) {
				Point p = MouseInput.getPosition();
				menu.click(600, 400, p.x, p.y);
			}
		}
	}
	
	@Override
	public void update(Graphics g) {
		
	}
	
	@Override
	public void paint(Graphics g) {
		synchronized (buffer) {
			g.drawImage(buffer, 0, 0, this);
		}
	}
	
	public void setGameRunning(boolean running) {
		gameIsRunning = running;
		
		if (gameIsRunning) {
			MouseInput.setRelative(true);
			MouseInput.disableCursor();
		} else {
			bufferGraphics.setColor(PAUSED_OVERLAY_COLOR);
			bufferGraphics.fillRect(0, 0, 2 * halfScreenX, 2 * halfScreenY);
			repaint();
			MouseInput.setRelative(false);
			MouseInput.enableCursor();
		}
	}
}
