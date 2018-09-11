package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.model.Model;
import net.jonhopkins.game3d.model.ModelFactory;
import net.jonhopkins.game3d.script.Script;

public class Person extends Prefab {
	public Person() {
		final Model model = ModelFactory.getModel("human.obj");
		registerChild("human", model);
		setPosition(0.0, 0.0, 0.0);
		model.setScale(new Vector(0.75, 0.75, 0.75));
		
		registerScript("animation_script", new Script(this) {
			@Override
			public void update(double timestep) {
				model.setAnimationActive("test_animation", true);
			}
		});
	}
}
