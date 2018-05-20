package net.jonhopkins.game3d.script;

import net.jonhopkins.game3d.model.Prefab;

public abstract class Script {
	protected Prefab prefab;
	
	public Script(Prefab prefab) {
		this.prefab = prefab;
	}
	
	public abstract void update(double timestep);
}
