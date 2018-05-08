package net.jonhopkins.game3d.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;

public class MapSector extends Model {
	private BufferedImage bImgPoints;
	private BufferedImage bImgTiles;
	private static final int SECTOR_WIDTH = 64;
	private static final int SECTOR_HEIGHT = 64;
	
	private static final String SECTORS_DIR = "sectors/";
	private static final String TILES_DIR = SECTORS_DIR + "tiles/";
	private static final String TILES_FILE = TILES_DIR + "Sector_%d_%d_tiles.png";
	private static final String POINTS_DIR = SECTORS_DIR + "points/";
	private static final String POINTS_FILE = POINTS_DIR + "Sector_%d_%d_points.png";
	
	public MapSector(Vertex[] vertices, int[][] faceVertices, int[] faceColors) {
		super(vertices, faceVertices, faceColors);
	}
	
	public static MapSector getMapSector(int x, int y) {
		BufferedImage imgPoints = getPointsFile(x, y);
		BufferedImage imgTiles = getTilesFile(x, y);
		
		int[] coords = new int[(SECTOR_HEIGHT + 1) * (SECTOR_WIDTH + 1)];
		imgPoints.getRGB(0, 0, SECTOR_WIDTH + 1, SECTOR_HEIGHT + 1, coords, 0, SECTOR_WIDTH + 1);
		
		int[] tempColors = new int[SECTOR_HEIGHT * SECTOR_WIDTH];
		imgTiles.getRGB(0, 0, SECTOR_WIDTH, SECTOR_HEIGHT, tempColors, 0, SECTOR_WIDTH);
		int[] faceColors = new int[tempColors.length * 2];
		for (int i = 0; i < tempColors.length; i++) {
			faceColors[i * 2] = tempColors[i];
			faceColors[i * 2 + 1] = tempColors[i];
		}
		
		Vertex[] vertices = new Vertex[(SECTOR_HEIGHT + 1) * (SECTOR_WIDTH + 1)];
		Face[] tiles = new Face[SECTOR_HEIGHT * SECTOR_WIDTH * 2];
		
		int count = 0;
		for (int vy = 0; vy < SECTOR_HEIGHT + 1; vy++) {
			for (int vx = 0; vx < SECTOR_WIDTH + 1; vx++) {
				vertices[count] = new Vertex(
						(vx - (SECTOR_WIDTH / 2)) * 10.0,
						(coords[vy * (SECTOR_HEIGHT + 1) + vx] & 0x00ff) - 128.0,
						(SECTOR_HEIGHT / 2 - vy) * 10.0);
				count++;
			}
		}
		
		int[][] tileVertices = new int[tiles.length][];
		count = 0;
		for (int vy = 0; vy < SECTOR_HEIGHT; vy++) {
			for (int vx = 0; vx < SECTOR_WIDTH; vx++) {
				tileVertices[count] = new int[] {
						vy * (SECTOR_HEIGHT + 1) + vx,
						vy * (SECTOR_HEIGHT + 1) + vx + 1,
						(vy + 1) * (SECTOR_HEIGHT + 1) + vx + 1
				};
				count++;
				tileVertices[count] = new int[] {
						vy * (SECTOR_HEIGHT + 1) + vx,
						(vy + 1) * (SECTOR_HEIGHT + 1) + vx + 1,
						(vy + 1) * (SECTOR_HEIGHT + 1) + vx
				};
				count++;
			}
		}
		
		MapSector sector = new MapSector(vertices, tileVertices, faceColors);
		sector.setPointsFile(imgPoints);
		sector.setTilesFile(imgTiles);
		return sector;
	}
	
	public static BufferedImage getTilesFile(int x, int y) {
		String tilesfile = String.format(TILES_FILE, x, y);
		
		BufferedImage bImgTiles = null;
		try {
			bImgTiles = ImageIO.read(new File(tilesfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return bImgTiles;
	}
	
	private void setTilesFile(BufferedImage imgTiles) {
		this.bImgTiles = imgTiles;
	}
	
	private static BufferedImage getPointsFile(int x, int y) {
		String pointsfile = String.format(POINTS_FILE, x, y);;
		
		BufferedImage bImgPoints = null;
		try {
			bImgPoints = ImageIO.read(new File(pointsfile));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return bImgPoints;
	}
	
	private void setPointsFile(BufferedImage imgPoints) {
		this.bImgPoints = imgPoints;
	}
	
	public void setPoint(int x, int y, int rgb) {
		//bImgPoints.setRGB(x, y, rgb);
		//coords[y * (SECTOR_HEIGHT + 1) + x] = rgb;
		//setPoints();
	}
	
	public void setTile(int x, int y, int rgb) {
		//bImgTiles.setRGB(x, y, rgb);
		//setTiles();
	}
	
	public BufferedImage getPointFile() {
		return bImgPoints;
	}
	
	public BufferedImage getTilesFile() {
		return bImgTiles;
	}
}
