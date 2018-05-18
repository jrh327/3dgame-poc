package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;

public class Model {
	protected Vertex[] origVertices;
	protected Vertex[] vertices;
	protected Face[] faces;
	protected Bone primaryBone;
	
	public Model(Vertex[] vertices, int[][] faceVertices, int[] faceColors) {
		this.origVertices = vertices;
		this.vertices = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			this.vertices[i] = new Vertex(this.origVertices[i]);
		}
		this.faces = new Face[faceVertices.length];
		for (int i = 0; i < faceVertices.length; i++) {
			int[] faceVerts = faceVertices[i];
			Vertex[] verts = new Vertex[faceVerts.length];
			for (int v = 0; v < verts.length; v++) {
				verts[v] = this.vertices[faceVerts[v]];
			}
			this.faces[i] = new Face(verts, faceColors[i]);
		}
	}
	
	public void update(double timestep) {
		resetVertices();
		if (primaryBone != null) {
			primaryBone.update(timestep);
		}
	}
	
	private void resetVertices() {
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].setTo(origVertices[i]);
		}
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Face[] getFaces() {
		return faces;
	}
}
