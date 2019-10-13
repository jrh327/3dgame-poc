package net.jonhopkins.game3d.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class Rig {
	protected Joint rootJoint;
	protected Map<String, Model> models;
	
	public Rig() {
		this(new Joint("root", new Vertex(), new Vertex[]{}, new Joint[] {}));
	}
	
	public Rig(Joint primaryJoint) {
		this.rootJoint = primaryJoint;
		this.models = new HashMap<>();
	}
	
	public void replacePart(Model part) {
		Joint joint = part.getRig().getRoot();
		boolean foundMatch = false;
		if (rootJoint.getName().equals(joint.getName())) {
			part.snapTo(rootJoint);
			rootJoint = joint;
			foundMatch = true;
		} else {
			Joint origJoint = getJoint(joint.getName());
			if (origJoint != null) {
				part.snapTo(origJoint);
				rootJoint.setChild(joint);
				foundMatch = true;
			}
		}
		
		if (foundMatch) {
			setJointModels(joint, part);
		}
	}
	
	protected void setJointModels(Joint joint, Model model) {
		models.put(joint.getName(), model);
	}
	
	public Joint getRoot() {
		return rootJoint;
	}
	
	public Joint getJoint(String name) {
		if (rootJoint.getName().equals(name)) {
			return rootJoint;
		}
		return rootJoint.getChild(name);
	}
	
	public List<Model> getModels() {
		List<Model> modelSet = new ArrayList<>();
		for (Model model : models.values()) {
			if (!modelSet.contains(model)) {
				modelSet.add(model);
			}
		}
		return modelSet;
	}
	
	public void rotateAndTranslate() {
		rootJoint.rotateAndTranslate();
	}
	
	public void setScale(Vector scale) {
		rootJoint.setScale(scale);
	}
}
