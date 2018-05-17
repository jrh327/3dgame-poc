package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.model.MapSector;
import net.jonhopkins.game3d.model.Sun;

public class Renderer {
	private static Sun sun = new Sun();
	
	public static Face[] prepareScene(MapSector s1, Vertex camera, double rotatex, double rotatey) {
		Vertex[] tempPoints = s1.getVertices();
		Face[] tempTiles = s1.getFaces();
		Renderer.translatePointsWithRespectToCamera(tempPoints, camera);
		if (rotatey != 0) {
			Renderer.rotatePointsY(tempPoints, rotatey);
		}
		if (rotatex != 0) {
			Renderer.rotatePointsX(tempPoints, rotatex);
		}
		tempTiles = Renderer.backFaceCulling(tempTiles);
		Renderer.Quicksort(tempTiles, 0, tempTiles.length - 1);
		
		return tempTiles;
	}
	
	public static int drawScene(Face[] tiles, Vertex camera, double cameraHeight, double viewingDistance, int halfScreenX, int halfScreenY, Graphics bufferGraphics, int tod, double rotatex, double rotatey) {
		bufferGraphics.clearRect(0, 0, 600, 400);
		
		Vertex sunUL = new Vertex(-1, -32, 1);
		Vertex sunUR = new Vertex(1, -32, 1);
		Vertex sunLR = new Vertex(1, -32, -1);
		Vertex sunLL = new Vertex(-1, -32, -1);
		
		Face sun1 = new Face(new Vertex[] { sunUL, sunUR, sunLR }, 0xffff99);
		Face sun2 = new Face(new Vertex[] { sunUL, sunLR, sunLL }, 0xffff99);
		
		sunUL.rotateZ((double)(-(tod - 60) * 360.0 / 1440.0));
		sunUL.rotateY(rotatey);
		sunUL.rotateX(rotatex);
		sunUR.rotateZ((double)(-(tod - 60) * 360.0 / 1440.0));
		sunUR.rotateY(rotatey);
		sunUR.rotateX(rotatex);
		sunLR.rotateZ((double)(-(tod - 60) * 360.0 / 1440.0));
		sunLR.rotateY(rotatey);
		sunLR.rotateX(rotatex);
		sunLL.rotateZ((double)(-(tod - 60) * 360.0 / 1440.0));
		sunLL.rotateY(rotatey);
		sunLL.rotateX(rotatex);
		
		int xs[] = new int[3];
		int ys[] = new int[3];
		int closestToMouse = -1;
		
		double lightlevel = 0;
		
		if (tod < 240 || tod > 1320) {
			lightlevel = 1;
			bufferGraphics.setColor(new Color(0, 0, 50));
		} else if (tod > 660 && tod < 900) {
			lightlevel = 8;
			bufferGraphics.setColor(new Color(153, 153, 255));
		} else {
			if (tod <= 660) {
				lightlevel = (double)(tod / 60 - 3 + (tod % 60) / 60.0);
				int r = clamp((int)(lightlevel * 153 / 8), 0, 255);
				int g = clamp((int)(lightlevel * 153 / 8), 0, 255);
				int b = clamp(50 + (int)(lightlevel * 205 / 8), 0, 255);
				
				bufferGraphics.setColor(new Color(r, g, b));
			} else if (tod >= 900) {
				lightlevel = (double)(tod / 60 - (9 + 2 * (tod / 60 - 16)) - (1 - (tod % 60) / 60.0));
				int r = clamp((int)((1 - lightlevel) * 153 / 8), 0, 255);
				int g = clamp((int)((1 - lightlevel) * 153 / 8), 0, 255);
				int b = clamp(50 + (int)((1 - lightlevel) * 205 / 8), 0, 255);
				
				bufferGraphics.setColor(new Color(r, g, b));
			} else {
				int r = clamp((int)(lightlevel * 153 / 8), 0, 255);
				int g = clamp((int)(lightlevel * 153 / 8), 0, 255);
				int b = clamp(50 + (int)(lightlevel * 205 / 8), 0, 255);
				
				bufferGraphics.setColor(new Color(r, g, b));
			}
		}
		
		double timeofdayscalar = (8.0 - Math.abs(lightlevel - 8.0)) / 8.0;
		
		bufferGraphics.fillRect(0, 0, 2 * halfScreenX, 2 * halfScreenY);
		
		if (sun1.avgZ() >= 0.0) {
			sun1.to2DCoords(halfScreenX, halfScreenY, xs, ys);
			bufferGraphics.setColor(new Color(sun1.getRGB()));
			bufferGraphics.fillPolygon(xs, ys, xs.length);
			sun2.to2DCoords(halfScreenX, halfScreenY, xs, ys);
			bufferGraphics.setColor(new Color(sun2.getRGB()));
			bufferGraphics.fillPolygon(xs, ys, xs.length);
		}
		
		int counter = 0;
		for (Face tile : tiles) {
			double dist = Math.pow(Math.pow(tile.avgX(), 2) + Math.pow(tile.avgY() + cameraHeight, 2) + Math.pow(tile.avgZ(), 2), 0.5); 
			
			if (tile.avgZ() >= 0.0 && dist < viewingDistance) {
				tile.to2DCoords(halfScreenX, halfScreenY, xs, ys);
				
				double colorScaler = (1.0 - (dist / viewingDistance)) * timeofdayscalar;
				
				int color = tile.getRGB();
				int newR = (int)(((color & 0xff0000) >> 16) * colorScaler);
				int newG = (int)(((color & 0xff00) >> 8) * colorScaler);
				int newB = (int)(((color & 0xff)) * colorScaler);
				
				if (newR < 0) newR = 0;
				if (newR > 255) newR = 255;
				if (newG < 0) newG = 0;
				if (newG > 255) newG = 255;
				if (newB < 0) newB = 0;
				if (newB > 255) newB = 255;
				
				bufferGraphics.setColor(new Color(newR, newG, newB));
				bufferGraphics.fillPolygon(xs, ys, xs.length);
				
				if (inpoly(xs, ys, xs.length, 300, 200)) {
					closestToMouse = counter;
				}
				counter++;
			}
		}
		
		return closestToMouse;
	}
	
