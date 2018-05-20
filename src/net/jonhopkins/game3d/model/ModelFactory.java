package net.jonhopkins.game3d.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import net.jonhopkins.game3d.geometry.Vertex;

public class ModelFactory {
	/**
	 * Generates a model from the given file.
	 * <p>
	 * The file is expected to contain each vertex and face on its own line. 
	 * Lines containing vertices and faces can be grouped together or mixed up. 
	 * A line containing a vertex begins with the lowercase letter v, followed 
	 * by three decimal values representing the vertex's x, y, and z coordinates. 
	 * A line containing a face begins with the lowercase letter f, followed by 
	 * the index of each of its vertices (starting with 0, and counting all 
	 * vertices in the file, in order), and finally the face's color, in 
	 * hexadecimal form and beginning with the # character (eg #ff6653). All 
	 * values are separated by a single space.
	 * 
	 * @param filename the path to the file containing this model's information
	 * @return the model
	 */
	public static Model getModel(String filename) {
		List<String> lines;
		try {
			lines = Files.readAllLines(new File("res/models/" + filename).toPath());
		} catch (IOException e) {
			e.printStackTrace();
			return new Model(new Vertex[0], new int[0][], new int[0]);
		}
		
		int numVertices = 0;
		int numFaces = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("v")) {
				numVertices++;
			} else if (line.startsWith("f")) {
				numFaces++;
			}
		}
		
		Vertex[] vertices = new Vertex[numVertices];
		int[][] faces = new int[numFaces][];
		int[] colors = new int[numFaces];
		
		int vertexCounter = 0;
		int faceCounter = 0;
		for (String line : lines) {
			String[] parts = line.split(" ");
			if (parts[0].equals("v")) {
				double x = Double.valueOf(parts[1]);
				double y = Double.valueOf(parts[2]);
				double z = Double.valueOf(parts[3]);
				
				vertices[vertexCounter] = new Vertex(x, y, z);
				vertexCounter++;
			} else if (parts[0].equals("f")) {
				int[] vertexIndices = new int[parts.length - 2];
				for (int i = 0; i < vertexIndices.length; i++) {
					vertexIndices[i] = Integer.valueOf(parts[i + 1]);
				}
				faces[faceCounter] = vertexIndices;
				colors[faceCounter] = Integer.valueOf(parts[parts.length - 1].substring(1), 16);
				faceCounter++;
			}
		}
		
		return new Model(vertices, faces, colors);
	}
}
