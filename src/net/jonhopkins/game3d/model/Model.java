package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;

public class Model {
	protected Vertex[] vertices;
	protected Face[] faces;
	protected Bone primaryBone;
	
	public Model(Vertex[] vertices, Face[] faces) {
		this.vertices = vertices;
		this.faces = faces;
	}
	
	public void update(double timestep) {
		primaryBone.update(timestep);
	}
	
	public Vertex[] getVertices() {
		return this.vertices;
	}
	
	public Face[] getFaces() {
		return this.faces;
	}
}
