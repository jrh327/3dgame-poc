package net.jonhopkins.game3d.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.object.GameObject;

public class Model extends GameObject implements Drawable {
	protected Vertex[] origVertices;
	protected List<Vertex> vertices;
	protected List<Face> faces;
	protected Map<String, Animation> animations;
	protected Joint rootJoint;
	protected Vector scale;
	
	public Model(Vertex[] vertices, int[][] faceVertices, int[] faceColors) {
		this(vertices, faceVertices, faceColors, null);
	}
	
	public Model(Vertex[] vertices, int[][] faceVertices, int[] faceColors, Joint primaryBone) {
		super();
		this.vertices = Arrays.asList(vertices);
		this.origVertices = new Vertex[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			this.origVertices[i] = new Vertex(vertices[i]);
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
		this.animations = new HashMap<>();
		this.rootJoint = primaryBone;
		this.scale = new Vector(1.0, 1.0, 1.0);
	}
	
	@Override
	public void update(double timestep) {
		resetVertices();
		for (Animation animation : animations.values()) {
			animation.update(timestep);
		}
		if (rootJoint != null) {
			rootJoint.rotateAndTranslate();
		}
	}
	
	private void resetVertices() {
		for (int i = 0; i < origVertices.length; i++) {
			Vertex v = vertices.get(i);
			v.setTo(origVertices[i]);
			v.x *= scale.x;
			v.y *= scale.y;
			v.z *= scale.z;
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
	
	public Joint getJoint(String name) {
		if (rootJoint.getName().equals(name)) {
			return rootJoint;
		}
		return rootJoint.getChild(name);
	}
	
	public Vector getScale() {
		return scale;
	}
	
	public void setScale(Vector scale) {
		this.scale.setTo(scale);
		resetVertices();
		rootJoint.setScale(scale);
	}
	
	void setAnimations(Map<String, Animation> animations) {
		this.animations.putAll(animations);
	}
	
	public void setAnimationActive(String name, boolean active) {
		animations.get(name).setActive(active);
	}
}
