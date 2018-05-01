package net.jonhopkins.game3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class DrawingPreparation {
	public static Face[] backFaceCulling(Face[] tiles) {
		List<Face> visible = new ArrayList<>(tiles.length);
		
		for (Face tile : tiles) {
			Vector normal = tile.getNormal();
			
			// camera is the "origin" here, so the vector between the camera
			// and a point on the tile is just the negative of that point
			Vector camToTile = new Vector(-tile.UL.x, -tile.UL.y, -tile.UL.z);
			double dot = Vector.dot(normal, camToTile);
			if (dot > 0.0) {
				visible.add(tile);
			}
		}
		
		return visible.toArray(new Face[visible.size()]);
	}
	
	public static void Quicksort(Face[] list, int min, int max) {
		Collections.sort(Arrays.asList(list));
	}
	
	public static void rotatePointsX(Vertex[] points, double rotateX) {
		Vertex.rotateX(points, rotateX);
	}
	
	public static void rotatePointsY(Vertex[] points, double rotateY) {
		Vertex.rotateY(points, rotateY);
	}
	
	public static void rotatePointsZ(Vertex[] points, double rotateZ) {
		Vertex.rotateZ(points, rotateZ);
	}
	
	public static void translatePointsX(Vertex[] points, double offsetX) {
		for (Vertex point : points) {
			point.x += offsetX;
		}
	}
	
	public static void translatePointsY(Vertex[] points, double offsetY) {
		for (Vertex point : points) {
			point.y += offsetY;
		}
	}
	
	public static void translatePointsZ(Vertex[] points, double offsetZ) {
		for (Vertex point : points) {
			point.z += offsetZ;
		}
	}
	
	public static void translatePointsWithRespectToCamera(Vertex[] points, Vertex camera) {
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
