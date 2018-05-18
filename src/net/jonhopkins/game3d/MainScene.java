package net.jonhopkins.game3d;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.AmbientLight;
import net.jonhopkins.game3d.model.MapSector;
import net.jonhopkins.game3d.model.Sun;

public class MainScene extends Scene {
	private double cameraHeight = 10.0;
	
	public MainScene() {
		super();
		
		camera = new Camera(new Vertex(0.0, cameraHeight, 0.0), new Vector(0.0, 0.0, 0.0));
		
		Sun sun = new Sun();
		registerModel(sun);
		registerModel(MapSector.getMapSector(0, 0));
		registerLight(new AmbientLight(Color.white, 0.125));
		registerLight(sun.getLight());
	}
}
