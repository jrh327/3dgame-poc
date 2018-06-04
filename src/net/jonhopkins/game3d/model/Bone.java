package net.jonhopkins.game3d.model;

import java.util.Arrays;
import java.util.List;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

class Bone {
	private Vertex pivot;
	private Vertex[] vertices;
	private Bone[] children;
	private Vector translation;
	private Vector rotation;
	
	public Bone(Vertex pivot, Vertex[] vertices, Bone[] children) {
		this.pivot = pivot;
		this.vertices = vertices;
		this.children = children;
		this.translation = new Vector();
		this.rotation = new Vector();
	}
	
	public Vertex getPivot() {
		return pivot;
	}
	
	public void rotateAndTranslate() {
		for (Bone child : children) {
			child.pivot.translate(new Vector(-pivot.x, -pivot.y, -pivot.z));
			child.pivot.rotateX(rotation.x);
			child.pivot.rotateY(rotation.y);
			child.pivot.rotateZ(rotation.z);
			child.pivot.translate(translation);
			child.rotateAndTranslate();
			child.pivot.translate(new Vector(-translation.x, -translation.y, -translation.z));
			child.pivot.rotateZ(-rotation.z);
			child.pivot.rotateY(-rotation.y);
			child.pivot.rotateX(-rotation.x);
			child.pivot.translate(new Vector(pivot));
		}
		
		List<Vertex> verts = Arrays.asList(vertices);
		Vertex.translate(verts, new Vector(-pivot.x, -pivot.y, -pivot.z));
		Vertex.rotateX(verts, rotation.x);
		Vertex.rotateY(verts, rotation.y);
		Vertex.rotateZ(verts, rotation.z);
		Vertex.translate(verts, new Vector(pivot));
		Vertex.translate(verts, translation);
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Bone[] getChildren() {
		return children;
	}
	
	public Vector getTranslation() {
		return translation;
	}
	
	public Vector getRotation() {
		return rotation;
	}
}
