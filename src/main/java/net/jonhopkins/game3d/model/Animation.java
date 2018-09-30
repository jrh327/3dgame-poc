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
		reset();
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
		if (newKey && keys.length > 1) {
			Bone[] prevBones = keys[currentKey - 1].getBones();
			Vector[] prevTranslations = keys[currentKey - 1].getTranslations();
			Vector[] prevRotations = keys[currentKey - 1].getRotations();
			for (int i = 0; i < prevBones.length; i++) {
				previousTranslations.put(prevBones[i], new Vector(prevTranslations[i]));
				previousRotations.put(prevBones[i], new Vector(prevRotations[i]));
			}
		}
		
		for (int i = 0; i < bones.length; i++) {
			Vector curTranslation = bones[i].getTranslation();
			Vector startTranslation = previousTranslations.get(bones[i]);
			if (startTranslation == null) {
				startTranslation = bones[i].getTranslation();
			}
			Vector targetTranslation = translations[i];
			
			double dx = startTranslation.x + (targetTranslation.x - startTranslation.x) * fractionOfKey;
			double dy = startTranslation.y + (targetTranslation.y - startTranslation.y) * fractionOfKey;
			double dz = startTranslation.z + (targetTranslation.z - startTranslation.z) * fractionOfKey;
			
			curTranslation.x = dx;
			curTranslation.y = dy;
			curTranslation.z = dz;
			
			Vector curRotation = bones[i].getRotation();
			Vector startRotation = previousRotations.get(bones[i]);
			if (startRotation == null) {
				startRotation = bones[i].getRotation();
			}
			Vector targetRotation = rotations[i];
			
			dx = startRotation.x + (targetRotation.x - startRotation.x) * fractionOfKey;
			dy = startRotation.y + (targetRotation.y - startRotation.y) * fractionOfKey;
			dz = startRotation.z + (targetRotation.z - startRotation.z) * fractionOfKey;
			
			curRotation.x = dx;
			curRotation.y = dy;
			curRotation.z = dz;
		}
	}
	
	public void setActive(boolean active) {
		if (active && !this.active) {
			reset();
		}
		this.active = active;
	}
	
	public void reset() {
		timeElapsed = 0.0;
		currentKey = 0;
		Bone[] bones = keys[0].getBones();
		for (Bone bone : bones) {
			Vector translation = new Vector(bone.getTranslation());
			Vector rotation = new Vector(bone.getRotation());
			previousTranslations.put(bone, translation);
			previousRotations.put(bone, rotation);
		}
	}
	
	public boolean isActive() {
		return active;
	}
}
