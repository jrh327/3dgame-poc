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
		if (KeyboardInput.keyDown(KeyEvent.VK_UP)) {
			relativeRotation.x -= Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			relativeRotation.x += Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			relativeRotation.y -= Math.ceil(camHorizSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			relativeRotation.y += Math.ceil(camHorizSpeed * timestep);
		}
		
		updatePosition();
	}
	
	private void updatePosition() {
		if (relativeRotation.y < 0) {
			relativeRotation.y += 360;
		} else if (relativeRotation.y >= 360) {
			relativeRotation.y -= 360;
		}
		if (relativeRotation.x > -15) {
			relativeRotation.x = -15;
		} else if (relativeRotation.x < -75) {
			relativeRotation.x = -75;
		}
		
		Vertex v = new Vertex(0, 10, distance);
		v.rotateX(relativeRotation.x);
		v.rotateY(-relativeRotation.y);
		v.x = -v.x;
		v.z = -v.z;
		this.relativePosition.setTo(v);
	}
	
	public void setTarget(GameObject object) {
		this.target = object;
		updatePosition();
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
