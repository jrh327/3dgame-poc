package net.jonhopkins.game3d.model;

import java.util.Arrays;
import java.util.List;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

class Bone {
	private String name;
	private Vertex origPivot;
	private Vertex pivot;
	private Vertex[] vertices;
	private Bone[] children;
	private Vector translation;
	private Vector rotation;
	private Vector scale;
	
	public Bone(String name, Vertex pivot, Vertex[] vertices, Bone[] children) {
		this.name = name;
		this.origPivot = new Vertex(pivot);
		this.pivot = pivot;
		this.vertices = vertices;
		this.children = children;
		this.translation = new Vector();
		this.rotation = new Vector();
		this.scale = new Vector(1.0, 1.0, 1.0);
	}
	
	public String getName() {
		return this.name;
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
	
	public Vector getScale() {
		return scale;
	}
	
	public void setScale(Vector scale) {
		this.scale.setTo(scale);
		scalePivot();
		for (Bone child : children) {
			child.setScale(scale);
		}
	}
	
	private void scalePivot() {
		pivot.x = origPivot.x * scale.x;
		pivot.y = origPivot.y * scale.y;
		pivot.z = origPivot.z * scale.z;
	}
	
	public Vector getTranslation() {
		return translation;
	}
	
	public Vector getRotation() {
		return rotation;
	}
}
