package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class DirectionalLight extends Light {
	private Vertex position;
	private Vector direction;
	
	public DirectionalLight(Vertex position, Vector direction, Color color, double intensity) {
		super(color, intensity);
		this.position = position;
		this.direction = direction;
	}
	
	public void setPosition(Vertex position) {
		this.position = position;
	}
	
	public Vertex getPosition() {
		return position;
	}
	
	public void setDirection(Vector direction) {
		this.direction = direction;
	}
	
	public Vector getDirection() {
		return direction;
	}
}
