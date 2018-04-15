package net.jonhopkins.game3d;

import java.util.Arrays;
import java.util.Collections;

public class DrawingPreparation {
	public static void backFaceCulling(MapTile[] tiles) {
		for (int i = 0; i < tiles.length - 1; i++) {
			//TODO
		}
	}
	
	public static void Quicksort(MapTile[] list, int min, int max) {
		Collections.sort(Arrays.asList(list));
	}
	
	public static void rotatePointsX(Point3D[] points, double rotateX) {
		Point3D.rotateX(points, rotateX);
	}
	
	public static void rotatePointsY(Point3D[] points, double rotateY) {
		Point3D.rotateY(points, rotateY);
	}
	
	public static void rotatePointsZ(Point3D[] points, double rotateZ) {
		Point3D.rotateZ(points, rotateZ);
	}
	
	public static void translatePointsX(Point3D[] points, double offsetX) {
		for (Point3D point : points) {
			point.x += offsetX;
		}
	}
	
	public static void translatePointsY(Point3D[] points, double offsetY) {
		for (Point3D point : points) {
			point.y += offsetY;
		}
	}
	
	public static void translatePointsZ(Point3D[] points, double offsetZ) {
		for (Point3D point : points) {
			point.z += offsetZ;
		}
	}
	
	public static void translatePointsWithRespectToCamera(Point3D[] points, Point3D camera) {
		double cameraX = camera.x;
		double cameraY = camera.y;
		double cameraZ = camera.z;
		
		for (Point3D point : points) {
			point.x -= cameraX;
			point.y -= cameraY;
			point.z -= cameraZ;
		}
	}
}
