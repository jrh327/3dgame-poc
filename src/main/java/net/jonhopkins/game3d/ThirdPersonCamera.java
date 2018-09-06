package net.jonhopkins.game3d;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;
import net.jonhopkins.game3d.object.GameObject;

public class ThirdPersonCamera extends Camera {
	private double camVertSpeed = 50;
	private double camHorizSpeed = 50;
	private GameObject target;
	private double distance = 50.0;
	
	public ThirdPersonCamera(Vertex position, Vector direction) {
		super(position, direction);
	}
	
	@Override
	public void update(double timestep) {
		boolean hasChanged = false;
		if (KeyboardInput.keyDown(KeyEvent.VK_UP)) {
			rotation.x -= Math.ceil(camVertSpeed * timestep);
			hasChanged = true;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			rotation.x += Math.ceil(camVertSpeed * timestep);
			hasChanged = true;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			rotation.y += Math.ceil(camHorizSpeed * timestep);
			hasChanged = true;
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			rotation.y -= Math.ceil(camHorizSpeed * timestep);
			hasChanged = true;
		}
		
		if (hasChanged) {
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
			
			Vertex v = new Vertex(distance, 0, 0);
			v.rotateX(-rotation.x);
			v.rotateY(-rotation.y);
			this.position.setTo(v);
			this.position.translate(new Vector(target.getPosition()));
			this.position.translateY(10.0);
		}
	}
	
	public void setTarget(GameObject object) {
		this.target = object;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
