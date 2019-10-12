package net.jonhopkins.game3d.object;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.model.Model;
import net.jonhopkins.game3d.model.ModelFactory;
import net.jonhopkins.game3d.model.Rig;

public class Person extends Prefab {
	public Person() {
		final Model model = ModelFactory.getModel("skeleton_default.obj");
		registerChild("human", model);
		//model.setScale(new Vector(0.75, 0.75, 0.75));
		Rig rig = model.getRig();
		registerChild("torso", ModelFactory.getModel("torso_default.obj"));
		rig.replacePart((Model)getChild("torso"));
		registerChild("legs", ModelFactory.getModel("legs_default.obj"));
		rig.replacePart((Model)getChild("legs"));
		registerChild("head", ModelFactory.getModel("head_default.obj"));
		rig.replacePart((Model)getChild("head"));
		registerChild("left_hand", ModelFactory.getModel("hand_left_default.obj"));
		rig.replacePart((Model)getChild("left_hand"));
		registerChild("right_hand", ModelFactory.getModel("hand_right_default.obj"));
		rig.replacePart((Model)getChild("right_hand"));
		registerChild("left_foot", ModelFactory.getModel("foot_left_default.obj"));
		rig.replacePart((Model)getChild("left_foot"));
		registerChild("right_foot", ModelFactory.getModel("foot_right_default.obj"));
		rig.replacePart((Model)getChild("right_foot"));
		
		model.setAnimationActive("test_animation", true);
	}
}
