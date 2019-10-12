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
	
	private Map<String, Vector> previousTranslations;
	private Map<String, Vector> previousRotations;
	
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
			// to get the joints to the final position
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
		
		Joint[] joints = key.getJoints();
		Vector[] translations = key.getTranslations();
		Vector[] rotations = key.getRotations();
		if (newKey && keys.length > 1) {
			Joint[] prevJoints = keys[currentKey - 1].getJoints();
			Vector[] prevTranslations = keys[currentKey - 1].getTranslations();
			Vector[] prevRotations = keys[currentKey - 1].getRotations();
			for (int i = 0; i < prevJoints.length; i++) {
				previousTranslations.put(prevJoints[i].getName(), new Vector(prevTranslations[i]));
				previousRotations.put(prevJoints[i].getName(), new Vector(prevRotations[i]));
			}
		}
		
		for (int i = 0; i < joints.length; i++) {
			Vector curTranslation = joints[i].getTranslation();
			Vector startTranslation = previousTranslations.get(joints[i].getName());
			if (startTranslation == null) {
				startTranslation = joints[i].getTranslation();
			}
			Vector targetTranslation = translations[i];
			
			double dx = startTranslation.x + (targetTranslation.x - startTranslation.x) * fractionOfKey;
			double dy = startTranslation.y + (targetTranslation.y - startTranslation.y) * fractionOfKey;
			double dz = startTranslation.z + (targetTranslation.z - startTranslation.z) * fractionOfKey;
			
			curTranslation.x = dx;
			curTranslation.y = dy;
			curTranslation.z = dz;
			
			Vector curRotation = joints[i].getRotation();
			Vector startRotation = previousRotations.get(joints[i].getName());
			if (startRotation == null) {
				startRotation = joints[i].getRotation();
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
		Joint[] joints = keys[0].getJoints();
		for (Joint joint : joints) {
			Vector translation = new Vector(joint.getTranslation());
			Vector rotation = new Vector(joint.getRotation());
			previousTranslations.put(joint.getName(), translation);
			previousRotations.put(joint.getName(), rotation);
		}
	}
	
	public boolean isActive() {
		return active;
	}
}
