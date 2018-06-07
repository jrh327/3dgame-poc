package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.gui.Menu;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.input.MouseInput;
import net.jonhopkins.game3d.model.Drawable;
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
	private double speedx = 10;
	private double speedz = 10;
	private double camVertSpeed = 50;
	private double camHorizSpeed = 50;
	private double cameraHeight;
	private int viewingDistance;
	private int halfScreenX;
	private int halfScreenY;
	private KeyboardInput keyboard;
	private MouseInput mouse;
	private Renderer renderer;
	private Scene scene;
	
	public static void main(String[] args) {
		new Game3D().init();
	}
	
	public Game3D() {
		cameraHeight = 10;
		viewingDistance = 320;
		camera = new Camera(new Vertex(0D, cameraHeight, 0D), new Vector());
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
		
		keyboard = new KeyboardInput();
		addKeyListener(keyboard);
		
		mouse = new MouseInput(this);
		mouse.setRelative(true);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		setFocusable(true);
	}
	
	public void initialize() {
		setGameRunning(false);
		
		renderer = new Renderer(bufferGraphics);
		renderer.setCamera(camera);
		renderer.setDimensions(halfScreenX * 2, halfScreenY * 2);
		renderer.setRenderDistance(viewingDistance);
		scene = new MainScene();
		
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
				keyboard.poll();
				mouse.poll();
				
				long startOfFrame = System.currentTimeMillis();
				double timestep = (startOfFrame - lastFrame) / 1000.0;
				
				if (gameIsRunning) {
					scene.update(timestep);
					
					if (keyboard.keyDown(KeyEvent.VK_ESCAPE)
							|| (keyboard.keyDown(KeyEvent.VK_CONTROL) && keyboard.keyDown(KeyEvent.VK_C))) {
						setGameRunning(false);
						break;
					} else if (keyboard.keyDown(KeyEvent.VK_P)){
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
			camera.position.x /= 10;
			camera.position.z /= 10;
			
			double tempX = camera.position.x;
			double tempZ = camera.position.z;
			double cosY = Math.cos(camera.rotation.y * Math.PI / 180.0);
			double sinY = Math.sin(camera.rotation.y * Math.PI / 180.0);
			
			if (keyboard.keyDown(KeyEvent.VK_W)) {
				tempZ += cosY * speedz * timestep;
				tempX -= sinY * speedx * timestep;
			}
			if (keyboard.keyDown(KeyEvent.VK_A)) {
				tempX -= cosY * speedx * timestep;
				tempZ -= sinY * speedz * timestep;
			}
			if (keyboard.keyDown(KeyEvent.VK_S)) {
				if (keyboard.keyDown(KeyEvent.VK_CONTROL)) {
					//mapeditor.save(0, 0);
				} else {
					tempZ -= cosY * speedz * timestep;
					tempX += sinY * speedx * timestep;
				}
			}
			if (keyboard.keyDown(KeyEvent.VK_D)) {
				tempX += cosY * speedx * timestep;
				tempZ += sinY * speedz * timestep;
			}
			
			if (tempX <= -32.0) {
				tempX = -32.0;
			} else if (tempX >= 32.0) {
				tempX = 31.99;
			}
			if (tempZ <= -32.0) {
				tempZ = -31.999;
			} else if (tempZ >= 32.0) {
				tempZ = 32.0;
			}
			
			camera.position.x = tempX;
			camera.position.z = tempZ;
			
			int tileIndex = (int)(64 - (camera.position.z + 32)) * 64 * 2 + (int)(camera.position.x + 32) * 2;
			camera.position.y = ((Drawable)s1.getChild("sector")).getFaces().get(tileIndex).avgY() + cameraHeight;
			
			camera.position.x *= 10;
			camera.position.z *= 10;
			
			if (keyboard.keyDown(KeyEvent.VK_PLUS)) {
				mapeditor.changeRaisePoint('+');
			} else if (keyboard.keyDown(KeyEvent.VK_MINUS)) {
				mapeditor.changeRaisePoint('-');
			}
			
			if (keyboard.keyDown(KeyEvent.VK_UP)) {
				camera.rotation.x += Math.ceil(camVertSpeed * timestep);
			}
			if (keyboard.keyDown(KeyEvent.VK_DOWN)) {
				camera.rotation.x -= Math.ceil(camVertSpeed * timestep);
			}
			if (keyboard.keyDown(KeyEvent.VK_LEFT)) {
				camera.rotation.y += Math.ceil(camHorizSpeed * timestep);
			}
			if (keyboard.keyDown(KeyEvent.VK_RIGHT)) {
				camera.rotation.y -= Math.ceil(camHorizSpeed * timestep);
			}
			
			//Point p = mouse.getPosition();
			//rotatey -= p.x;
			//rotatex -= p.y;
			
			if (camera.rotation.y < 0) {
				camera.rotation.y += 360;
			} else if (camera.rotation.y >= 360) {
				camera.rotation.y -= 360;
			}
			if (camera.rotation.x < -89) {
				camera.rotation.x = -89;
			} else if (camera.rotation.x > 89) {
				camera.rotation.x = 89;
			}
			
			if (mouse.buttonDown(1)) {
				mapeditor.raisePoint(mousePosition, s1, 1);
			}
			if (mouse.buttonDown(3)) {
				mapeditor.raisePoint(mousePosition, s1, -1);
			}
		} else {
			if (mouse.buttonDown(1)) {
				Point p = mouse.getPosition();
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
			mouse.setRelative(true);
			mouse.disableCursor();
		} else {
			bufferGraphics.setColor(PAUSED_OVERLAY_COLOR);
			bufferGraphics.fillRect(0, 0, 2 * halfScreenX, 2 * halfScreenY);
			repaint();
			mouse.setRelative(false);
			mouse.enableCursor();
		}
	}
}
