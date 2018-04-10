package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapEditor {
	public MapEditor(int x, int y) {
		String pointsfile = (new StringBuilder("sectors/points/Sector_")).append(x).append('_').append(y).append("_points.png").toString();
		String tilesfile = (new StringBuilder("sectors/tiles/Sector_")).append(x).append('_').append(y).append("_tiles.png").toString();
		
		pts = null;
		try {
			pts = ImageIO.read(getClass().getClassLoader().getResourceAsStream(pointsfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
		ts = null;
		try {
			ts = ImageIO.read(getClass().getClassLoader().getResourceAsStream(tilesfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		scale = 3;
	}
	
	public void drawHUD(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 200, 400);
		
		g.drawImage(pts, 2, 1, (SECTOR_WIDTH + 1) * scale, (SECTOR_HEIGHT + 1) * scale, null);
		g.drawImage(ts, 2, 1 + SECTOR_HEIGHT * scale + 5, SECTOR_WIDTH * scale, SECTOR_HEIGHT * scale, null);
		
		g.setColor(Color.black);
		g.drawString(new StringBuilder("").append(raisePoint).toString(), 1, SECTOR_HEIGHT * 2 * scale + 16);
		g.drawString(new StringBuilder("Last height change: ").append(lastHeight).toString(), 10, SECTOR_HEIGHT * 2 * scale + 16);
	}
	
	public void changeRaisePoint(char rp) {
		if (rp == '+') {
			if (raisePoint == 10) return;
			raisePoint++;
		} else
		if (rp == '-') {
			if (raisePoint == 1) return;
			raisePoint--;
		}
	}
	public void raisePoint(Point3D position, MapSector s1, int direction) {
		if (position == null) {
			return;
		}
		
		int x = (int)position.x + 32;
		int y = 64 - ((int)position.z + 32);
		
		int oldHeight = pts.getRGB(x, y);
		
		int a = (oldHeight & 0xff000000);
		int r = (oldHeight & 0xff0000) >> 16;
		int g = (oldHeight & 0xff00) >> 8;
		int b = (oldHeight & 0xff);
		
		r += (raisePoint * direction);
		g += (raisePoint * direction);
		b += (raisePoint * direction);
		position.y += (raisePoint * direction);
		
		lastHeight = b - 128;
		
		int newHeight = a + (r << 16) + (g << 8) + (b);
		
		s1.setPoint(x, y, newHeight);
		pts.setRGB(x, y, newHeight);
	}
	
	public Point3D getPosition(int x, int y) {
		x = x / scale;
		y = y / scale;
		if (x < 0 || x > 64 || y < 0 || y > 64) {
			return null;
		}
		
		return new Point3D(x - 32, (pts.getRGB(x, y) & 0xff) - 128, 32 - y);
	}
	
	public void save(int x, int y) {
		/*
		File f = new File((new StringBuilder("sectors/points/Sector_")).append(x).append('_').append(y).append("_points.png").toString());
		
		try {
			ImageIO.write(pts, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private BufferedImage pts;
	private BufferedImage ts;
	private int scale;
	private int raisePoint = 1;
	private int lastHeight;
	private final int SECTOR_WIDTH = 64;
	private final int SECTOR_HEIGHT = 64;
}