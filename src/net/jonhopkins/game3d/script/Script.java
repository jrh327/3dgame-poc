package net.jonhopkins.game3d.script;

import net.jonhopkins.game3d.model.Prefab;

public abstract class Script {
	protected Prefab prefab;
	protected boolean enabled = true;
	
	public Script(Prefab prefab) {
		this.prefab = prefab;
	}
	
	public abstract void update(double timestep);
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
