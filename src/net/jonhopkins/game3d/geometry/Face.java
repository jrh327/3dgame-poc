package net.jonhopkins.game3d.geometry;

public class Face implements Comparable<Face> {
	public Vertex[] vertices;
	private int RGB;
	
	public Face(Vertex[] vertices, int rgb) {
		if (vertices.length < 3) {
			throw new IllegalArgumentException("Cannot have fewer than three vertices in a polygon");
		}
		this.vertices = vertices.clone();
		RGB = rgb;
	}
	
	public void rotateX(double theta) {
		for (Vertex vertex : vertices) {
			vertex.rotateX(theta);
		}
	}
	
	public void rotateY(double theta) {
		for (Vertex vertex : vertices) {
			vertex.rotateY(theta);
		}
	}
	
	public void rotateZ(double theta) {
		for (Vertex vertex : vertices) {
			vertex.rotateZ(theta);
		}
	}
	
	public int getRGB() {
		return RGB;
	}
	
	public Vertex[] getPoints() {
		return vertices.clone();
	}
	
	public double avgX() {
		double x = 0.0;
		int verts = vertices.length;
		for (int i = 0; i < verts; i++) {
			x += vertices[i].x;
		}
		return x / verts;
	}
	
	public double avgY() {
		double y = 0.0;
		int verts = vertices.length;
		for (int i = 0; i < verts; i++) {
			y += vertices[i].y;
		}
		return y / verts;
	}
	
	public double avgZ() {
		double z = 0.0;
		int verts = vertices.length;
		for (int i = 0; i < verts; i++) {
			z += vertices[i].z;
		}
		return z / verts;
	}
	
	public Vertex getCenter() {
		return new Vertex(avgX(), avgY(), avgZ());
	}
	
	public void to2DCoords(int halfScreenX, int halfScreenY, int[] xs, int[] ys) {
		int verts = vertices.length;
		if (xs.length != verts) {
			throw new IllegalArgumentException("Must have as many x values as vertices");
		}
		if (ys.length != verts) {
			throw new IllegalArgumentException("Must have as many y values as vertices");
		}
		
		double x = (double)halfScreenX;
		double y = (double)halfScreenY;
		
		for (int i = 0; i < verts; i++) {
			Vertex vertex = vertices[i];
			double z = vertex.z;
			if (z <= 0.0) {
				z = 0.1;
			}
			xs[i] = (int)(x + (vertex.x * (50.0 / z) * 10.0));
			ys[i] = (int)(y - (vertex.y * (50.0 / z) * 10.0));
		}
	}
	
	public Vector getNormal() {
		Vertex v1 = vertices[0];
		Vertex v2 = vertices[1];
		Vertex v3 = vertices[2];
		Vector pq = new Vector(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
		Vector pr = new Vector(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
		Vector cross = Vector.cross(pq, pr);
		return cross;
	}
	
	@Override
	public int compareTo(Face other) {
		double thisZ = this.avgZ();
		double otherZ = other.avgZ();
		
		if (thisZ > otherZ) {
			return -1;
		} else if (thisZ == otherZ) {
			return 0;
		}
		return 1;
	}
}
