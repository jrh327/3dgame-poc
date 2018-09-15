package net.jonhopkins.game3d;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;

public class ThirdPersonCamera extends Camera {
	private double camVertSpeed = 50;
	private double camHorizSpeed = 50;
	private double distance = 50.0;
	
	public ThirdPersonCamera(Vertex position, Vector direction) {
		super(position, direction);
	}
	
	@Override
	public void update(double timestep) {
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
		
		updatePosition();
	}
	
	private void updatePosition() {
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
		this.position.setTo(v);
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
