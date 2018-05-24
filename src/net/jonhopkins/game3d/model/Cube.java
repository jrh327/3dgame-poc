package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.script.Script;

public class Cube extends Prefab {
	public Cube() {
		model = ModelFactory.getModel("cube.obj");
		position = new Vertex(-100.0, 10.0, 10.0);
		
		final Prefab cube = this;
		registerScript(new Script(cube) {
			@Override
			public void update(double timestep) {
				this.prefab.rotation.x += timestep * 20.0;
				this.prefab.rotation.y += timestep * 10.0;
				this.prefab.rotation.z += timestep * -10.0;
			}
		});
	}
}
