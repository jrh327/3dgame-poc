package net.jonhopkins.game3d.model;

import java.util.List;

import net.jonhopkins.game3d.geometry.Face;
import net.jonhopkins.game3d.geometry.Vertex;

public interface Drawable {
	public List<Vertex> getVertices();
	public List<Face> getFaces();
}
