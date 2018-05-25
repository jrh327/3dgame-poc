package net.jonhopkins.game3d;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.AmbientLight;
import net.jonhopkins.game3d.model.Cube;
import net.jonhopkins.game3d.model.MapSector;
import net.jonhopkins.game3d.model.Sun;

public class MainScene extends Scene {
	private double cameraHeight = 10.0;
	
	public MainScene() {
		super();
		
		camera = new Camera(new Vertex(0.0, cameraHeight, 0.0), new Vector());
		
		Sun sun = new Sun();
		registerPrefab("sun", sun);
		registerPrefab("sector_0_0", MapSector.getMapSector(0, 0));
		registerLight("ambient", new AmbientLight(Color.white, 0.125));
		registerLight("sun", sun.getLight());
		
		Cube cube = new Cube();
		registerPrefab("cube", cube);
	}
}
