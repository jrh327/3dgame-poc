package net.jonhopkins.game3d.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public abstract class GameObject {
	/**
	 * Position relative to parent, or relative to scene if top-level object
	 */
	protected Vertex position;
	
	/**
	 * Rotation relative to parent, or relative to scene if top-level object
	 */
	protected Vector rotation;
	protected Map<String, GameObject> children;
	
	public GameObject() {
		position = new Vertex();
		rotation = new Vector();
		children = new HashMap<>();
	}
	
	public void update(double timestep) {
		for (GameObject child : children.values()) {
			child.update(timestep);
		}
	}
	
	public void translate(Vector translate) {
		this.position.x += translate.x;
		this.position.y += translate.y;
		this.position.z += translate.z;
	}
	
	public void setPosition(double x, double y, double z) {
		this.position.setTo(x, y, z);
	}
	
	public void setPosition(Vertex position) {
		this.position.setTo(position);
	}
	
	public Vertex getPosition() {
		return new Vertex(position);
	}
	
	public void rotate(double x, double y, double z) {
		this.rotation.x += x;
		this.rotation.y += y;
		this.rotation.z += z;
	}
	
	public void rotate(Vector rotation) {
		this.rotation.x += rotation.x;
		this.rotation.y += rotation.y;
		this.rotation.z += rotation.z;
	}
	
	public void setRotation(Vector rotation) {
		this.rotation.setTo(rotation);
	}
	
	public Vector getRotation() {
		return rotation;
	}
	
	public GameObject getChild(String child) {
		return this.children.get(child);
	}
	
	public List<GameObject> getChildren() {
		return new ArrayList<GameObject>(this.children.values());
	}
	
	public void registerChild(String name, GameObject child) {
		this.children.put(name, child);
	}
	
	public GameObject deregisterChild(String name) {
		return children.remove(name);
	}
}
