package net.jonhopkins.game3d;

public class Point3D {
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void rotateX(double theta) {
		double tempZ;
		double tempY;
		
		theta = Math.PI * theta / 180;
		
		tempY = y * Math.cos(theta) - z * Math.sin(theta);
		tempZ = y * Math.sin(theta) + z * Math.cos(theta);
		
		y = tempY;
		z = tempZ;
	}
	public void rotateY(double theta) {
		double tempX;
		double tempZ;
		
		theta = Math.PI * theta / 180;
		
		tempZ = z * Math.cos(theta) - x * Math.sin(theta);
		tempX = z * Math.sin(theta) + x * Math.cos(theta);
		
		x = tempX;
		z = tempZ;
	}
	public void rotateZ(double theta) {
		double tempX;
		double tempY;
		
		theta = Math.PI * theta / 180;
		
		tempX = x * Math.cos(theta) - y * Math.sin(theta);
		tempY = x * Math.sin(theta) + y * Math.cos(theta);
		
		x = tempX;
		y = tempY;
	}
	
	public double x;
	public double y;
	public double z;
}