package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.model.ModelFactory;
import net.jonhopkins.game3d.script.Script;

public class Cube extends Prefab {
	public Cube() {
		registerChild("cube", ModelFactory.getModel("cube.obj"));
		setPosition(-100.0, 10.0, 10.0);
		
		final GameObject cube = this.getChild("cube");
		registerScript("rotation_script", new Script(this) {
			@Override
			public void update(double timestep) {
				cube.rotate(timestep * 20.0, timestep * 10.0, timestep * -10.0);
			}
		});
	}
}
