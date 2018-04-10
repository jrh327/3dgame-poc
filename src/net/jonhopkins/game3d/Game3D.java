package net.jonhopkins.game3d;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Calendar;

public class Game3D extends Applet implements Runnable{
	public Game3D() {
		cameraHeight = 10;
		viewingDistance = 320;
		camera = new Point3D(0D, cameraHeight, 0D);
		mousePosition = new Point3D(0, 0, 0);
		mapeditor = new MapEditor(0, 0);
		rotatex = 0;
		rotatey = 0;
		halfScreenX = 300;
		halfScreenY = 200;
	}
	
	public void start() {
		Thread thread = new Thread(this, "Main thread");
		thread.start();
	}
	
	public void init() {
		resize(600, 400);
		s1 = new MapSector(0, 0);
		getPoints();
		getTiles();
		setBackground(new Color(0x999999));
		buffer = createImage(2 * halfScreenX, 2 * halfScreenY);
		bufferGraphics = buffer.getGraphics();
		setVisible(true);
		keyboard = new KeyboardInput();
		mouse = new MouseInput(this);
		mouse.setRelative(true);
		addKeyListener(keyboard);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		requestFocus();
		
		int hour = java.util.Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = java.util.Calendar.getInstance().get(Calendar.MINUTE);
		int sec = java.util.Calendar.getInstance().get(Calendar.SECOND);
		tod = ((60 * hour + min) % 24) * 60 + sec;
		if (tod > 1439) tod = 0;
		
		setGameRunning(false);
		menu = new GuiMenu(this);
		menu.draw();
	}
	
	public void drawSector() {
		Point3D[] tempPoints = s1.getPoints();
		MapTile[] tempTiles = s1.getTiles(tempPoints);
		DrawingPreparation.translatePointsWithRespectToCamera(tempPoints, camera);
		if (rotatey != 0) DrawingPreparation.rotatePointsY(tempPoints, rotatey);
		if (rotatex != 0) DrawingPreparation.rotatePointsX(tempPoints, rotatex);
		DrawingPreparation.Quicksort(tempTiles, 0, tempTiles.length - 1);
		bufferGraphics.clearRect(0, 0, 600, 400);
		
		int closestToMouse = DrawScene.drawScene(tempPoints, tempTiles, camera, cameraHeight, viewingDistance, halfScreenX, halfScreenY, bufferGraphics, tod, rotatex, rotatey);
		
		if (closestToMouse >= 0) {
			int[] xs = new int[4];
			int[] ys = new int[4];
			tempTiles[closestToMouse].to2DCoords(halfScreenX, halfScreenY, xs, ys);
			bufferGraphics.setColor(Color.black);
			bufferGraphics.drawPolygon(xs, ys, 4);
			
			int bestDist = 1000;
			int index = 0;
			
			for (int i = 0; i < 4; i++) {
				int dist = (int)Math.sqrt((xs[i] - 300) * (xs[i] - 300) + (ys[i] - 200) * (ys[i] - 200));
				if (dist < bestDist) {
					bestDist = dist;
					index = i;
				}
			}
			
			bufferGraphics.setColor(Color.red);
			bufferGraphics.drawRect(xs[index], ys[index], 3, 3);
			
			if (index == 0) {
				mousePosition.x = tempTiles[closestToMouse].UL.x / 10;
				mousePosition.y = tempTiles[closestToMouse].UL.y / 10;
				mousePosition.z = tempTiles[closestToMouse].UL.z / 10;
			} else if (index == 1) {
				mousePosition.x = tempTiles[closestToMouse].UR.x / 10;
				mousePosition.y = tempTiles[closestToMouse].UR.y / 10;
				mousePosition.z = tempTiles[closestToMouse].UR.z / 10;
			} else if (index == 2) {
				mousePosition.x = tempTiles[closestToMouse].LR.x / 10;
				mousePosition.y = tempTiles[closestToMouse].LR.y / 10;
				mousePosition.z = tempTiles[closestToMouse].LR.z / 10;
			} else if (index == 3) {
				mousePosition.x = tempTiles[closestToMouse].LL.x / 10;
				mousePosition.y = tempTiles[closestToMouse].LL.y / 10;
				mousePosition.z = tempTiles[closestToMouse].LL.z / 10;
			}
			mousePosition.rotateY(-rotatey);
			mousePosition.x += camera.x / 10;
			mousePosition.y += camera.y / 10;
			mousePosition.z += camera.z / 10;
			mousePosition.x = Math.round(mousePosition.x);
			mousePosition.y = Math.round(mousePosition.y);
			mousePosition.z = Math.round(mousePosition.z);
		}
		
		bufferGraphics.setColor(Color.white);
		
		bufferGraphics.drawLine(halfScreenX, halfScreenY - 10, halfScreenX, halfScreenY + 10);
		bufferGraphics.drawLine(halfScreenX - 10, halfScreenY, halfScreenX + 10, halfScreenY);
		
		bufferGraphics.drawString((new StringBuilder(String.valueOf(camera.x))).append(", ").append(camera.y).append(", ").append(camera.z).toString(), 10, 10);
		bufferGraphics.drawString((new StringBuilder("rotatex: ")).append(rotatex).toString(), 10, 20);
		bufferGraphics.drawString((new StringBuilder("rotatey: ")).append(rotatey).toString(), 10, 30);
		bufferGraphics.drawString(new StringBuilder("pointing at: ").append(mousePosition.x).append(", ").append(mousePosition.y).append(", ").append(mousePosition.z).toString(), 10, 40);
		bufferGraphics.drawString((new StringBuilder("")).append(fps).append(" fps").toString(), 10, 50);
		bufferGraphics.drawString((new StringBuilder("Time of Day: ")).append(tod / 60).append(":").append(new String("00").substring(new Integer(tod % 60).toString().length())).append((int)tod % 60).toString(), 10, 60);
	}
	
