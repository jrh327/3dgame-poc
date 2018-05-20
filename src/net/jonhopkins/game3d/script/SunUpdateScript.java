package net.jonhopkins.game3d.script;

import java.util.Calendar;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.model.Prefab;

public class SunUpdateScript extends Script {
	private double tod;
	private Light light;
	
	public SunUpdateScript(Prefab prefab, Light light) {
		super(prefab);
		this.light = light;
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		tod = ((60 * hour + min) % 24) * 60 + sec;
		if (tod >= 1440.0) {
			tod = 0.0;
		}
	}
	
	public void update(double timestep) {
		tod += timestep;
		if (tod >= 1440.0) {
			tod = 0.0;
		}
		
		double centerX = 0.0;
		double centerY = 0.0;
		double centerZ = 0.0;
		Vertex[] vertices = prefab.getModel().getVertices();
		for (Vertex vertex : vertices) {
			vertex.rotateZ(-(tod - 60.0) * 360.0 / 1440.0);
			centerX += vertex.x;
			centerY += vertex.y;
			centerZ += vertex.z;
		}
		
		Vertex lightPosition = light.getPosition();
		lightPosition.x = (centerX / vertices.length);
		lightPosition.y = (centerY / vertices.length);
		lightPosition.z = (centerZ / vertices.length);
	}
}
