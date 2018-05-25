package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.script.Script;

public class Cube extends Prefab {
	public Cube() {
		model = ModelFactory.getModel("cube.obj");
		setPosition(-100.0, 10.0, 10.0);
		
		final Prefab cube = this;
		registerScript("rotation_script", new Script(cube) {
			@Override
			public void update(double timestep) {
				this.prefab.rotate(timestep * 20.0, timestep * 10.0, timestep * -10.0);
			}
		});
	}
}
