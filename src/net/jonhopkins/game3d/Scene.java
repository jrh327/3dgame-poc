package net.jonhopkins.game3d;

import java.util.ArrayList;
import java.util.List;

import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.model.Model;
import net.jonhopkins.game3d.model.Prefab;

public abstract class Scene {
	protected List<Model> models;
	protected List<Prefab> prefabs;
	protected List<Light> lights;
	protected Camera camera;
	
	protected Scene() {
		models = new ArrayList<>();
		prefabs = new ArrayList<>();
		lights = new ArrayList<>();
	}
	
	public void update(double timestep) {
		for (Light light : lights) {
			light.update(timestep);
		}
		for (Model model : models) {
			model.update(timestep);
		}
		for (Prefab prefab : prefabs) {
			prefab.updateScripts(timestep);
			prefab.update(timestep);
		}
	}
	
	public void registerModel(Model model) {
		models.add(model);
	}
	
	public void deregisterModel(Model model) {
		models.remove(model);
	}
	
	public List<Model> getModels() {
		return models;
	}
	
	public void registerPrefab(Prefab prefab) {
		prefabs.add(prefab);
	}
	
	public void deregisterPrefab(Prefab prefab) {
		prefabs.remove(prefab);
	}
	
	public List<Prefab> getPrefabs() {
		return prefabs;
	}
	
	public void registerLight(Light light) {
		lights.add(light);
	}
	
	public void deregisterLight(Light light) {
		lights.remove(light);
	}
	
	public List<Light> getLights() {
		return lights;
	}
	
	public Camera getCamera() {
		return camera;
	}
}
