package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;

public class DrawScene {
	public static int drawScene(Point3D[] points, MapTile[] tiles, Point3D camera, double cameraHeight, double viewingDistance, int halfScreenX, int halfScreenY, Graphics bufferGraphics, int tod, double rotatex, double rotatey) {
		bufferGraphics.clearRect(0, 0, 600, 400);
		
		MapTile sun = new MapTile(new Point3D(-1, -32, 1), new Point3D(1, -32, 1), new Point3D(-1, -32, -1), new Point3D(1, -32, -1), 0xffff99);
		
		sun.rotateZ((double)(-(tod - 60) * 360.0 / 1440.0));
		sun.rotateY(rotatey);
		sun.rotateX(rotatex);
		
		int xs[] = new int[4];
		int ys[] = new int[4];
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
				bufferGraphics.setColor(new Color((int)(lightlevel * 153 / 8),
						(int)(lightlevel * 153 / 8),
						50 + (int)(lightlevel * 205 / 8)));
			} else if (tod >= 900) {
				lightlevel = (double)(tod / 60 - (9 + 2 * (tod / 60 - 16)) - (1 - (tod % 60) / 60.0));
				bufferGraphics.setColor(new Color((int)((1 - lightlevel) * 153 / 8),
						(int)((1 - lightlevel) * 153 / 8),
						50 + (int)((1 - lightlevel) * 205 / 8)));
			}
			
			bufferGraphics.setColor(new Color((int)(lightlevel * 153 / 8),
					(int)(lightlevel * 153 / 8),
					50 + (int)(lightlevel * 205 / 8)));
		}
		
		double timeofdayscalar = (8.0 - Math.abs(lightlevel - 8.0)) / 8.0;
		
		bufferGraphics.fillRect(0, 0, 2 * halfScreenX, 2 * halfScreenY);
		
		if (sun.avgZ() >= 0.0) {
			sun.to2DCoords(halfScreenX, halfScreenY, xs, ys);
			bufferGraphics.setColor(new Color(sun.getRGB()));
			bufferGraphics.fillPolygon(xs, ys, 4);
		}
		
		for(int i = 0; i < 64; i++) {
			for(int j = 0; j < 64; j++) {
				double dist = Math.pow(Math.pow(tiles[i * 64 + j].avgX(), 2) + Math.pow(tiles[i* 64 + j].avgHeight() + cameraHeight, 2) + Math.pow(tiles[i * 64 + j].avgZ(), 2), 0.5); 
				
				if((tiles[i * 64 + j].avgZ() >= 0.0) && dist < viewingDistance) {
					tiles[i * 64 + j].to2DCoords(halfScreenX, halfScreenY, xs, ys);
					
					double colorScaler = (1.0 - (dist / viewingDistance)) * timeofdayscalar;
					
					int color = tiles[i * 64 + j].getRGB();
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
					bufferGraphics.fillPolygon(xs, ys, 4);
					
					if (inpoly(xs, ys, 4, 300, 200)) {
						closestToMouse = i * 64 + j;
					}
				}
			}
		}
		
		return closestToMouse;
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
}