package net.jonhopkins.game3d;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.object.GameObject;

public class Camera extends GameObject {
	public Camera(Vertex position, Vector direction) {
		super();
		this.position = new Vertex(position);
		this.rotation = direction;
	}
}
