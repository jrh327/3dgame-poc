package net.jonhopkins.game3d.model;

class Animation {
	private AnimationKey[] keys;
	private double[] keyTimes;
	private double timeElapsed;
	private boolean active = false;
	
	public Animation(AnimationKey[] keys, double[] keyTimes) {
		this.keys = keys;
		this.keyTimes = keyTimes;
		this.timeElapsed = 0.0;
	}
	
	public void update(double timestep) {
		timeElapsed += timestep;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
}
