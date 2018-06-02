package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vertex;

class Bone {
	private Vertex pivot;
	private Vertex[] vertices;
	private Bone[] children;
	
	public Bone(Vertex pivot, Vertex[] vertices, Bone[] children) {
		this.pivot = pivot;
		this.vertices = vertices;
		this.children = children;
	}
	
	public Vertex getPivot() {
		return pivot;
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Bone[] getChildren() {
		return children;
	}
}
