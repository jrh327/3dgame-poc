package net.jonhopkins.game3d.light;

import java.awt.Color;

public class Light {
	protected Color color;
	protected double intensity;
	
	public Light(Color color, double intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}
	
	public double getIntensity() {
		return intensity;
	}
}
