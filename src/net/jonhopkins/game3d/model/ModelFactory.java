package net.jonhopkins.game3d.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		int numBones = 0;
		int numKeys = 0;
		int numAnimations = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			switch (line.charAt(0)) {
			case 'v':
				numVertices++;
				break;
			case 'f':
				numFaces++;
				break;
			case 'b':
				numBones++;
				break;
			case 'k':
				numKeys++;
				break;
			case 'a':
				numAnimations++;
				break;
			default:
				break;
			}
		}
		
		Vertex[] vertices = new Vertex[numVertices];
		int[][] faces = new int[numFaces][];
		int[] colors = new int[numFaces];
		Map<String, Animation> animations = new HashMap<>();
		Bone primaryBone = null;
		
		int boneCounter = 0;
		Vertex[] bonePivots = new Vertex[numBones];
		int[][] boneChildren = new int[numBones][];
		int[][] boneVerts = new int[numBones][];
		
		int vertexCounter = 0;
		int faceCounter = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			switch (line.charAt(0)) {
			case 'v': {
				String[] parts = line.split(" ");
				double x = Double.valueOf(parts[1]);
				double y = Double.valueOf(parts[2]);
				double z = Double.valueOf(parts[3]);
				
				vertices[vertexCounter] = new Vertex(x, y, z);
				vertexCounter++;
				break;
			}
			case 'f': {
				String[] parts = line.split(" ");
				int[] vertexIndices = new int[parts.length - 2];
				for (int i = 0; i < vertexIndices.length; i++) {
					vertexIndices[i] = Integer.valueOf(parts[i + 1]) - 1;
				}
				faces[faceCounter] = vertexIndices;
				colors[faceCounter] = Integer.valueOf(parts[parts.length - 1].substring(1), 16);
				faceCounter++;
				break;
			}
			case 'b': {
				String[] parts = line.split(" ");
				int numChildren = 0;
				for (int i = 1; i < parts.length; i++) {
					if (parts[i].charAt(0) != 'b') {
						break;
					}
					numChildren++;
				}
				
				int[] bchildren = new int[numChildren];
				boneChildren[boneCounter] = bchildren;
				for (int i = 0; i < numChildren; i++) {
					bchildren[i] = Integer.valueOf(parts[i + 1].substring(1));
				}
				
				double pivotX = Double.valueOf(parts[numChildren + 1]);
				double pivotY = Double.valueOf(parts[numChildren + 2]);
				double pivotZ = Double.valueOf(parts[numChildren + 3]);
				bonePivots[boneCounter] = new Vertex(pivotX, pivotY, pivotZ);
				
				int[] bverts =  new int[parts.length - numChildren - 4];
				boneVerts[boneCounter] = bverts;
				int vert = 0;
				for (int i = numChildren + 4; i < parts.length; i++) {
					bverts[vert] = Integer.valueOf(parts[i]);
					vert++;
				}
				
				boneCounter++;
				break;
			}
			case 'k': {
				String[] parts = line.split(" ");
				break;
			}
			case 'a': {
				String[] parts = line.split(" ");
				break;
			}
			default:
				break;
			}
		}
		
		Bone[] bones = new Bone[numBones];
		Bone[][] bonesChildren = new Bone[numBones][];
		Vertex[][] bonesVertices = new Vertex[numBones][];
		for (int i = 0; i < numBones; i++) {
			bonesChildren[i] = new Bone[boneChildren[i].length];
			bones[i] = new Bone(bonePivots[i], bonesVertices[i], bonesChildren[i], null);
		}
		
		for (int i = 0; i < numBones; i++) {
			Bone[] bchildren = bonesChildren[i];
			int[] childrenIndices = boneChildren[i];
			for (int j = 0; j < bchildren.length; j++) {
				bchildren[j] = bones[childrenIndices[j]];
			}
			
			Vertex[] bverts = bonesVertices[i];
			int[] verticesIndices = boneVerts[i];
			for (int j = 0; j < bchildren.length; j++) {
				bverts[j] = vertices[verticesIndices[j]];
			}
		}
		
		return new Model(vertices, faces, colors, animations, primaryBone);
	}
}
