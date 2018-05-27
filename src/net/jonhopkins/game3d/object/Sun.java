package net.jonhopkins.game3d.object;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.DirectionalLight;
import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.model.Model;
import net.jonhopkins.game3d.model.ModelFactory;
import net.jonhopkins.game3d.script.SunUpdateScript;

public class Sun extends Prefab {
	private Light light;
	private final double maxLightLevel = 0.875;
	
	public Sun() {
		Vertex center = new Vertex(0, -320, 0);
		Vertex pivot = new Vertex();
		
		Model model = ModelFactory.getModel("sun.obj");
		model.setPosition(center);
		light = new DirectionalLight(center, pivot, Color.white, maxLightLevel);
		position = new Vertex(pivot);
		
		this.registerChild("sun_model", model);
		this.registerChild("sun_light", light);
		
		registerScript("rotation_script", new SunUpdateScript(this));
	}
	
	public Light getLight() {
		return light;
	}
}
