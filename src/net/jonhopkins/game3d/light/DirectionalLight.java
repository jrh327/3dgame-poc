package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class DirectionalLight extends Light {
	private Vertex target;
	
	public DirectionalLight(Vertex position, Vertex target, Color color, double intensity) {
		super(position, color, intensity);
		this.target = target;
	}
	
	public void setTarget(Vertex target) {
		this.target = target;
	}
	
	public Vertex getTarget() {
		return target;
	}
	
	@Override
	public double getLightFactor(Face face) {
		Vector direction = new Vector(absolutePosition.x - target.x,
				absolutePosition.y - target.y, absolutePosition.z - target.z);
		double factor = Vector.dotNormalized(face.getNormal(), direction) * intensity;
		if (factor < 0.0) {
			factor = 0.0;
		}
		return factor;
	}
}
