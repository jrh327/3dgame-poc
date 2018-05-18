package net.jonhopkins.game3d;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class Camera {
	public Vertex position;
	public Vector rotation;
	
	public Camera(Vertex position, Vector direction) {
		this.position = position;
		this.rotation = direction;
	}
}
