package net.jonhopkins.game3d.geometry;

import java.util.List;

public class Vertex {
	public double x;
	public double y;
	public double z;
	
	public Vertex() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vertex(Vertex vertex) {
		this.x = vertex.x;
		this.y = vertex.y;
		this.z = vertex.z;
	}
	
	/**
	 * Set the coordinates of this vertex to those given.
	 * 
	 * @param x the new x position
	 * @param y the new y position
	 * @param z the new z position
	 */
	public void setTo(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Set the coordinates of this vertex to those of the other vertex.
	 * 
	 * @param vertex the other vertex
	 */
	public void setTo(Vertex vertex) {
		this.x = vertex.x;
		this.y = vertex.y;
		this.z = vertex.z;
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
	public static void rotateX(List<Vertex> points, double theta) {
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
	public static void rotateY(List<Vertex> points, double theta) {
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
	public static void rotateZ(List<Vertex> points, double theta) {
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
	
	public void translateX(double x) {
		this.x += x;
	}
	
	public static void translateX(List<Vertex> points, double offsetX) {
		for (Vertex point : points) {
			point.x += offsetX;
		}
	}
	
	public void translateY(double y) {
		this.y += y;
	}
	
	public static void translateY(List<Vertex> points, double offsetY) {
		for (Vertex point : points) {
			point.y += offsetY;
		}
	}
	
	public void translateZ(double z) {
		this.z += z;
	}
	
	public static void translateZ(List<Vertex> points, double offsetZ) {
		for (Vertex point : points) {
			point.z += offsetZ;
		}
	}
	
	/**
	 * Move the vertex coordinates by the given distances.
	 * 
	 * @param x the distance to move in the x direction
	 * @param y the distance to move in the y direction
	 * @param z the distance to move in the z direction
	 */
	public void translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	/**
	 * Move the vertex in the given direction.
	 * 
	 * @param translate vector representing the direction and distance to move
	 */
	public void translate(Vector translate) {
		this.x += translate.x;
		this.y += translate.y;
		this.z += translate.z;
	}
	
	public static void translate(List<Vertex> points, Vector translate) {
		double translateX = translate.x;
		double translateY = translate.y;
		double translateZ = translate.z;
		
		for (Vertex point : points) {
			point.x += translateX;
			point.y += translateY;
			point.z += translateZ;
		}
	}
}
