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
	
	/**
	 * Point around which to rotate, relative to parent
	 */
	protected Vertex pivot;
	
	protected GameObject parent;
	protected Map<String, GameObject> children;
	
	public GameObject() {
		position = new Vertex();
		rotation = new Vector();
		pivot = new Vertex();
		parent = null;
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
	
	public Vertex getAbsolutePosition() {
		if (parent != null) {
			Vertex parentPosition = parent.getAbsolutePosition();
			double x = parentPosition.x + position.x;
			double y = parentPosition.y + position.y;
			double z = parentPosition.z + position.z;
			return new Vertex(x, y, z);
		}
		return new Vertex(position);
	}
	
	private void clampRotation(Vector rotation) {
		if (rotation.x < 0.0) {
			while (rotation.x < 360.0) {
				rotation.x += 360.0;
			}
		} else {
			while (rotation.x > 360.0) {
				rotation.x -= 360.0;
			}
		}
		if (rotation.y < 0.0) {
			while (rotation.y < 360.0) {
				rotation.y += 360.0;
			}
		} else {
			while (rotation.y > 360.0) {
				rotation.y -= 360.0;
			}
		}
		if (rotation.z < 0.0) {
			while (rotation.z < 360.0) {
				rotation.z += 360.0;
			}
		} else {
			while (rotation.z > 360.0) {
				rotation.z -= 360.0;
			}
		}
	}
	
	public void rotate(double x, double y, double z) {
		this.rotation.x += x;
		this.rotation.y += y;
		this.rotation.z += z;
		clampRotation(this.rotation);
	}
	
	public void rotate(Vector rotation) {
		this.rotation.x += rotation.x;
		this.rotation.y += rotation.y;
		this.rotation.z += rotation.z;
		clampRotation(this.rotation);
	}
	
	public void setRotation(Vector rotation) {
		this.rotation.setTo(rotation);
		clampRotation(this.rotation);
	}
	
	public Vector getRotation() {
		return new Vector(rotation);
	}
	
	public Vector getAbsoluteRotation() {
		if (parent != null) {
			Vector parentRotation = parent.getAbsoluteRotation();
			double x = parentRotation.x + rotation.x;
			double y = parentRotation.y + rotation.y;
			double z = parentRotation.z + rotation.z;
			return new Vector(x, y, z);
		}
		return new Vector(rotation);
	}
	
	public void setPivot(Vertex pivot) {
		this.pivot.setTo(pivot);
	}
	
	public Vertex getPivot() {
		return pivot;
	}
	
	public GameObject getChild(String child) {
		return this.children.get(child);
	}
	
	public List<GameObject> getChildren() {
		return new ArrayList<GameObject>(this.children.values());
	}
	
	public void registerChild(String name, GameObject child) {
		this.children.put(name, child);
		child.parent = this;
	}
	
	public GameObject deregisterChild(String name) {
		return children.remove(name);
	}
}
