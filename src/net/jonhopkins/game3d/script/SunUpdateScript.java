package net.jonhopkins.game3d.script;

import java.util.Calendar;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.Light;
import net.jonhopkins.game3d.object.Prefab;

public class SunUpdateScript extends Script {
	private double tod;
	private Vertex origPosition;
	
	public SunUpdateScript(Prefab prefab) {
		super(prefab);
		this.origPosition = new Vertex(prefab.getPosition());
		
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
		while (tod >= 1440.0) {
			tod = tod - 1440.0;
		}
		prefab.getRotation().z = tod * 360.0 / 1440.0;
		
		/*Vertex[] vertices = prefab.getModel().getVertices();
		for (Vertex vertex : vertices) {
			vertex.rotateZ(tod * 360.0 / 1440.0);
		}
		
		Vertex newPosition = new Vertex(this.origPosition);
		newPosition.rotateZ(tod * 360.0 / 1440.0);
		prefab.setPosition(newPosition);
		light.setPosition(prefab.getPosition());
		*/
	}
}