	private static int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		}
		return value;
	}
	
	public static boolean inpoly(int xs[], int ys[], int npoints, int xt, int yt) {
		int xnew, ynew;
		int xold, yold;
		int x1, y1;
		int x2, y2;
		int i;
		boolean inside = false;
		
		if (npoints < 3) {
			return false;
		}
		xold = xs[npoints - 1];
		yold = ys[npoints - 1];
		for (i = 0; i < npoints; i++) {
			xnew = xs[i];
			ynew = ys[i];
			if (xnew > xold) {
				x1 = xold;
				x2 = xnew;
				y1 = yold;
				y2 = ynew;
			}
			else {
				x1 = xnew;
				x2 = xold;
				y1 = ynew;
				y2 = yold;
			}
			if ((xnew < xt) == (xt <= xold)          /* edge "open" at one end */
					&& ((long)yt - (long)y1) * (long)(x2 - x1)
					< ((long)y2 - (long)y1) * (long)(xt - x1)) {
				inside= !inside;
			}
			xold = xnew;
			yold = ynew;
		}
		return inside;
	}
	
	
	
	private static Face[] backFaceCulling(Face[] tiles) {
		List<Face> visible = new ArrayList<>(tiles.length);
		
		for (Face tile : tiles) {
			Vector normal = tile.getNormal();
			
			// camera is the "origin" here, so the vector between the camera
			// and a point on the tile is just the negative of that point
			Vertex center = tile.getCenter();
			Vector camToTile = new Vector(-center.x, -center.y, -center.z);
			double dot = Vector.dot(normal, camToTile);
			if (dot > 0.0) {
				visible.add(tile);
			}
		}
		
		return visible.toArray(new Face[visible.size()]);
	}
	
	private static void Quicksort(Face[] list, int min, int max) {
		Collections.sort(Arrays.asList(list));
	}
	
	private static void rotatePointsX(Vertex[] points, double rotateX) {
		Vertex.rotateX(points, rotateX);
	}
	
	private static void rotatePointsY(Vertex[] points, double rotateY) {
		Vertex.rotateY(points, rotateY);
	}
	
	private static void rotatePointsZ(Vertex[] points, double rotateZ) {
		Vertex.rotateZ(points, rotateZ);
	}
	
	private static void translatePointsX(Vertex[] points, double offsetX) {
		for (Vertex point : points) {
			point.x += offsetX;
		}
	}
	
	private static void translatePointsY(Vertex[] points, double offsetY) {
		for (Vertex point : points) {
			point.y += offsetY;
		}
	}
	
	private static void translatePointsZ(Vertex[] points, double offsetZ) {
		for (Vertex point : points) {
			point.z += offsetZ;
		}
	}
	
	private static void translatePointsWithRespectToCamera(Vertex[] points, Vertex camera) {
		double cameraX = camera.x;
		double cameraY = camera.y;
		double cameraZ = camera.z;
		
		for (Vertex point : points) {
			point.x -= cameraX;
			point.y -= cameraY;
			point.z -= cameraZ;
		}
	}
}
