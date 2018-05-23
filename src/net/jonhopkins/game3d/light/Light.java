package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;

public abstract class Light {
	protected Color color;
	protected double intensity;
	protected Vertex originalPosition;
	protected Vertex position;
	
	public Light(Color color, double intensity) {
		this(new Vertex(), color, intensity);
	}
	
	public Light(Vertex position, Color color, double intensity) {
		setPosition(position);
		this.color = color;
		this.intensity = intensity;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setPosition(Vertex position) {
		this.originalPosition = new Vertex(position);
		this.position = position;
	}
	
	public Vertex getPosition() {
		return position;
	}
	
	public void update(double timestep) {
		resetPosition();
	}
	
	private void resetPosition() {
		position.x = originalPosition.x;
		position.y = originalPosition.y;
		position.z = originalPosition.z;
	}
	
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public double getIntensity() {
		return intensity;
	}
	
	public abstract double getLightFactor(Face face);
}
