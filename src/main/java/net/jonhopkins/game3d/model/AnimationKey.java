package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vector;

public class AnimationKey {
	private Bone[] bones;
	private Vector[] translations;
	private Vector[] rotations;
	
	public AnimationKey(Bone[] bones, Vector[] translations, Vector[] rotations) {
		this.bones = bones;
		this.translations = translations;
		this.rotations = rotations;
	}
	
	public Bone[] getBones() {
		return bones;
	}
	
	public Vector[] getTranslations() {
		return translations;
	}
	
	public Vector[] getRotations() {
		return rotations;
	}
}
