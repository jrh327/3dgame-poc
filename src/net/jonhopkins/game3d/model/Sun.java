package net.jonhopkins.game3d.model;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.DirectionalLight;
import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.script.SunUpdateScript;

public class Sun extends Prefab {
	private Light light;
	private final double maxLightLevel = 0.875;
	
	public Sun() {
		model = ModelFactory.getModel("sun.obj");
		
		Vertex center = new Vertex(0, -32, 0);
		Vertex pivot = new Vertex(0.0, 0.0, 0.0);
		light = new DirectionalLight(center, pivot, Color.white, maxLightLevel);
		position = center;
		
		registerScript(new SunUpdateScript(this, light));
	}
	
	public Light getLight() {
		return light;
	}
}
