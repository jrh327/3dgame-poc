package net.jonhopkins.game3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.object.GameObject;
import net.jonhopkins.game3d.object.Scriptable;

public abstract class Scene {
	protected Map<String, GameObject> objects;
	protected Map<String, Light> lights;
	protected Camera camera;
	
	protected Scene() {
		objects = new HashMap<>();
		lights = new HashMap<>();
	}
	
	public void update(double timestep) {
		camera.update(timestep);
		for (Light light : lights.values()) {
			light.update(timestep);
		}
		for (GameObject object : objects.values()) {
			if (object instanceof Scriptable) {
				((Scriptable)object).updateScripts(timestep);
			}
			object.update(timestep);
		}
	}
	
	public void registerObject(String name, GameObject object) {
		objects.put(name, object);
	}
	
	public GameObject getObject(String name) {
		return objects.get(name);
	}
	
	public GameObject deregisterObject(String name) {
		return objects.remove(name);
	}
	
	public List<GameObject> getObjects() {
		return new ArrayList<GameObject>(objects.values());
	}
	
	public void registerLight(String name, Light light) {
		lights.put(name, light);
	}
	
	public Light deregisterLight(String name) {
		return lights.remove(name);
	}
	
	public List<Light> getLights() {
		return new ArrayList<Light>(lights.values());
	}
	
	public Camera getCamera() {
		return camera;
	}
}
