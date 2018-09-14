package net.jonhopkins.game3d.object;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.DirectionalLight;
import net.jonhopkins.game3d.model.Model;
import net.jonhopkins.game3d.model.ModelFactory;
import net.jonhopkins.game3d.script.SunUpdateScript;

public class Sun extends Prefab {
	private final double maxLightLevel = 0.875;
	private final Vertex center = new Vertex(0, -320, 0);
	
	public Sun() {
		super();
		
		Model model = ModelFactory.getModel("sun.obj");
		model.setPosition(new Vertex(center));
		DirectionalLight light = new DirectionalLight(
				new Vertex(center), new Vertex(), Color.white, maxLightLevel);
		
		registerChild("sun_model", model);
		registerChild("sun_light", light);
		registerScript("rotation_script", new SunUpdateScript(this));
	}
}
