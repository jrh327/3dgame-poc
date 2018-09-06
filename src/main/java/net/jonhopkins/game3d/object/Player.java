package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.script.ThirdPersonCharacterController;

public class Player extends Prefab {
	public Player(Camera camera, MapSector sector) {
		ThirdPersonCharacterController characterController = new ThirdPersonCharacterController(this, camera);
		characterController.setMapSector(sector);
		registerScript("character_controller", characterController);
		registerChild("person", new Person());
	}
}
