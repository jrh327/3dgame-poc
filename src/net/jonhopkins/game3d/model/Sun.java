package net.jonhopkins.game3d.model;

import net.jonhopkins.game3d.geometry.Vector;
import net.jonhopkins.game3d.geometry.Vertex;

public class Sun extends Model {
	
	public Sun() {
		super(new Vertex[] {
				new Vertex(-1, -32, 1),
				new Vertex(1, -32, 1),
				new Vertex(1, -32, -1),
				new Vertex(-1, -32, -1)
		}, new int[][] {
			new int[] { 0, 1, 2 },
			new int[] { 0, 2, 3 }
		}, new int[] { 0xffff99, 0xffff99 });
		
		Vertex pivot = new Vertex(0.0, 0.0, 0.0);
		Bone[] children = new Bone[] {};
		Animation[] animations = new Animation[] {
				new Animation(new Vector(0.0, 0.0, 0.0), new Vector(0.0, 0.0, 4.0), 1440)
		};
		
		primaryBone = new Bone(pivot, origVertices, children, animations);
	}
}
