package net.jonhopkins.game3d.script;

import net.jonhopkins.game3d.object.GameObject;
import net.jonhopkins.game3d.object.Scriptable;

public abstract class Script {
	protected GameObject object;
	protected boolean enabled = true;
	
	public Script(Scriptable object) {
		this.object = (GameObject)object;
	}
	
	public abstract void update(double timestep);
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
