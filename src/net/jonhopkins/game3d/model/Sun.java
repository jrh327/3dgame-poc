package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class Sun extends Model {
	
	public Sun() {
		super(null, null);
		
		Vertex pivot = new Vertex(0.0, 0.0, 0.0);
		vertices = new Vertex[] {
				new Vertex(-1, -32, 1),
				new Vertex(1, -32, 1),
				new Vertex(1, -32, -1),
				new Vertex(-1, -32, -1)
		};
		faces = new Face[] { new Face(vertices, 0xffff99) };
		Bone[] children = new Bone[] {};
		Animation[] animations = new Animation[] {
				new Animation(new Vector(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 4.0), 1440)
		};
		
		primaryBone = new Bone(pivot, vertices, children, animations);
	}
}
