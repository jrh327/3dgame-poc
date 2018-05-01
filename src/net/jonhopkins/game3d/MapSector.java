package net.jonhopkins.game3d;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;

public class MapSector {
	private int pts[];
	private int ts[];
	private BufferedImage bImgPoints;
	private BufferedImage bImgTiles;
	private Vertex points[];
	private Face tiles[];
	private final int SECTOR_WIDTH = 64;
	private final int SECTOR_HEIGHT = 64;
	
	private final String SECTORS_DIR = "sectors/";
	private final String TILES_DIR = SECTORS_DIR + "tiles/";
	private final String TILES_FILE = TILES_DIR + "Sector_%d_%d_tiles.png";
	private final String POINTS_DIR = SECTORS_DIR + "points/";
	private final String POINTS_FILE = POINTS_DIR + "Sector_%d_%d_points.png";
	
	public MapSector(int x, int y) {
		setPointsFile(x, y);
		setTilesFile(x, y);
		
		pts = new int[(SECTOR_HEIGHT + 1) * (SECTOR_WIDTH + 1)];
		ts = new int[SECTOR_HEIGHT * SECTOR_WIDTH];
		
		pts = bImgPoints.getRGB(0, 0, SECTOR_WIDTH + 1, SECTOR_HEIGHT + 1, pts, 0, SECTOR_WIDTH + 1);
		ts = bImgTiles.getRGB(0, 0, SECTOR_WIDTH, SECTOR_HEIGHT, ts, 0, SECTOR_WIDTH);
		
		points = new Vertex[(SECTOR_HEIGHT + 1) * (SECTOR_WIDTH + 1)];
		tiles = new Face[SECTOR_HEIGHT * SECTOR_WIDTH];
		
		setPoints();
		setTiles();
	}
	
	public int[] getpts() {
		return pts;
	}
	
	public int[] getts() {
		return ts;
	}
	
	public Vertex[] getPoints() {
		Vertex[] p2 = new Vertex[points.length];
		
		for (int i = 0; i < p2.length; i++) {
			p2[i] = new Vertex(points[i].x, points[i].y, points[i].z);
		}
		
		return p2;
	}
	
	public Face[] getTiles() {
		Face[] t2 = new Face[tiles.length];
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				t2[i * 64 + j] = new Face(new Vertex(points[i * 65 + j].x, points[i * 65 + j].y, points[i * 65 + j].z),
						new Vertex(points[i * 65 + j + 1].x, points[i * 65 + j + 1].y, points[i * 65 + j + 1].z),
						new Vertex(points[(i + 1) * 65 + j].x, points[(i + 1) * 65 + j].y, points[(i + 1) * 65 + j].z),
						new Vertex(points[(i + 1) * 65 + j + 1].x, points[(i + 1) * 65 + j + 1].y, points[(i + 1) * 65 + j + 1].z),
						ts[i * 64 + j]);
			}
		}
		
		return t2;
	}
	
	public Face[] getTiles(Vertex[] points) {
		Face[] t2 = new Face[tiles.length];
		
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 64; j++) {
				t2[i * 64 + j] = new Face(points[i * 65 + j], points[i * 65 + j + 1], points[(i + 1) * 65 + j], points[(i + 1) * 65 + j + 1], ts[i * 64 + j]);
			}
		}
		
		return t2;
	}
	
	public void setTilesFile(int x, int y) {
		String tilesfile = String.format(TILES_FILE, x, y);
		
		bImgTiles = null;
		try {
			bImgTiles = ImageIO.read(new File(tilesfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPointsFile(int x, int y) {
		String pointsfile = String.format(POINTS_FILE, x, y);;
		
		bImgPoints = null;
		try {
			bImgPoints = ImageIO.read(new File(pointsfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPoints() {
		for(int i = 0; i < SECTOR_HEIGHT + 1; i++) {
			for(int j = 0; j < SECTOR_WIDTH + 1; j++) {
				points[i * 65 + j] = new Vertex((j - (SECTOR_WIDTH / 2)) * 10, (pts[i * (SECTOR_HEIGHT + 1) + j] & 0xff) - 128, (SECTOR_HEIGHT / 2 - i) * 10);
			}
		}
	}
	
	public void setTiles() {
		for(int i = 0; i < SECTOR_HEIGHT; i++) {
			for(int j = 0; j < SECTOR_WIDTH; j++) {
				tiles[i * 64 + j] = new Face(points[i * 65 + j], points[i * 65 + j + 1], points[(i + 1) * 65 + j], points[(i + 1) * 65 + j + 1], ts[i * 64 + j]);
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
}
