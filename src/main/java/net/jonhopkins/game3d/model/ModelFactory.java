package net.jonhopkins.game3d.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jonhopkins.game3d.geometry.Vector;
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
			lines = Files.readAllLines(new File("src/main/resources/models/" + filename).toPath());
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
		
		int boneCounter = 0;
		String[] boneNames = new String[numBones];
		Vertex[] bonePivots = new Vertex[numBones];
		int[][] boneChildren = new int[numBones][];
		int[][] boneVerts = new int[numBones][];
		
		int keyCounter = 0;
		int[][] keyBoneIndices = new int[numKeys][];
		Vector[][] keyTranslations = new Vector[numKeys][];
		Vector[][] keyRotations = new Vector[numKeys][];
		
		int animationCounter = 0;
		String[] animationNames = new String[numAnimations];
		int[][] animationKeyIndices = new int[numAnimations][];
		double[][] animationKeyTimes = new double[numAnimations][];
		Map<String, Animation> animations = new HashMap<>();
		
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
				
				boneNames[boneCounter] = parts[1];
				
				int numChildren = 0;
				for (int i = 2; i < parts.length; i++) {
					if (parts[i].charAt(0) != 'b') {
						break;
					}
					numChildren++;
				}
				
				int[] bchildren = new int[numChildren];
				boneChildren[boneCounter] = bchildren;
				for (int i = 0; i < numChildren; i++) {
					bchildren[i] = Integer.valueOf(parts[i + 2].substring(1)) - 1;
				}
				
				double pivotX = Double.valueOf(parts[numChildren + 2]);
				double pivotY = Double.valueOf(parts[numChildren + 3]);
				double pivotZ = Double.valueOf(parts[numChildren + 4]);
				bonePivots[boneCounter] = new Vertex(pivotX, pivotY, pivotZ);
				
				int[] bverts =  new int[parts.length - numChildren - 5];
				boneVerts[boneCounter] = bverts;
				int vert = 0;
				for (int i = numChildren + 5; i < parts.length; i++) {
					bverts[vert] = Integer.valueOf(parts[i]) - 1;
					vert++;
				}
				
				boneCounter++;
				break;
			}
			case 'k': {
				String[] parts = line.split(" ");
				int numKeyBones = (parts.length - 1) / 7;
				
				int[] keyBones = new int[numKeyBones];
				keyBoneIndices[keyCounter] = keyBones;
				Vector[] translations  = new Vector[numKeyBones];
				keyTranslations[keyCounter] = translations;
				Vector[] rotations = new Vector[numKeyBones];
				keyRotations[keyCounter] = rotations;
				
				int bone = 1;
				for (int i = 0; i < numKeyBones; i++) {
					keyBones[i] = Integer.valueOf(parts[bone]) - 1;
					
					double translateX = Double.valueOf(parts[bone + 1]);
					double translateY = Double.valueOf(parts[bone + 2]);
					double translateZ = Double.valueOf(parts[bone + 3]);
					translations[i] = new Vector(translateX, translateY, translateZ);
					
					double rotateX = Double.valueOf(parts[bone + 4]);
					double rotateY = Double.valueOf(parts[bone + 5]);
					double rotateZ = Double.valueOf(parts[bone + 6]);
					rotations[i] = new Vector(rotateX, rotateY, rotateZ);
					
					bone += 7;
				}
				
				keyCounter++;
				
				break;
			}
			case 'a': {
				String[] parts = line.split(" ");
				int numAnimationKeys = (parts.length - 2) / 2;
				
				int[] keyIndices = new int[numAnimationKeys];
				animationKeyIndices[animationCounter] = keyIndices;
				animationKeyTimes[animationCounter] = new double[numAnimationKeys];
				
				animationNames[animationCounter] = parts[1];
				
				int animation = 2;
				for (int i = 0; i < numAnimationKeys; i++) {
					keyIndices[i] = Integer.valueOf(parts[animation]) - 1;
					animationKeyTimes[animationCounter][i] = Double.valueOf(parts[animation + 1]);
					animation += 2;
				}
				
				animationCounter++;
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
			bonesVertices[i] = new Vertex[boneVerts[i].length];
			bones[i] = new Bone(boneNames[i], bonePivots[i], bonesVertices[i], bonesChildren[i]);
		}
		
		for (int i = 0; i < numBones; i++) {
			Bone[] bchildren = bonesChildren[i];
			int[] childrenIndices = boneChildren[i];
			for (int j = 0; j < bchildren.length; j++) {
				bchildren[j] = bones[childrenIndices[j]];
			}
			
			Vertex[] bverts = bonesVertices[i];
			int[] verticesIndices = boneVerts[i];
			for (int j = 0; j < bverts.length; j++) {
				bverts[j] = vertices[verticesIndices[j]];
			}
		}
		
		AnimationKey[] keys = new AnimationKey[numKeys];
		for (int i = 0; i < numKeys; i++) {
			int[] boneIndices = keyBoneIndices[i];
			Bone[] keyBones = new Bone[boneIndices.length];
			for (int b = 0; b < keyBones.length; b++) {
				keyBones[b] = bones[boneIndices[b]];
			}
			keys[i] = new AnimationKey(keyBones, keyTranslations[i], keyRotations[i]);
		}
		
		for (int i = 0; i < numAnimations; i++) {
			int[] keyIndices = animationKeyIndices[i];
			AnimationKey[] animationKeys = new AnimationKey[keyIndices.length];
			for (int k = 0; k < animationKeys.length; k++) {
				animationKeys[k] = keys[keyIndices[k]];
			}
			animations.put(animationNames[i], new Animation(animationKeys, animationKeyTimes[i]));
		}
		
		Bone primaryBone = null;
		if (bones.length > 0) {
			primaryBone = bones[0];
		}
		return new Model(vertices, faces, colors, animations, primaryBone);
	}
}
