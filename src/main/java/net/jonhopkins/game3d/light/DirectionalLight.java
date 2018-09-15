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
		Vertex position = getAbsolutePosition();
		Vector rotation = getAbsoluteRotation();
		position.rotateX(rotation.x);
		position.rotateY(rotation.y);
		position.rotateZ(rotation.z);
		Vector direction = new Vector(position.x - target.x,
				position.y - target.y, position.z - target.z);
		double factor = Vector.dotNormalized(face.getNormal(), direction) * intensity;
		if (factor < 0.0) {
			factor = 0.0;
		}
		return factor;
	}
}
