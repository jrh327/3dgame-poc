package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.script.CharacterController;

public class Player extends Prefab {
	public Player(Camera camera, MapSector sector) {
		CharacterController characterController = new CharacterController(this, camera);
		characterController.setMapSector(sector);
		registerScript("character_controller", characterController);
	}
}
