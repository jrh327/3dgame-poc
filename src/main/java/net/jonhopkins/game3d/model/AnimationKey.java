package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vector;

public class AnimationKey {
	private Model model;
	private String[] jointNames;
	private Vector[] translations;
	private Vector[] rotations;
	
	public AnimationKey(Model model, String[] joints, Vector[] translations, Vector[] rotations) {
		this.model = model;
		this.jointNames = joints;
		this.translations = translations;
		this.rotations = rotations;
	}
	
	public Joint[] getJoints() {
		Joint[] joints = new Joint[jointNames.length];
		for (int i = 0; i < joints.length; i++) {
			joints[i] = model.getRig().getJoint(jointNames[i]);
		}
		return joints;
	}
	
	public Vector[] getTranslations() {
		return translations;
	}
	
	public Vector[] getRotations() {
		return rotations;
	}
}
