package net.jonhopkins.game3d.model;

import java.util.ArrayList;
import java.util.List;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.script.Script;

public abstract class Prefab {
	protected Model model;
	protected Vertex position;
	protected Vector rotation;
	protected List<Script> scripts;
	
	public Model getModel() {
		return model;
	}
	
	public Prefab() {
		position = new Vertex();
		rotation = new Vector();
		scripts = new ArrayList<>();
	}
	
	public void updateScripts(double timestep) {
		for (Script script : scripts) {
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
	
	public void registerScript(Script script) {
		this.scripts.add(script);
	}
	
	public void deregisterScript(Script script) {
		this.scripts.remove(script);
	}
}
