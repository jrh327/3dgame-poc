package net.jonhopkins.game3d.model;

import java.awt.Color;
import java.util.Calendar;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.light.DirectionalLight;
import net.jonhopkins.game3d.light.Light;

public class Sun extends Model {
	private DirectionalLight light;
	private final double maxLightLevel = 0.875;
	private double tod = 720;
	
	public Sun() {
		super(new Vertex[] {
				new Vertex(-10, -320, 10),
				new Vertex(10, -320, 10),
				new Vertex(10, -320, -10),
				new Vertex(-10, -320, -10)
		}, new int[][] {
			new int[] { 0, 1, 2 },
			new int[] { 0, 2, 3 }
		}, new int[] { 0xffff99, 0xffff99 });
		
		Vertex pivot = new Vertex(0.0, 0.0, 0.0);
		Bone[] children = new Bone[] {};
		Animation[] animations = new Animation[] {};
		
		primaryBone = new Bone(pivot, origVertices, children, animations);
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		tod = ((60 * hour + min) % 24) * 60 + sec;
		if (tod >= 1440.0) {
			tod = 0.0;
		}
		light = new DirectionalLight(new Vertex(0, -32, 0), new Vertex(0, 0, 0), Color.white, maxLightLevel);
	}
	
	@Override
	public void update(double timestep) {
		super.update(timestep);
		
		tod += timestep;
		if (tod >= 1440.0) {
			tod = 0.0;
		}
		
		/*if (tod < 240 || tod > 1320) {
			light.setIntensity(0.125);
		} else if (tod > 660 && tod < 900) {
			light.setIntensity(maxLightLevel);
		} else {
			if (tod <= 660) {
				light.setIntensity(tod / 60 - 3 + (tod % 60) / 60.0);
			} else if (tod >= 900) {
				light.setIntensity(tod / 60 - (9 + 2 * (tod / 60 - 16)) - (1 - (tod % 60) / 60.0));
			} else {
				light.setIntensity(0);
			}
		}*/
		
		double centerX = 0.0;
		double centerY = 0.0;
		double centerZ = 0.0;
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
	
	public Light getLight() {
		return light;
	}
}
