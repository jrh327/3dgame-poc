package net.jonhopkins.game3d.object;

import java.util.HashMap;
import java.util.Map;

import net.jonhopkins.game3d.script.Script;

public abstract class Prefab extends GameObject implements Scriptable {
	protected Map<String, Script> scripts = new HashMap<>();
	
	@Override
	public void updateScripts(double timestep) {
		for (GameObject child : children.values()) {
			if (child instanceof Scriptable) {
				((Scriptable)child).updateScripts(timestep);
			}
		}
		for (Script script : scripts.values()) {
			if (script.isEnabled()) {
				script.update(timestep);
			}
		}
	}
	
	@Override
	public void registerScript(String name, Script script) {
		this.scripts.put(name, script);
	}
	
	@Override
	public Script deregisterScript(String name) {
		return scripts.remove(name);
	}
}
