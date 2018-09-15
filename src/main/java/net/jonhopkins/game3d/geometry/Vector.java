package net.jonhopkins.game3d.geometry;

public class Vector {
	public double x;
	public double y;
	public double z;
	
	public Vector() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
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
	
	public Vector(Vector vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}
	
	/**
	 * Set the components of this vector to those given.
	 * 
	 * @param x the new x component
	 * @param y the new y component
	 * @param z the new z component
	 */
	public void setTo(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Set the components of this vector to those of the other vector.
	 * 
	 * @param vector the other vector
	 */
	public void setTo(Vector vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
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
	
	public static double dotNormalized(Vector v1, Vector v2) {
		v1 = normalize(v1);
		v2 = normalize(v2);
		return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
	}
	
	public static Vector normalize(Vector v) {
		double magnitude = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
		return new Vector(v.x / magnitude, v.y / magnitude, v.z / magnitude);
	}
}
