package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

class Bone {
	private Vertex pivot;
	private Vertex[] vertices;
	private Bone[] children;
	private Animation[] animations;
	
	public Bone(Vertex pivot, Vertex[] vertices, Bone[] children, Animation[] animations) {
		this.pivot = pivot;
		this.vertices = vertices;
		this.children = children;
		this.animations = animations;
	}
	
	public void update(double timestep) {
		for (Bone child : children) {
			child.update(timestep);
		}
		
		for (Animation animation : animations) {
			if (animation.isActive()) {
				double duration = animation.getDuration();
				Vector rotate = animation.getRotate();
				for (Vertex vertex : vertices) {
					vertex.translate(new Vector(-pivot.x, -pivot.y, -pivot.z));
					vertex.rotateX(rotate.x * timestep / duration);
					vertex.rotateY(rotate.y * timestep / duration);
					vertex.rotateZ(rotate.z * timestep / duration);
					vertex.translate(new Vector(pivot.x, pivot.y, pivot.z));
				}
			}
		}
		for (Animation animation : animations) {
			if (animation.isActive()) {
				double duration = animation.getDuration();
				Vector translate = animation.getTranslate();
				for (Vertex vertex : vertices) {
					vertex.translateX(translate.x * timestep / duration);
					vertex.translateY(translate.y * timestep / duration);
					vertex.translateZ(translate.z * timestep / duration);
				}
			}
		}
	}
}
