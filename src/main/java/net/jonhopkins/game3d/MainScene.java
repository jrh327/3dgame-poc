package net.jonhopkins.game3d;

import java.awt.Color;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.AmbientLight;
import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.object.Cube;
import net.jonhopkins.game3d.object.MapSector;
import net.jonhopkins.game3d.object.Player;
import net.jonhopkins.game3d.object.Sun;

public class MainScene extends Scene {
	public MainScene() {
		super();
		
		Sun sun = new Sun();
		registerObject("sun", sun);
		MapSector sector = MapSector.getMapSector(0, 0);
		registerObject("sector_0_0", sector);
		registerLight("ambient", new AmbientLight(Color.white, 0.125));
		registerLight("sun", (Light)sun.getChild("sun_light"));
		
		registerObject("cube", new Cube());
		
		camera = new Camera(new Vertex(), new Vector());
		Player player = new Player(camera, sector);
		registerObject("player", player);
	}
}
