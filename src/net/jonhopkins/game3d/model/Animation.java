package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vector;

class Animation {
	private Vector translate;
	private Vector rotate;
	private double duration;
	private boolean active = false;
	
	public Animation(Vector translate, Vector rotate, double duration) {
		this.translate = translate;
		this.rotate = rotate;
		this.duration = duration;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Vector getTranslate() {
		return translate;
	}
	
	public Vector getRotate() {
		return rotate;
	}
	
	public double getDuration() {
		return duration;
	}
}
