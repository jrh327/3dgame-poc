package net.jonhopkins.game3d.light;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Face;

public class AmbientLight extends Light {
	public AmbientLight(Color color, double intensity) {
		super(color, intensity);
	}
	
	@Override
	public double getLightFactor(Face face) {
		return intensity;
	}
}
