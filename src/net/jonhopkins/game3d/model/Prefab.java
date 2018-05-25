package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.SceneObject;

public abstract class Prefab extends SceneObject {
	protected Model model;
	
	public Model getModel() {
		return model;
	}
	
	@Override
	public void update(double timestep) {
		super.update(timestep);
		model.update(timestep);
	}
}
