package net.jonhopkins.game3d.geometry;

public class Vector {
	public final double x;
	public final double y;
	public final double z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vertex point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}
	
	public static Vector cross(Vector v1, Vector v2) {
		double x = v1.y * v2.z - v1.z * v2.y;
		double y = v1.z * v2.x - v1.x * v2.z;
		double z = v1.x * v2.y - v1.y * v2.x;
		return new Vector(x, y, z);
	}
	
	public static double dot(Vector v1, Vector v2) {
		return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
	}
}
