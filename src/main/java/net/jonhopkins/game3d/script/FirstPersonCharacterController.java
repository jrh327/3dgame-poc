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
		boolean movingForward = KeyboardInput.keyDown(KeyEvent.VK_W);
		boolean movingLeft = KeyboardInput.keyDown(KeyEvent.VK_A);
		boolean movingBackward = KeyboardInput.keyDown(KeyEvent.VK_S);
		boolean movingRight = KeyboardInput.keyDown(KeyEvent.VK_D);
		
		if (movingForward == movingBackward && movingLeft == movingRight) {
			// no movement at all
			// note that pressing opposing directions cancels out
			return;
		}
		
		Vertex position = object.getPosition();
		
		double tempX = position.x;
		double tempZ = position.z;
		
		double rotationY = -Math.toRadians(object.getRotation().y);
		double cosY = Math.cos(rotationY) * speed * timestep;
		double sinY = Math.sin(rotationY) * speed * timestep;
		
		if (movingForward && !movingBackward) {
			tempZ += cosY;
			tempX -= sinY;
		}
		if (movingLeft && !movingRight) {
			tempX -= cosY;
			tempZ -= sinY;
		}
		if (movingBackward && !movingForward) {
			tempZ -= cosY;
			tempX += sinY;
		}
		if (movingRight && !movingLeft) {
			tempX += cosY;
			tempZ += sinY;
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
