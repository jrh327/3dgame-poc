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
		scripts = new ArrayList<>();
	}
	
	public void update(double timestep) {
		model.update(timestep);
		for (Script script : scripts) {
			script.update(timestep);
		}
	}
	
	public Vertex getPosition() {
		return position;
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
