package net.jonhopkins.game3d;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.model.Model;

public class Renderer {
	private Graphics bufferGraphics;
	private Camera camera;
	private int halfScreenX;
	private int halfScreenY;
	private double viewingDistance;
	private int frameCount = 0;
	private double elapsedTime = 1;
	private double tod = 720;
	
	private final Color DEBUG_TEXT_COLOR = Color.white;
	private final Color DEBUG_TILE_OUTLINE_COLOR = Color.black;
	private final Color DEBUG_POINT_OUTLINE_COLOR = Color.red;
	
	public Renderer(Graphics buffer) {
		this.bufferGraphics = buffer;
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		tod++;
		//tod = ((60 * hour + min) % 24) * 60 + sec;
		if (tod >= 1440.0) {
			tod = 0.0;
		}
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setDimensions(int width, int height) {
		this.halfScreenX = width / 2;
		this.halfScreenY = height / 2;
	}
	
	public void setRenderDistance(double renderDistance) {
		this.viewingDistance = renderDistance;
	}
	
	public void renderScene(Scene scene, double timestep) {
		elapsedTime += timestep;
		frameCount++;
		
		List<Face> tempTiles = prepareScene(scene, camera);
		int closestToMouse = drawScene(tempTiles, camera, bufferGraphics);
		
		Vertex mousePosition = new Vertex(0.0, 0.0, 0.0);
		Vertex cameraPosition = camera.position;
		Vector cameraRotation = camera.rotation;
		if (closestToMouse >= 0) {
			Face closest = tempTiles.get(closestToMouse);
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
			
			Vertex vertex = tempTiles.get(closestToMouse).vertices[index];
			mousePosition.x = vertex.x / 10.0;
			mousePosition.y = vertex.y / 10.0;
			mousePosition.z = vertex.z / 10.0;
			mousePosition.rotateY(-cameraRotation.y);
			mousePosition.x += cameraPosition.x / 10.0;
			mousePosition.y += cameraPosition.y / 10.0;
			mousePosition.z += cameraPosition.z / 10.0;
			mousePosition.x = Math.round(mousePosition.x);
			mousePosition.y = Math.round(mousePosition.y);
			mousePosition.z = Math.round(mousePosition.z);
		}
		
		bufferGraphics.setColor(DEBUG_TEXT_COLOR);
		
		bufferGraphics.drawLine(halfScreenX, halfScreenY - 10, halfScreenX, halfScreenY + 10);
		bufferGraphics.drawLine(halfScreenX - 10, halfScreenY, halfScreenX + 10, halfScreenY);
		
		bufferGraphics.drawString((new StringBuilder(String.valueOf(cameraPosition.x))).append(", ").append(cameraPosition.y).append(", ").append(cameraPosition.z).toString(), 10, 10);
		bufferGraphics.drawString((new StringBuilder("rotatex: ")).append(cameraRotation.x).toString(), 10, 20);
		bufferGraphics.drawString((new StringBuilder("rotatey: ")).append(cameraRotation.y).toString(), 10, 30);
		bufferGraphics.drawString(new StringBuilder("pointing at: ").append(mousePosition.x).append(", ").append(mousePosition.y).append(", ").append(mousePosition.z).toString(), 10, 40);
		
		int framesPerMs = (int)(elapsedTime / frameCount);
		if (framesPerMs == 0) {
			framesPerMs = 1;
		}
		bufferGraphics.drawString((new StringBuilder("")).append(1000 / framesPerMs).append(" fps").toString(), 10, 50);
		
		tod += timestep;
		if (tod >= 1440.0) {
			tod = 0.0;
		}
		bufferGraphics.drawString((new StringBuilder("Time of Day: ")).append((int)(tod / 60)).append(":").append(new String("00").substring(new Integer((int)(tod % 60)).toString().length())).append((int)tod % 60).toString(), 10, 60);
	}
	
	private List<Face> prepareScene(Scene scene, Camera camera) {
		int numVertices = 0;
		int numFaces = 0;
		List<Model> models = scene.getModels();
		for (Model model : models) {
			numVertices += model.getVertices().length;
			numFaces += model.getFaces().length;
		}
		
		List<Vertex> vertices = new ArrayList<>(numVertices);
		List<Face> faces = new ArrayList<>(numFaces);
		
		for (Model model : models) {
			vertices.addAll(Arrays.asList(model.getVertices()));
			faces.addAll(Arrays.asList(model.getFaces()));
		}
		
		Vertex.translate(vertices, new Vector(-camera.position.x, -camera.position.y, -camera.position.z));
		if (camera.rotation.y != 0) {
			Vertex.rotateY(vertices, camera.rotation.y);
		}
		if (camera.rotation.x != 0) {
			Vertex.rotateX(vertices, camera.rotation.x);
		}
		backFaceCulling(faces);
		sort(faces, 0, faces.size() - 1);
		
		return faces;
	}
	
	private int drawScene(List<Face> tiles, Camera camera, Graphics bufferGraphics) {
		bufferGraphics.clearRect(0, 0, 600, 400);
		
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
		
		int counter = 0;
		for (Face tile : tiles) {
			if (!tile.isVisible()) {
				continue;
			}
			
			double dist = Math.pow(Math.pow(tile.avgX(), 2) + Math.pow(tile.avgY() + camera.position.y, 2) + Math.pow(tile.avgZ(), 2), 0.5); 
			
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
	
	private int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		}
		return value;
	}
	
	private boolean inpoly(int xs[], int ys[], int npoints, int xt, int yt) {
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
	
	private void backFaceCulling(List<Face> tiles) {
		for (Face tile : tiles) {
			Vector normal = tile.getNormal();
			
			// camera is the "origin" here, so the vector between the camera
			// and a point on the tile is just the negative of that point
			Vertex center = tile.getCenter();
			Vector camToTile = new Vector(-center.x, -center.y, -center.z);
			double dot = Vector.dot(normal, camToTile);
			tile.setVisible(dot > 0.0);
		}
	}
	
	private void sort(List<Face> list, int min, int max) {
		Collections.sort(list);
	}
}
