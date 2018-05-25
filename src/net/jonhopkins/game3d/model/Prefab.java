package net.jonhopkins.game3d.model;

import java.util.HashMap;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.script.Script;

public abstract class Prefab {
	protected Model model;
	protected Vertex position;
	protected Vector rotation;
	protected Map<String, Script> scripts;
	
	public Model getModel() {
		return model;
	}
	
	public Prefab() {
		position = new Vertex();
		rotation = new Vector();
		scripts = new HashMap<>();
	}
	
	public void updateScripts(double timestep) {
		for (Script script : scripts.values()) {
			script.update(timestep);
		}
	}
	
	public void update(double timestep) {
		model.update(timestep);
	}
	
	public void translate(Vector translate) {
		this.position.x += translate.x;
		this.position.y += translate.y;
		this.position.z += translate.z;
	}
	
	public void setPosition(Vertex position) {
		this.position.setTo(position);
	}
	
	public Vertex getPosition() {
		return position;
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
	
	public void registerScript(String name, Script script) {
		this.scripts.put(name, script);
	}
	
	public Script deregisterScript(String name) {
		return scripts.remove(name);
	}
}
