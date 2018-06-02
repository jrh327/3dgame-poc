package net.jonhopkins.game3d.model;

class Animation {
	private AnimationKey[] keys;
	private double[] keyStarts;
	private double timeElapsed;
	private boolean active = false;
	
	public Animation(AnimationKey[] keys, double[] keyStarts) {
		this.keys = keys;
		this.keyStarts = keyStarts;
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
