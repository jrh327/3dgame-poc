package net.jonhopkins.game3d;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.model.Prefab;

public abstract class Scene {
	protected Map<String, Prefab> prefabs;
	protected Map<String, Light> lights;
	protected Camera camera;
	
	protected Scene() {
		prefabs = new HashMap<>();
		lights = new HashMap<>();
	}
	
	public void update(double timestep) {
		for (Light light : lights.values()) {
			light.update(timestep);
		}
		for (Prefab prefab : prefabs.values()) {
			prefab.updateScripts(timestep);
			prefab.update(timestep);
		}
	}
	
	public void registerPrefab(String name, Prefab prefab) {
		prefabs.put(name, prefab);
	}
	
	public Prefab deregisterPrefab(String name) {
		return prefabs.remove(name);
	}
	
	public Collection<Prefab> getPrefabs() {
		return prefabs.values();
	}
	
	public void registerLight(String name, Light light) {
		lights.put(name, light);
	}
	
	public Light deregisterLight(String name) {
		return lights.remove(name);
	}
	
	public Collection<Light> getLights() {
		return lights.values();
	}
	
	public Camera getCamera() {
		return camera;
	}
}
