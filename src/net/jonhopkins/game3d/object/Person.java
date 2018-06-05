package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.model.Model;
import net.jonhopkins.game3d.model.ModelFactory;
import net.jonhopkins.game3d.script.Script;

public class Person extends Prefab {
	public Person() {
		final Model model = ModelFactory.getModel("human.obj");
		registerChild("human", model);
		setPosition(10.0, 12.0, -50.0);
		
		registerScript("animation_script", new Script(this) {
			@Override
			public void update(double timestep) {
				model.setAnimationActive("test_animation", true);
			}
		});
	}
}
