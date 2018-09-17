package net.jonhopkins.game3d.script;

import net.jonhopkins.game3d.Camera;
import net.jonhopkins.game3d.geometry.Vertex;
import net.jonhopkins.game3d.model.Drawable;
import net.jonhopkins.game3d.object.MapSector;
import net.jonhopkins.game3d.object.Scriptable;

public abstract class CharacterController extends Script {
	protected Camera camera;
	protected double speed = 100.0;
	protected double playerLateralSpeed;
	protected double camVertSpeed = 50;
	protected double camHorizSpeed = 50;
	protected double distance = 50.0;
	protected MapSector currentSector;
	
	public CharacterController(Scriptable object, Camera camera) {
		super(object);
		this.camera = camera;
	}
	
	@Override
	public void update(double timestep) {
		updateCamera(timestep);
		updatePlayer(timestep);
	}
	
	protected abstract void updatePlayer(double timestep);
	protected abstract void updateCamera(double timestep);
	
	public void setMapSector(MapSector sector) {
		this.currentSector = sector;
	}
	
	protected void snapPlayerToTerrain() {
		Vertex position = this.object.getPosition();
		double tempX = position.x / 10.0;
		double tempZ = position.z / 10.0;
		
		int tileIndex = (int)(64 - (tempZ + 32)) * 64 * 2 + (int)(tempX + 32) * 2;
		double tileY = ((Drawable)currentSector.getChild("sector")).getFaces().get(tileIndex).avgY();
		
		position.y = tileY;
		this.object.setPosition(position);
	}
	
}
