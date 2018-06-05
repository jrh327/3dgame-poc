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
	protected Vertex relativePosition;
	
	/**
	 * Absolute position within scene
	 */
	protected Vertex absolutePosition;
	
	/**
	 * Rotation relative to parent, or relative to scene if top-level object
	 */
	protected Vector relativeRotation;
	
	/**
	 * Absolute rotation within scene
	 */
	protected Vector absoluteRotation;
	
	/**
	 * Point around which to rotate, relative to parent
	 */
	protected Vertex pivot;
	
	protected Map<String, GameObject> children;
	
	public GameObject() {
		relativePosition = new Vertex();
		absolutePosition = new Vertex();
		relativeRotation = new Vector();
		absoluteRotation = new Vector();
		pivot = new Vertex();
		children = new HashMap<>();
	}
	
	public void update(double timestep) {
		resetPosition();
		resetRotation();
		for (GameObject child : children.values()) {
			child.update(timestep);
		}
	}
	
	public void translate(Vector translate) {
		this.relativePosition.x += translate.x;
		this.relativePosition.y += translate.y;
		this.relativePosition.z += translate.z;
		resetPosition();
	}
	
	public void setPosition(double x, double y, double z) {
		this.relativePosition.setTo(x, y, z);
		resetPosition();
	}
	
	public void setPosition(Vertex position) {
		this.relativePosition.setTo(position);
		resetPosition();
	}
	
	public Vertex getPosition() {
		return new Vertex(relativePosition);
	}
	
	public void translateAbsolute(Vector translate) {
		this.absolutePosition.x += translate.x;
		this.absolutePosition.y += translate.y;
		this.absolutePosition.z += translate.z;
	}
	
	public void setAbsolutePosition(double x, double y, double z) {
		this.absolutePosition.setTo(x, y, z);
	}
	
	public void setAbsolutePosition(Vertex position) {
		this.absolutePosition.setTo(position);
	}
	
	public Vertex getAbsolutePosition() {
		return absolutePosition;
	}
	
	public void resetPosition() {
		this.absolutePosition.setTo(this.relativePosition);
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
		this.relativeRotation.x += x;
		this.relativeRotation.y += y;
		this.relativeRotation.z += z;
		clampRotation(this.relativeRotation);
		resetRotation();
	}
	
	public void rotate(Vector rotation) {
		this.relativeRotation.x += rotation.x;
		this.relativeRotation.y += rotation.y;
		this.relativeRotation.z += rotation.z;
		clampRotation(this.relativeRotation);
		resetRotation();
	}
	
	public void setRotation(Vector rotation) {
		this.relativeRotation.setTo(rotation);
		clampRotation(this.relativeRotation);
		resetRotation();
	}
	
	public Vector getRotation() {
		return relativeRotation;
	}
	
	public void rotateAbsolute(double x, double y, double z) {
		this.absoluteRotation.x += x;
		this.absoluteRotation.y += y;
		this.absoluteRotation.z += z;
		clampRotation(this.absoluteRotation);
	}
	
	public void rotateAbsolute(Vector rotation) {
		this.absoluteRotation.x += rotation.x;
		this.absoluteRotation.y += rotation.y;
		this.absoluteRotation.z += rotation.z;
		clampRotation(this.absoluteRotation);
	}
	
	public void setAbsoluteRotation(Vector rotation) {
		this.absoluteRotation.setTo(rotation);
		clampRotation(this.absoluteRotation);
	}
	
	public Vector getAbsoluteRotation() {
		return absoluteRotation;
	}
	
	public void resetRotation() {
		this.absoluteRotation.x = relativeRotation.x;
		this.absoluteRotation.y = relativeRotation.y;
		this.absoluteRotation.z = relativeRotation.z;
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
	}
	
	public GameObject deregisterChild(String name) {
		return children.remove(name);
	}
}
