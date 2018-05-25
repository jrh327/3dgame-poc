package net.jonhopkins.game3d;

import java.util.HashMap;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.script.Script;

public abstract class SceneObject {
	protected Vertex position;
	protected Vector rotation;
	protected Map<String, SceneObject> children;
	protected Map<String, Script> scripts;

	public SceneObject() {
		position = new Vertex();
		rotation = new Vector();
		children = new HashMap<>();
		scripts = new HashMap<>();
	}
	
	public void updateScripts(double timestep) {
		for (SceneObject child : children.values()) {
			child.updateScripts(timestep);
		}
		for (Script script : scripts.values()) {
			if (script.isEnabled()) {
				script.update(timestep);
			}
		}
	}
	
	public void update(double timestep) {
		for (SceneObject child : children.values()) {
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
	
	public void registerChild(String name, SceneObject child) {
		this.children.put(name, child);
	}
	
	public SceneObject deregisterChild(String name) {
		return children.remove(name);
	}
	
	public void registerScript(String name, Script script) {
		this.scripts.put(name, script);
	}
	
	public Script deregisterScript(String name) {
		return scripts.remove(name);
	}
}
