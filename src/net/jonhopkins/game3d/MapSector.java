package net.jonhopkins.game3d;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapSector {
	public MapSector(int x, int y) {
		setPointsFile(x, y);
		setTilesFile(x, y);
		
		pts = new int[(SECTOR_HEIGHT + 1) * (SECTOR_WIDTH + 1)];
		ts = new int[SECTOR_HEIGHT * SECTOR_WIDTH];
		
		pts = bImgPoints.getRGB(0, 0, SECTOR_WIDTH + 1, SECTOR_HEIGHT + 1, pts, 0, SECTOR_WIDTH + 1);
		ts = bImgTiles.getRGB(0, 0, SECTOR_WIDTH, SECTOR_HEIGHT, ts, 0, SECTOR_WIDTH);
		
		points = new Point3D[(SECTOR_HEIGHT + 1) * (SECTOR_WIDTH + 1)];
		tiles = new MapTile[SECTOR_HEIGHT * SECTOR_WIDTH];
		
		setPoints();
		setTiles();
	}
	
	public int[] getpts() {
		return pts;
	}
	
	public int[] getts() {
		return ts;
	}
	
	public Point3D[] getPoints() {
		Point3D[] p2 = new Point3D[points.length];
		
		for (int i = 0; i < p2.length; i++) {
			p2[i] = new Point3D(points[i].x, points[i].y, points[i].z);
		}
		
		return p2;
	}
	
	public MapTile[] getTiles() {
		MapTile[] t2 = new MapTile[tiles.length];
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				t2[i * 64 + j] = new MapTile(new Point3D(points[i * 65 + j].x, points[i * 65 + j].y, points[i * 65 + j].z),
						new Point3D(points[i * 65 + j + 1].x, points[i * 65 + j + 1].y, points[i * 65 + j + 1].z),
						new Point3D(points[(i + 1) * 65 + j].x, points[(i + 1) * 65 + j].y, points[(i + 1) * 65 + j].z),
						new Point3D(points[(i + 1) * 65 + j + 1].x, points[(i + 1) * 65 + j + 1].y, points[(i + 1) * 65 + j + 1].z),
						ts[i * 64 + j]);
			}
		}
		
		return t2;
	}
	
	public MapTile[] getTiles(Point3D[] points) {
		MapTile[] t2 = new MapTile[tiles.length];
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				t2[i * 64 + j] = new MapTile(points[i * 65 + j], points[i * 65 + j + 1], points[(i + 1) * 65 + j], points[(i + 1) * 65 + j + 1], ts[i * 64 + j]);
			}
		}
		
		return t2;
	}
	
	public void setTilesFile(int x, int y) {
		String tilesfile = (new StringBuilder("sectors/tiles/Sector_")).append(x).append('_').append(y).append("_tiles.png").toString();
		
		bImgTiles = null;
		try {
			bImgTiles = ImageIO.read(getClass().getClassLoader().getResourceAsStream(tilesfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void setPointsFile(int x, int y) {
		String pointsfile = (new StringBuilder("sectors/points/Sector_")).append(x).append('_').append(y).append("_points.png").toString();
		
		bImgPoints = null;
		try {
			bImgPoints = ImageIO.read(getClass().getClassLoader().getResourceAsStream(pointsfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void setPoints() {
		for(int i = 0; i < SECTOR_HEIGHT + 1; i++) {
			for(int j = 0; j < SECTOR_WIDTH + 1; j++) {
				points[i * 65 + j] = new Point3D((j - (SECTOR_WIDTH / 2)) * 10, (pts[i * (SECTOR_HEIGHT + 1) + j] & 0xff) - 128, (SECTOR_HEIGHT / 2 - i) * 10);
			}
		}
	}
	public void setTiles() {
		for(int i = 0; i < SECTOR_HEIGHT; i++) {
			for(int j = 0; j < SECTOR_WIDTH; j++) {
				tiles[i * 64 + j] = new MapTile(points[i * 65 + j], points[i * 65 + j + 1], points[(i + 1) * 65 + j], points[(i + 1) * 65 + j + 1], ts[i * 64 + j]);
			}
		}
	}
	
	public void setPoint(int x, int y, int rgb) {
		bImgPoints.setRGB(x, y, rgb);
		pts[y * (SECTOR_HEIGHT + 1) + x] = rgb;
		setPoints();
	}
	public void setTile(int x, int y, int rgb) {
		bImgTiles.setRGB(x, y, rgb);
		setTiles();
	}
	
	public BufferedImage getPointFile() {
		return bImgPoints;
	}
	public BufferedImage getTilesFile() {
		return bImgTiles;
	}
	
	private int pts[];
	private int ts[];
	private BufferedImage bImgPoints;
	private BufferedImage bImgTiles;
	private Point3D points[];
	private MapTile tiles[];
	private final int SECTOR_WIDTH = 64;
	private final int SECTOR_HEIGHT = 64;
}