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
			rotation.x += Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_DOWN)) {
			rotation.x -= Math.ceil(camVertSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_LEFT)) {
			rotation.y += Math.ceil(camHorizSpeed * timestep);
		}
		if (KeyboardInput.keyDown(KeyEvent.VK_RIGHT)) {
			rotation.y -= Math.ceil(camHorizSpeed * timestep);
		}
		
		//Point p = mouse.getPosition();
		//rotatey -= p.x;
		//rotatex -= p.y;
		
		if (rotation.y < 0) {
			rotation.y += 360;
		} else if (rotation.y >= 360) {
			rotation.y -= 360;
		}
		if (rotation.x < -89) {
			rotation.x = -89;
		} else if (rotation.x > 89) {
			rotation.x = 89;
		}
	}
}
