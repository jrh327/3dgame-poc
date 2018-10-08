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
	private static final char VERT_IDENTIFIER = 'v';
	private static final char FACE_IDENTIFIER = 'f';
	private static final char JOINT_IDENTIFIER = 'j';
	private static final char KEY_IDENTIFIER = 'k';
	private static final char ANIM_IDENTIFIER = 'a';
	
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
		int numJoints = 0;
		int numKeys = 0;
		int numAnimations = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.isEmpty()) {
				continue;
			}
			switch (line.charAt(0)) {
			case VERT_IDENTIFIER:
				numVertices++;
				break;
			case FACE_IDENTIFIER:
				numFaces++;
				break;
			case JOINT_IDENTIFIER:
				numJoints++;
				break;
			case KEY_IDENTIFIER:
				numKeys++;
				break;
			case ANIM_IDENTIFIER:
				numAnimations++;
				break;
			default:
				break;
			}
		}
		
		Vertex[] vertices = new Vertex[numVertices];
		int[][] faces = new int[numFaces][];
		int[] colors = new int[numFaces];
		
		int jointCounter = 0;
		String[] jointNames = new String[numJoints];
		Vertex[] jointPivots = new Vertex[numJoints];
		int[][] jointChildren = new int[numJoints][];
		int[][] jointVerts = new int[numJoints][];
		
		int keyCounter = 0;
		String[][] keyJointNames = new String[numKeys][];
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
			case VERT_IDENTIFIER: {
				String[] parts = line.split(" ");
				double x = Double.valueOf(parts[1]);
				double y = Double.valueOf(parts[2]);
				double z = Double.valueOf(parts[3]);
				
				vertices[vertexCounter] = new Vertex(x, y, z);
				vertexCounter++;
				break;
			}
			case FACE_IDENTIFIER: {
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
			case JOINT_IDENTIFIER: {
				String[] parts = line.split(" ");
				
				jointNames[jointCounter] = parts[1];
				
				int numChildren = 0;
				for (int i = 2; i < parts.length; i++) {
					if (parts[i].charAt(0) != JOINT_IDENTIFIER) {
						break;
					}
					numChildren++;
				}
				
				int[] jchildren = new int[numChildren];
				jointChildren[jointCounter] = jchildren;
				for (int i = 0; i < numChildren; i++) {
					jchildren[i] = Integer.valueOf(parts[i + 2].substring(1)) - 1;
				}
				
				double pivotX = Double.valueOf(parts[numChildren + 2]);
				double pivotY = Double.valueOf(parts[numChildren + 3]);
				double pivotZ = Double.valueOf(parts[numChildren + 4]);
				jointPivots[jointCounter] = new Vertex(pivotX, pivotY, pivotZ);
				
				int[] jverts =  new int[parts.length - numChildren - 5];
				jointVerts[jointCounter] = jverts;
				int vert = 0;
				for (int i = numChildren + 5; i < parts.length; i++) {
					jverts[vert] = Integer.valueOf(parts[i]) - 1;
					vert++;
				}
				
				jointCounter++;
				break;
			}
			case KEY_IDENTIFIER: {
				String[] parts = line.split(" ");
				int numKeyJoints = (parts.length - 1) / 7;
				
				String[] keyJoints = new String[numKeyJoints];
				keyJointNames[keyCounter] = keyJoints;
				Vector[] translations  = new Vector[numKeyJoints];
				keyTranslations[keyCounter] = translations;
				Vector[] rotations = new Vector[numKeyJoints];
				keyRotations[keyCounter] = rotations;
				
				int joint = 1;
				for (int i = 0; i < numKeyJoints; i++) {
					keyJoints[i] = parts[joint];
					
					double translateX = Double.valueOf(parts[joint + 1]);
					double translateY = Double.valueOf(parts[joint + 2]);
					double translateZ = Double.valueOf(parts[joint + 3]);
					translations[i] = new Vector(translateX, translateY, translateZ);
					
					double rotateX = Double.valueOf(parts[joint + 4]);
					double rotateY = Double.valueOf(parts[joint + 5]);
					double rotateZ = Double.valueOf(parts[joint + 6]);
					rotations[i] = new Vector(rotateX, rotateY, rotateZ);
					
					joint += 7;
				}
				
				keyCounter++;
				
				break;
			}
			case ANIM_IDENTIFIER: {
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
		
		Joint[] joints = new Joint[numJoints];
		Joint[][] jointsChildren = new Joint[numJoints][];
		Vertex[][] jointsVertices = new Vertex[numJoints][];
		for (int i = 0; i < numJoints; i++) {
			jointsChildren[i] = new Joint[jointChildren[i].length];
			jointsVertices[i] = new Vertex[jointVerts[i].length];
			joints[i] = new Joint(jointNames[i], jointPivots[i], jointsVertices[i], jointsChildren[i]);
		}
		
		for (int i = 0; i < numJoints; i++) {
			Joint[] jchildren = jointsChildren[i];
			int[] childrenIndices = jointChildren[i];
			for (int j = 0; j < jchildren.length; j++) {
				jchildren[j] = joints[childrenIndices[j]];
			}
			
			Vertex[] jverts = jointsVertices[i];
			int[] verticesIndices = jointVerts[i];
			for (int j = 0; j < jverts.length; j++) {
				jverts[j] = vertices[verticesIndices[j]];
			}
		}
		
		Joint rootJoint = null;
		if (joints.length > 0) {
			rootJoint = joints[0];
		}
		Model model = new Model(vertices, faces, colors, rootJoint);
		
		AnimationKey[] keys = new AnimationKey[numKeys];
		for (int i = 0; i < numKeys; i++) {
			String[] keyJoint = keyJointNames[i];
			keys[i] = new AnimationKey(model, keyJoint, keyTranslations[i], keyRotations[i]);
		}
		
		for (int i = 0; i < numAnimations; i++) {
			int[] keyIndices = animationKeyIndices[i];
			AnimationKey[] animationKeys = new AnimationKey[keyIndices.length];
			for (int k = 0; k < animationKeys.length; k++) {
				animationKeys[k] = keys[keyIndices[k]];
			}
			animations.put(animationNames[i], new Animation(animationKeys, animationKeyTimes[i]));
		}
		
		model.setAnimations(animations);
		return model;
	}
}
