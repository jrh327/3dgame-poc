package net.jonhopkins.game3d.script;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.geometry.Vector;
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
		Vertex position = this.object.getAbsolutePosition();
		
		double tempX = position.x / 10;
		double tempZ = position.z / 10;
		
		Vector rotation = camera.getRotation();
		double cosY = Math.cos(rotation.y * Math.PI / 180.0);
		double sinY = Math.sin(rotation.y * Math.PI / 180.0);
		
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
		
		int tileIndex = (int)(64 - (tempZ + 32)) * 64 * 2 + (int)(tempX + 32) * 2;
		double tempY = ((Drawable)currentSector.getChild("sector")).getFaces().get(tileIndex).avgY();
		
		tempX *= 10;
		tempZ *= 10;
		
		this.object.translateAbsolute(new Vector(tempX - position.x, tempY - position.y, tempZ - position.z));
		//this.object.setPosition(position);
	}
	
	public void setMapSector(MapSector sector) {
		this.currentSector = sector;
	}
}
