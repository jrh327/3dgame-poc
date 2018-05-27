package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.object.GameObject;

public abstract class Light extends GameObject {
	protected Color color;
	protected double intensity;
	protected Vertex originalPosition;
	
	public Light(Color color, double intensity) {
		this(new Vertex(), color, intensity);
	}
	
	public Light(Vertex position, Color color, double intensity) {
		super();
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
	
	@Override
	public void update(double timestep) {
		super.update(timestep);
	}
	
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public double getIntensity() {
		return intensity;
	}
	
	public abstract double getLightFactor(Face face);
}
