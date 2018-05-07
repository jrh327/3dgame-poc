package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vertex;

public class PointLight extends Light {
	private Vertex position;
	
	public PointLight(Vertex position, Color color, double intensity) {
		super(color, intensity);
		this.position = position;
	}
	
	public void setPosition(Vertex position) {
		this.position = position;
	}
	
	public Vertex getPosition() {
		return position;
	}
}
