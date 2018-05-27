package net.jonhopkins.game3d.model;

import java.util.ArrayList;
import java.util.List;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.object.GameObject;

public class Model extends GameObject implements Drawable {
	protected Vertex[] origVertices;
	protected List<Vertex> vertices;
	protected List<Face> faces;
	protected Bone primaryBone;
	
	public Model(Vertex[] vertices, int[][] faceVertices, int[] faceColors) {
		this.origVertices = vertices;
		this.vertices = new ArrayList<>(vertices.length);
		for (int i = 0; i < vertices.length; i++) {
			this.vertices.add(i, new Vertex(this.origVertices[i]));
		}
		this.faces = new ArrayList<>(faceVertices.length);
		for (int i = 0; i < faceVertices.length; i++) {
			int[] faceVerts = faceVertices[i];
			Vertex[] verts = new Vertex[faceVerts.length];
			for (int v = 0; v < verts.length; v++) {
				verts[v] = this.vertices.get(faceVerts[v]);
			}
			this.faces.add(i, new Face(verts, faceColors[i]));
		}
	}
	
	@Override
	public void update(double timestep) {
		resetVertices();
		if (primaryBone != null) {
			primaryBone.update(timestep);
		}
	}
	
	private void resetVertices() {
		for (int i = 0; i < origVertices.length; i++) {
			vertices.get(i).setTo(origVertices[i]);
		}
	}
	
	@Override
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	@Override
	public List<Face> getFaces() {
		return faces;
	}
}
