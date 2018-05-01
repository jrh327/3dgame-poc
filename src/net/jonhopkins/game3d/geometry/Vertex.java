package net.jonhopkins.game3d.geometry;

public class Vertex {
	public double x;
	public double y;
	public double z;
	
	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Convert degrees to radians.
	 * 
	 * @param theta the angle, in degrees, to convert
	 * @return the angle in radians
	 */
	public static double degreesToRadians(double theta) {
		return Math.PI * theta / 180.0;
	}
	
	/**
	 * Rotate the given points relative to the x-axis.
	 * 
	 * @param points the points to rotate
	 * @param theta the angle by which to rotate, in degrees
	 */
	public static void rotateX(Vertex[] points, double theta) {
		theta = degreesToRadians(theta);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		for (Vertex point : points) {
			double y = point.y;
			double z = point.z;
			double tempY = y * cosTheta - z * sinTheta;
			double tempZ = y * sinTheta + z * cosTheta;
			
			point.y = tempY;
			point.z = tempZ;
		}
	}
	
	/**
	 * Rotate the point relative to the x-axis.
	 * 
	 * @param theta the angle by which to rotate, in degrees
	 */
	public void rotateX(double theta) {
		theta = degreesToRadians(theta);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		double tempY = y * cosTheta - z * sinTheta;
		double tempZ = y * sinTheta + z * cosTheta;
		
		y = tempY;
		z = tempZ;
	}
	
	/**
	 * Rotate the given points relative to the y-axis.
	 * 
	 * @param points the points to rotate
	 * @param theta the angle by which to rotate, in degrees
	 */
	public static void rotateY(Vertex[] points, double theta) {
		theta = degreesToRadians(theta);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		for (Vertex point : points) {
			double z = point.z;
			double x = point.x;
			double tempZ = z * cosTheta - x * sinTheta;
			double tempX = z * sinTheta + x * cosTheta;
			
			point.x = tempX;
			point.z = tempZ;
		}
	}
	
	/**
	 * Rotate the point relative to the y-axis.
	 * 
	 * @param theta the angle by which to rotate, in degrees
	 */
	public void rotateY(double theta) {
		theta = degreesToRadians(theta);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		double tempZ = z * cosTheta - x * sinTheta;
		double tempX = z * sinTheta + x * cosTheta;
		
		x = tempX;
		z = tempZ;
	}
	
	/**
	 * Rotate the given points relative to the z-axis.
	 * 
	 * @param points the points to rotate
	 * @param theta the angle by which to rotate, in degrees
	 */
	public static void rotateZ(Vertex[] points, double theta) {
		theta = degreesToRadians(theta);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		for (Vertex point : points) {
			double x = point.x;
			double y = point.y;
			double tempX = x * cosTheta - y * sinTheta;
			double tempY = x * sinTheta + y * cosTheta;
			
			point.x = tempX;
			point.y = tempY;
		}
	}
	
	/**
	 * Rotate the point relative to the z-axis.
	 * 
	 * @param theta the angle by which to rotate, in degrees
	 */
	public void rotateZ(double theta) {
		theta = degreesToRadians(theta);
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		
		double tempX = x * cosTheta - y * sinTheta;
		double tempY = x * sinTheta + y * cosTheta;
		
		x = tempX;
		y = tempY;
	}
}
