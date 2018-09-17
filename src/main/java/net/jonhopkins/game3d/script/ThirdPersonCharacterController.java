package net.jonhopkins.game3d.script;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.object.MapSector;
import net.jonhopkins.game3d.object.Scriptable;

public class ThirdPersonCharacterController extends CharacterController {
	public ThirdPersonCharacterController(Scriptable object, Camera camera) {
		super(object, camera);
	}
	
	@Override
	public void update(double timestep) {
		updatePlayer(timestep);
		updateCamera(timestep);
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
		
		double direction = 0.0;
		if (movingForward && !movingBackward) {
			if (movingRight && !movingLeft) {
				direction = 45.0;
			} else if (movingLeft && !movingRight) {
				direction = 135.0;
			} else {
				direction = 90.0;
			}
		} else if (movingBackward && !movingForward) {
			if (movingRight && !movingLeft) {
				direction = 315.0;
			} else if (movingLeft && !movingRight) {
				direction = 225.0;
			} else {
				direction = 270.0;
			}
		// can only get here if movingForward == movingBackward
		// since both pairs can't be equal, must be right or left
		} else if (movingRight) {
			direction = 0.0;
		} else {
			direction = 180.0;
		}
		
		Vertex position = this.object.getPosition();
		
		double tempX = position.x;
		double tempZ = position.z;
		
		double directionRads = Math.toRadians(direction);
		tempX += Math.cos(directionRads) * speed * timestep;
		tempZ += Math.sin(directionRads) * speed * timestep;
		
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
		
		this.object.translate(tempX - position.x, 0.0, tempZ - position.z);
		snapPlayerToTerrain();
		
		Vector rotation = this.object.getChild("person").getRotation();
		rotation.y = 180.0 - direction - 90.0;
		this.object.getChild("person").setRotation(rotation);
	}
	
	@Override
	protected void updateCamera(double timestep) {
		Vector rotation = camera.getRotation();
		if (KeyboardInput.keyDown(KeyEvent.VK_UP)) {
			rotation.x -= Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			rotation.x += Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			rotation.y -= Math.ceil(camHorizSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			rotation.y += Math.ceil(camHorizSpeed * timestep);
		}
		
		if (rotation.y < 0) {
			rotation.y += 360;
		} else if (rotation.y >= 360) {
			rotation.y -= 360;
		}
		if (rotation.x > -15) {
			rotation.x = -15;
		} else if (rotation.x < -75) {
			rotation.x = -75;
		}
		
		Vertex v = new Vertex(0, 10, distance);
		v.rotateX(rotation.x);
		v.rotateY(-rotation.y);
		v.x = -v.x;
		v.z = -v.z;
		camera.setPosition(v);
		camera.setRotation(rotation);
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void setMapSector(MapSector sector) {
		this.currentSector = sector;
		snapPlayerToTerrain();
	}
}
