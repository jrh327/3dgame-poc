package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class PointLight extends Light {
	public PointLight(Vertex position, Color color, double intensity) {
		super(color, intensity);
		this.position = position;
	}
	
	@Override
	public double getLightFactor(Face face) {
		Vertex center = face.getCenter();
		Vector direction = new Vector(
				position.x - center.x,
				position.y - center.y,
				position.z - center.z);
		return Vector.dot(face.getNormal(), direction) * intensity;
	}
}
