package net.jonhopkins.game3d.script;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.model.Drawable;
import net.jonhopkins.game3d.object.MapSector;
import net.jonhopkins.game3d.object.Scriptable;

public class ThirdPersonCharacterController extends Script {
	private double speed = 10.0;
	private MapSector currentSector;
	
	public ThirdPersonCharacterController(Scriptable object) {
		super(object);
	}
	
	@Override
	public void update(double timestep) {
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
		
		double tempX = position.x / 10;
		double tempZ = position.z / 10;
		
		double directionRads = Math.toRadians(direction);
		tempX += Math.cos(directionRads) * speed * timestep;
		tempZ += Math.sin(directionRads) * speed * timestep;
		
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
		double tileY = ((Drawable)currentSector.getChild("sector")).getFaces().get(tileIndex).avgY();
		
		tempX *= 10;
		tempZ *= 10;
		this.object.translate(new Vector(tempX - position.x, 0.0, tempZ - position.z));
		
		position = this.object.getPosition();
		position.y = tileY;
		this.object.setPosition(position);
		
		Vector rotation = this.object.getRotation();
		rotation.y = 180.0 - direction - 90.0;
		this.object.setRotation(rotation);
	}
	
	public void setMapSector(MapSector sector) {
		this.currentSector = sector;
	}
}
