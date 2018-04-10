package net.jonhopkins.game3d;

public class DrawingPreparation {
	public static void backFaceCulling(MapTile[] tiles) {
		for (int i = 0; i < tiles.length - 1; i++) {
			//TODO
		}
	}
	
	public static void Quicksort(MapTile[] list, int min, int max) {
		MapTile med_value;
		int hi;
		int lo;
		int i;
		
		//If min >= max, the list contains 0 or 1 items so it is sorted.
		if (min >= max) return;
		
		//Pick the dividing value.
		i = (int)((max - min + 1) * Math.random() + min);
		med_value = list[i];
		
		//Swap it to the front.
		list[i] = list[min];
		
		lo = min;
		hi = max;
		while (true) {
			//Look down from hi for a value < med_value.
			while (list[hi].avgZ() <= med_value.avgZ()) {
				hi = hi - 1;
				if (hi <= lo) break;
			}
			if (hi <= lo) {
				list[lo] = med_value;
				break;
			}
			
			//Swap the lo and hi values.
			list[lo] = list[hi];
			
			//Look up from lo for a value >= med_value.
			lo = lo + 1;
			while (list[lo].avgZ() > med_value.avgZ()) {
				lo = lo + 1;
				if (lo >= hi) break;
			}
			if (lo >= hi) {
				lo = hi;
				list[hi] = med_value;
				break;
			}
			
			//Swap the lo and hi values.
			list[hi] = list[lo];
		}
		
		//Sort the two sublists.
		Quicksort (list, min, lo - 1);
		Quicksort (list, lo + 1, max);
	}
	
	public static void rotatePointsX(Point3D[] points, double rotatex) {
		for (int i = 0; i < points.length; i++) {
			points[i].rotateX(rotatex);
		}
	}
	public static void rotatePointsY(Point3D[] points, double rotatey) {
		for (int i = 0; i < points.length; i++) {
			points[i].rotateY(rotatey);
		}
	}
	public static void rotatePointsZ(Point3D[] points, double rotatez) {
		for (int i = 0; i < points.length; i++) {
			points[i].rotateZ(rotatez);
		}
	}
	public static void translatePointsX(Point3D[] points, double offsetx) {
		for (int i = 0; i < points.length; i++) {
			points[i].x += offsetx;
		}
	}
	public static void translatePointsY(Point3D[] points, double offsetx) {
		for (int i = 0; i < points.length; i++) {
			points[i].x += offsetx;
		}
	}
	public static void translatePointsZ(Point3D[] points, double offsetx) {
		for (int i = 0; i < points.length; i++) {
			points[i].x += offsetx;
		}
	}
	public static void translatePointsWithRespectToCamera(Point3D[] points, Point3D camera) {
		for (int i = 0; i < points.length; i++) {
			points[i].x -= camera.x;
			points[i].y -= camera.y;
			points[i].z -= camera.z;
		}
	}
}