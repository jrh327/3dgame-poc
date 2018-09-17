package net.jonhopkins.game3d.script;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.object.Scriptable;

public class FirstPersonCharacterController extends CharacterController {
	private double cameraHeight = 14.0;
	
	public FirstPersonCharacterController(Scriptable object, Camera camera) {
		super(object, camera);
		Vertex position = this.camera.getPosition();
		position.y = cameraHeight;
		this.camera.setPosition(position);
	}
	
	@Override
	protected void updatePlayer(double timestep) {
		Vertex position = object.getPosition();
		
		double tempX = position.x;
		double tempZ = position.z;
		
		double rotationY = -object.getRotation().y;
		double cosY = Math.cos(rotationY * Math.PI / 180.0);
		double sinY = Math.sin(rotationY * Math.PI / 180.0);
		
		if (KeyboardInput.keyDown(KeyEvent.VK_W)) {
			tempZ += cosY * speed * timestep;
			tempX -= sinY * speed * timestep;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_A)) {
			tempX -= cosY * speed * timestep;
			tempZ -= sinY * speed * timestep;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_S)) {
			tempZ -= cosY * speed * timestep;
			tempX += sinY * speed * timestep;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_D)) {
			tempX += cosY * speed * timestep;
			tempZ += sinY * speed * timestep;
		}
		
		if (tempX <= -320.0) {
			tempX = -320.0;
		} else if (tempX >= 320.0) {
			tempX = 319.999;
		}
		if (tempZ <= -320.0) {
			tempZ = -319.999;
		} else if (tempZ >= 320.0) {
			tempZ = 320.0;
		}
		
		position.x = tempX;
		position.z = tempZ;
		
		this.object.setPosition(position);
		
		snapPlayerToTerrain();
	}
	
	@Override
	protected void updateCamera(double timestep) {
		Vector objectRotation = object.getRotation();
		
		if (KeyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			objectRotation.y -= Math.ceil(camHorizSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			objectRotation.y += Math.ceil(camHorizSpeed * timestep);
		}
		if (objectRotation.y < 0.0) {
			objectRotation.y += 360.0;
		} else if (objectRotation.y >= 360.0) {
			objectRotation.y -= 360.0;
		}
		object.setRotation(objectRotation);
		
		Vector cameraRotation = camera.getRotation();
		if (KeyboardInput.keyDown(KeyEvent.VK_UP)) {
			cameraRotation.x -= Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			cameraRotation.x += Math.ceil(camVertSpeed * timestep);
		}
		if (cameraRotation.x < -89.0) {
			cameraRotation.x = -89.0; 
		} else if (cameraRotation.x > 89.0) {
			cameraRotation.x = 89.0;
		}
		camera.setRotation(cameraRotation);
	}
}
