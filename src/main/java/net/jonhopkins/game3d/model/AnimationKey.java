package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vector;

public class AnimationKey {
	private Model model;
	private String[] boneNames;
	private Vector[] translations;
	private Vector[] rotations;
	
	public AnimationKey(Model model, String[] bones, Vector[] translations, Vector[] rotations) {
		this.model = model;
		this.boneNames = bones;
		this.translations = translations;
		this.rotations = rotations;
	}
	
	public Bone[] getBones() {
		Bone[] bones = new Bone[boneNames.length];
		for (int i = 0; i < bones.length; i++) {
			bones[i] = model.getBone(boneNames[i]);
		}
		return bones;
	}
	
	public Vector[] getTranslations() {
		return translations;
	}
	
	public Vector[] getRotations() {
		return rotations;
	}
}
