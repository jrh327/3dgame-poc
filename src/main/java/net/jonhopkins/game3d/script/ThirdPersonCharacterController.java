package net.jonhopkins.game3d.script;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.model.Drawable;
import net.jonhopkins.game3d.object.MapSector;
import net.jonhopkins.game3d.object.Scriptable;

public class ThirdPersonCharacterController extends Script {
	private Camera camera;
	private double speedx = 10;
	private double speedz = 10;
	private MapSector currentSector;
	
	public ThirdPersonCharacterController(Scriptable object, Camera camera) {
		super(object);
		this.camera = camera;
	}
	
	@Override
	public void update(double timestep) {
		Vertex position = this.object.getPosition();
		position.x /= 10;
		position.z /= 10;
		
		double tempX = position.x;
		double tempZ = position.z;
		double cosY = Math.cos(camera.rotation.y * Math.PI / 180.0);
		double sinY = Math.sin(camera.rotation.y * Math.PI / 180.0);
		
		if (KeyboardInput.keyDown(KeyEvent.VK_W)) {
			tempZ += cosY * speedz * timestep;
			tempX -= sinY * speedx * timestep;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_A)) {
			tempX -= cosY * speedx * timestep;
			tempZ -= sinY * speedz * timestep;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_S)) {
			if (KeyboardInput.keyDown(KeyEvent.VK_CONTROL)) {
				//mapeditor.save(0, 0);
			} else {
				tempZ -= cosY * speedz * timestep;
				tempX += sinY * speedx * timestep;
			}
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_D)) {
			tempX += cosY * speedx * timestep;
			tempZ += sinY * speedz * timestep;
		}
		
		if (tempX <= -32.0) {
			tempX = -32.0;
		} else if (tempX >= 32.0) {
			tempX = 31.99;
		}
		if (tempZ <= -32.0) {
			tempZ = -31.999;
		} else if (tempZ >= 32.0) {
			tempZ = 32.0;
		}
		
		position.x = tempX;
		position.z = tempZ;
		
		int tileIndex = (int)(64 - (position.z + 32)) * 64 * 2 + (int)(position.x + 32) * 2;
		position.y = ((Drawable)currentSector.getChild("sector")).getFaces().get(tileIndex).avgY();
		
		position.x *= 10;
		position.z *= 10;
		
		this.object.setPosition(position);
	}
	
	public void setMapSector(MapSector sector) {
		this.currentSector = sector;
	}
}
