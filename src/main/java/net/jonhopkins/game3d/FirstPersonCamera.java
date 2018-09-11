package net.jonhopkins.game3d;

import java.awt.event.KeyEvent;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.input.KeyboardInput;

public class FirstPersonCamera extends Camera {
	private double camVertSpeed = 50;
	private double camHorizSpeed = 50;
	
	public FirstPersonCamera(Vertex position, Vector direction) {
		super(position, direction);
	}
	
	@Override
	public void update(double timestep) {
		if (KeyboardInput.keyDown(KeyEvent.VK_UP)) {
			relativeRotation.x += Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			relativeRotation.x -= Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			relativeRotation.y += Math.ceil(camHorizSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			relativeRotation.y -= Math.ceil(camHorizSpeed * timestep);
		}
		
		//Point p = mouse.getPosition();
		//rotatey -= p.x;
		//rotatex -= p.y;
		
		if (relativeRotation.y < 0) {
			relativeRotation.y += 360;
		} else if (relativeRotation.y >= 360) {
			relativeRotation.y -= 360;
		}
		if (relativeRotation.x < -89) {
			relativeRotation.x = -89;
		} else if (relativeRotation.x > 89) {
			relativeRotation.x = 89;
		}
	}
}
