package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.gui.Menu;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.input.MouseInput;
import net.jonhopkins.game3d.model.MapSector;

public class Game3D extends JFrame implements Runnable {
	private static final long serialVersionUID = -429272861506526060L;
	private final Color GAME_BG_COLOR = new Color(0x999999);
	private final Color PAUSED_OVERLAY_COLOR = new Color(0, 0, 0, 150);
	private final Color DEBUG_TEXT_COLOR = Color.white;
	private final Color DEBUG_TILE_OUTLINE_COLOR = Color.black;
	private final Color DEBUG_POINT_OUTLINE_COLOR = Color.red;
	
	private boolean gameIsRunning;
	private int tod = 0;
	
	private Menu menu;
	private MapSector s1;
	private Image buffer;
	private Graphics bufferGraphics;
	private MapEditor mapeditor;
	private Vertex camera;
	private Vertex mousePosition;
	private int rotatex;
	private int rotatey;
	private double speedx = 1;
	private double speedz = 1;
	private int fps = 0;
	private int framecount = 0;
	private double cameraHeight;
	private int viewingDistance;
	private int halfScreenX;
	private int halfScreenY;
	private KeyboardInput keyboard;
	private MouseInput mouse;
	
	public static void main(String[] args) {
		new Game3D().init();
	}
	
	public Game3D() {
		cameraHeight = 10;
		viewingDistance = 320;
		camera = new Vertex(0D, cameraHeight, 0D);
		mousePosition = new Vertex(0, 0, 0);
		mapeditor = new MapEditor(0, 0);
		rotatex = 0;
		rotatey = 0;
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
		
		s1 = MapSector.getMapSector(0, 0);
		
		updateTime();
		
		menu = new Menu(this, bufferGraphics);
		menu.draw();
		
		requestFocus();
	}
	
	public void drawSector() {
		Vertex[] tempPoints = s1.getVertices();
		Face[] tempTiles = s1.getFaces();
		DrawingPreparation.translatePointsWithRespectToCamera(tempPoints, camera);
		if (rotatey != 0) {
			DrawingPreparation.rotatePointsY(tempPoints, rotatey);
		}
		if (rotatex != 0) {
			DrawingPreparation.rotatePointsX(tempPoints, rotatex);
		}
		tempTiles = DrawingPreparation.backFaceCulling(tempTiles);
		DrawingPreparation.Quicksort(tempTiles, 0, tempTiles.length - 1);
		bufferGraphics.clearRect(0, 0, 600, 400);
		
		int closestToMouse = DrawScene.drawScene(tempTiles, camera, cameraHeight, viewingDistance, halfScreenX, halfScreenY, bufferGraphics, tod, rotatex, rotatey);
		
		if (closestToMouse >= 0) {
			Face closest = tempTiles[closestToMouse];
			int[] xs = new int[closest.vertices.length];
			int[] ys = new int[closest.vertices.length];
			closest.to2DCoords(halfScreenX, halfScreenY, xs, ys);
			bufferGraphics.setColor(DEBUG_TILE_OUTLINE_COLOR);
			bufferGraphics.drawPolygon(xs, ys, xs.length);
			
			int bestDist = 1000;
			int index = 0;
			
			for (int i = 0; i < xs.length; i++) {
				int dist = (int)Math.sqrt((xs[i] - 300) * (xs[i] - 300) + (ys[i] - 200) * (ys[i] - 200));
				if (dist < bestDist) {
					bestDist = dist;
					index = i;
				}
			}
			
			bufferGraphics.setColor(DEBUG_POINT_OUTLINE_COLOR);
			bufferGraphics.drawRect(xs[index], ys[index], 3, 3);
			
			Vertex vertex = tempTiles[closestToMouse].vertices[index];
			mousePosition.x = vertex.x / 10.0;
			mousePosition.y = vertex.y / 10.0;
			mousePosition.z = vertex.z / 10.0;
			mousePosition.rotateY(-rotatey);
			mousePosition.x += camera.x / 10.0;
			mousePosition.y += camera.y / 10.0;
			mousePosition.z += camera.z / 10.0;
			mousePosition.x = Math.round(mousePosition.x);
			mousePosition.y = Math.round(mousePosition.y);
			mousePosition.z = Math.round(mousePosition.z);
		}
		
		bufferGraphics.setColor(DEBUG_TEXT_COLOR);
		
		bufferGraphics.drawLine(halfScreenX, halfScreenY - 10, halfScreenX, halfScreenY + 10);
		bufferGraphics.drawLine(halfScreenX - 10, halfScreenY, halfScreenX + 10, halfScreenY);
		
		bufferGraphics.drawString((new StringBuilder(String.valueOf(camera.x))).append(", ").append(camera.y).append(", ").append(camera.z).toString(), 10, 10);
		bufferGraphics.drawString((new StringBuilder("rotatex: ")).append(rotatex).toString(), 10, 20);
		bufferGraphics.drawString((new StringBuilder("rotatey: ")).append(rotatey).toString(), 10, 30);
		bufferGraphics.drawString(new StringBuilder("pointing at: ").append(mousePosition.x).append(", ").append(mousePosition.y).append(", ").append(mousePosition.z).toString(), 10, 40);
		bufferGraphics.drawString((new StringBuilder("")).append(fps).append(" fps").toString(), 10, 50);
		bufferGraphics.drawString((new StringBuilder("Time of Day: ")).append(tod / 60).append(":").append(new String("00").substring(new Integer(tod % 60).toString().length())).append((int)tod % 60).toString(), 10, 60);
	}
	
