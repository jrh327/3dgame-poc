package net.jonhopkins.game3d.model;

import java.util.HashMap;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Vector;

class Animation {
	private AnimationKey[] keys;
	private double[] keyTimes;
	private double timeElapsed;
	private int currentKey;
	private boolean active = false;
	
	private Map<Bone, Vector> previousTranslations;
	private Map<Bone, Vector> previousRotations;
	
	public Animation(AnimationKey[] keys, double[] keyTimes) {
		this.keys = keys;
		this.keyTimes = keyTimes;
		this.timeElapsed = 0.0;
		
		this.previousTranslations = new HashMap<>();
		this.previousRotations = new HashMap<>();
	}
	
	public void update(double timestep) {
		if (!active) {
			return;
		}
		
		timeElapsed += timestep;
		
		boolean newKey = false;
		if (timeElapsed > keyTimes[keyTimes.length - 1]) {
			// to get the bones to the final position
			timeElapsed = keyTimes[keyTimes.length - 1];
			currentKey = keys.length - 1;
			
			// so the animation won't run next frame
			setActive(false);
			
			newKey = true;
		} else {
			while (timeElapsed > keyTimes[currentKey]) {
				currentKey++;
				newKey = true;
			}
		}
		
		AnimationKey key = keys[currentKey];
		double timeForKey = keyTimes[currentKey];
		if (currentKey > 0) {
			timeForKey -= keyTimes[currentKey - 1];
		}
		double fractionOfKey = (timeForKey - (keyTimes[currentKey] - timeElapsed)) / timeForKey;
		
		Bone[] bones = key.getBones();
		Vector[] translations = key.getTranslations();
		Vector[] rotations = key.getRotations();
		if (newKey) {
			for (Bone bone : bones) {
				previousTranslations.put(bone, new Vector(bone.getTranslation()));
				previousRotations.put(bone, new Vector(bone.getRotation()));
			}
		}
		
		for (int i = 0; i < bones.length; i++) {
			Vector curTranslation = bones[i].getTranslation();
			Vector targetTranslation = translations[i];
			
			double dx = (targetTranslation.x - curTranslation.x) * fractionOfKey;
			double dy = (targetTranslation.y - curTranslation.y) * fractionOfKey;
			double dz = (targetTranslation.z - curTranslation.z) * fractionOfKey;
			
			curTranslation.x += dx;
			curTranslation.y += dy;
			curTranslation.z += dz;
			
			Vector curRotation = bones[i].getRotation();
			Vector targetRotation = rotations[i];
			
			dx = (targetRotation.x - curRotation.x) * fractionOfKey;
			dy = (targetRotation.y - curRotation.y) * fractionOfKey;
			dz = (targetRotation.z - curRotation.z) * fractionOfKey;
			
			curRotation.x += dx;
			curRotation.y += dy;
			curRotation.z += dz;
		}
	}
	
	public void setActive(boolean active) {
		if (active && !this.active) {
			timeElapsed = 0.0;
			currentKey = 0;
		}
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
}
