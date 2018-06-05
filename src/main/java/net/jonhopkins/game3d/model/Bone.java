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
			child.rotateAndTranslate();
		}
		rotateAndTranslate(pivot, translation, rotation);
	}
	
	private void rotateAndTranslate(Vertex pivot, Vector translate, Vector rotate) {
		List<Vertex> verts = Arrays.asList(vertices);
		Vertex.translate(verts, new Vector(-pivot.x, -pivot.y, -pivot.z));
		Vertex.rotateX(verts, rotate.x);
		Vertex.rotateY(verts, rotate.y);
		Vertex.rotateZ(verts, rotate.z);
		Vertex.translate(verts, new Vector(pivot));
		Vertex.translate(verts, translate);
		
		for (Bone child : children) {
			child.rotateAndTranslate(pivot, translate, rotate);
		}
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