	@Override
	public void run() {
		short ticks = 0;
		long timesofar = 0;
		
		try {
			while (true) {
				keyboard.poll();
				mouse.poll();
				
				if (gameIsRunning) {
					long time = System.currentTimeMillis();
					
					s1.resetVertices();
					
					if (keyboard.keyDown(KeyEvent.VK_ESCAPE) || (keyboard.keyDown(KeyEvent.VK_CONTROL) && keyboard.keyDown(KeyEvent.VK_C))) {
						setGameRunning(false);
						break;
					} else if (keyboard.keyDown(KeyEvent.VK_P)){
						setGameRunning(false);
						menu.draw();
						continue;
					} else {
						processInput();
					}
					
					framecount++;
					drawSector();
					repaint();
					
					timesofar += (System.currentTimeMillis() - time);
					if (ticks == 10) {
						fps = 1000 / (int)(timesofar / framecount);
						ticks = 0;
						timesofar = 0;
						framecount = 0;
						updateTime();
					}
					ticks++;
				} else {
					processInput();
				}
				try {
					Thread.sleep(50L);
				} catch(InterruptedException interruptedexception) { }
			}
		} finally {
			if (bufferGraphics != null) bufferGraphics.dispose();
		}
		
		System.exit(0);
	}
	
	public void processInput() {
		if (gameIsRunning) {
			camera.x /= 10;
			camera.z /= 10;
			
			double tempX = camera.x;
			double tempZ = camera.z;
			double cosY = Math.cos(rotatey * Math.PI / 180.0);
			double sinY = Math.sin(rotatey * Math.PI / 180.0);
			
			if (keyboard.keyDown(KeyEvent.VK_W)) {
				tempZ += cosY * speedz;
				tempX -= sinY * speedx;
			}
			if (keyboard.keyDown(KeyEvent.VK_A)) {
				tempX -= cosY * speedx;
				tempZ -= sinY * speedz;
			}
			if (keyboard.keyDown(KeyEvent.VK_S)) {
				if (keyboard.keyDown(KeyEvent.VK_CONTROL)) {
					//mapeditor.save(0, 0);
				} else {
					tempZ -= cosY * speedz;
					tempX += sinY * speedx;
				}
			}
			if (keyboard.keyDown(KeyEvent.VK_D)) {
				tempX += cosY * speedx;
				tempZ += sinY * speedz;
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
			
			camera.x = tempX;
			camera.z = tempZ;
			
			int tileIndex = (int)(64 - (camera.z + 32)) * 64 * 2 + (int)(camera.x + 32) * 2;
			camera.y = s1.getFaces()[tileIndex].avgY() + cameraHeight;
			
			camera.x *= 10;
			camera.z *= 10;
			
			if (keyboard.keyDown(KeyEvent.VK_PLUS)) {
				mapeditor.changeRaisePoint('+');
			} else if (keyboard.keyDown(KeyEvent.VK_MINUS)) {
				mapeditor.changeRaisePoint('-');
			}
			
			if (keyboard.keyDown(KeyEvent.VK_UP)) {
				rotatex += 5;
			}
			if (keyboard.keyDown(KeyEvent.VK_DOWN)) {
				rotatex -= 5;
			}
			if (keyboard.keyDown(KeyEvent.VK_LEFT)) {
				rotatey += 5;
			}
			if (keyboard.keyDown(KeyEvent.VK_RIGHT)) {
				rotatey -= 5;
			}
			
			//Point p = mouse.getPosition();
			//rotatey -= p.x;
			//rotatex -= p.y;
			
			if (rotatey < 0) {
				rotatey += 360;
			} else if (rotatey >= 360) {
				rotatey -= 360;
			}
			if (rotatex < -89) {
				rotatex = -89;
			} else if (rotatex > 89) {
				rotatex = 89;
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
		g.drawImage(buffer, 0, 0, this);
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
	
	public void updateTime() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		tod = ((60 * hour + min) % 24) * 60 + sec;
		if (tod > 1439) {
			tod = 0;
		}
	}
}
