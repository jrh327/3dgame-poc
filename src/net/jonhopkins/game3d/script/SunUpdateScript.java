package net.jonhopkins.game3d.script;

import java.util.Calendar;

import net.jonhopkins.game3d.object.Scriptable;

public class SunUpdateScript extends Script {
	private double tod;
	
	public SunUpdateScript(Scriptable object) {
		super(object);
		
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
		object.getRotation().z = tod * 360.0 / 1440.0;
	}
}
