package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.script.Script;

public interface Scriptable {
	public void updateScripts(double timestep);
	public Script getScript(String name);
	public void registerScript(String name, Script script);
	public Script deregisterScript(String name);
}