	public void getPoints() {
		points = s1.getPoints();
	}
	public void getTiles() {
		tiles = s1.getTiles(points);
	}
	
	public void run() {
		short ticks = 0;
		long timesofar = 0;
		
		try {
			while (true) {
				keyboard.poll();
				mouse.poll();
				
				if (gameIsRunning) {
					long time = System.nanoTime();
					
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
					
					timesofar += ((System.nanoTime() - time) / 1000000);
					if (ticks == 10) {
						fps = 1000 / (int)(timesofar / framecount);
						ticks = 0;
						timesofar = 0;
						framecount = 0;
						int hour = java.util.Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						int min = java.util.Calendar.getInstance().get(Calendar.MINUTE);
						int sec = java.util.Calendar.getInstance().get(Calendar.SECOND);
						tod = ((60 * hour + min) % 24) * 60 + sec;
						if (tod > 1439) tod = 0;
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
			
			if (keyboard.keyDown(KeyEvent.VK_W)) {
				double tempX = camera.x;
				double tempZ = camera.z;
				if(camera.z + Math.cos(rotatey * Math.PI / 180) <= 32D && camera.z + Math.cos(rotatey * Math.PI / 180) >= -32D ) {
					tempZ += Math.cos(rotatey * Math.PI / 180) * speedz;
					if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
						camera.z = tempZ;
					}
				}
				if (camera.x - Math.sin(rotatey * Math.PI / 180) <= 32D && camera.x - Math.sin(rotatey * Math.PI / 180) >= -32D) {
					tempX -= Math.sin(rotatey * Math.PI / 180) * speedx;
					if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
						camera.x = tempX;
					}
				}
				camera.y = tiles[(int)(64 - (camera.z + 32)) * 64 + (int)(camera.x + 32)].avgHeight() + cameraHeight;
			}
			if (keyboard.keyDown(KeyEvent.VK_A)) {
				double tempX = camera.x;
				double tempZ = camera.z;
				if(camera.x - Math.cos(rotatey * Math.PI / 180) <= 32D && camera.x - Math.cos(rotatey * Math.PI / 180) >= -32D){
					tempX -= Math.cos(rotatey * Math.PI / 180) * speedx;
					if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
						camera.x = tempX;
					}
				}
				if (camera.z - Math.sin(rotatey * Math.PI / 180) <= 32D && camera.z - Math.sin(rotatey * Math.PI / 180) >= -32D) {
					tempZ -= Math.sin(rotatey * Math.PI / 180) * speedz;
					if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
						camera.z = tempZ;
					}
				}
				camera.y = tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight;
			}
			if (keyboard.keyDown(KeyEvent.VK_S)) {
				if (keyboard.keyDown(KeyEvent.VK_CONTROL)) {
					//mapeditor.save(0, 0);
				} else {
					double tempX = camera.x;
					double tempZ = camera.z;
					if(camera.z - Math.cos(rotatey * Math.PI / 180) <= 32D && camera.z - Math.cos(rotatey * Math.PI / 180) >= -32D) {
						tempZ -= Math.cos(rotatey * Math.PI / 180) * speedz;
						if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
							camera.z = tempZ;
						}
					}
					if( camera.x + Math.sin(rotatey * Math.PI / 180) <= 32D && camera.x + Math.sin(rotatey * Math.PI / 180) >= -32D) {
						tempX += Math.sin(rotatey * Math.PI / 180) * speedx;
						if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
							camera.x = tempX;
						}
					}
					camera.y = tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight;
				}
			}
			if (keyboard.keyDown(KeyEvent.VK_D)) {
				double tempX = camera.x;
				double tempZ = camera.z;
				if(camera.x + Math.cos(rotatey * Math.PI / 180) <= 32D && camera.x + Math.cos(rotatey * Math.PI / 180) >= -32D) {
					tempX += Math.cos(rotatey * Math.PI / 180) * speedx;
					if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
						camera.x = tempX;
					}
				}
				if (camera.z + Math.sin(rotatey * Math.PI / 180) <= 32D && camera.z + Math.sin(rotatey * Math.PI / 180) >= -32D) {
					tempZ += Math.sin(rotatey * Math.PI / 180) * speedz;
					if (tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight - camera.y <= 2) {
						camera.z = tempZ;
					}
				}
				camera.y = tiles[(int)(64 - (tempZ + 32)) * 64 + (int)(tempX + 32)].avgHeight() + cameraHeight;
			}
			
			camera.x *= 10;
			camera.z *= 10;
			
			if (keyboard.keyDown(KeyEvent.VK_PLUS)) {
				mapeditor.changeRaisePoint('+');
			} else
			if (keyboard.keyDown(KeyEvent.VK_MINUS)) {
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
			
			if (rotatey < 0) rotatey += 360;
			else if (rotatey >= 360) rotatey -= 360;
			if (rotatex < -89) rotatex = -89;
			else if (rotatex > 89) rotatex = 89;
			
			if (mouse.buttonDown(1))
				mapeditor.raisePoint(mousePosition, s1, 1);
			if (mouse.buttonDown(3))
				mapeditor.raisePoint(mousePosition, s1, -1);
		} else {
			if (mouse.buttonDown(1)) {
				Point p = mouse.getPosition();
				menu.click(600, 400, p.x, p.y);
			}
		}
	}
	
	public void update(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}
	
	public void paint(Graphics g) {
		
	}
	
	public void setGameRunning(boolean running) {
		gameIsRunning = running;
		
		if(gameIsRunning) {
			mouse.setRelative(true);
			mouse.disableCursor();
		} else {
			bufferGraphics.setColor(new Color(0, 0, 0, 150));
			bufferGraphics.fillRect(0, 0, 2 * halfScreenX, 2 * halfScreenY);
			repaint();
			mouse.setRelative(false);
			mouse.enableCursor();
		}
	}
	
	private boolean gameIsRunning;
	
	private int tod = 0;
	
	private GuiMenu menu;
	private MapSector s1;
	private Image buffer;
	private Graphics bufferGraphics;
	private MapEditor mapeditor;
	private Point3D camera;
	private Point3D mousePosition;
	private int rotatex;
	private int rotatey;
	private double speedx = 1;
	private double speedz = 1;
	private int fps = 0;
	private int framecount = 0;
	private double cameraHeight;
	private int viewingDistance;
	private Point3D[] points;
	private MapTile[] tiles;
	private int halfScreenX;
	private int halfScreenY;
	private KeyboardInput keyboard;
	private MouseInput mouse;
	private static final long serialVersionUID = -429272861506526060L;
}