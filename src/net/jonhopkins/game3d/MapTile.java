package net.jonhopkins.game3d;

public class MapTile implements Comparable<MapTile> {
	public Point3D UL;
	public Point3D UR;
	public Point3D LL;
	public Point3D LR;
	private int RGB;
	
	public MapTile(Point3D ul, Point3D ur, Point3D ll, Point3D lr, int rgb) {
		UL = ul;
		UR = ur;
		LL = ll;
		LR = lr;
		RGB = rgb;
	}
	
	public void rotateX(double theta) {
		UL.rotateX(theta);
		UR.rotateX(theta);
		LL.rotateX(theta);
		LR.rotateX(theta);
	}
	
	public void rotateY(double theta) {
		UL.rotateY(theta);
		UR.rotateY(theta);
		LL.rotateY(theta);
		LR.rotateY(theta);
	}
	
	public void rotateZ(double theta) {
		UL.rotateZ(theta);
		UR.rotateZ(theta);
		LL.rotateZ(theta);
		LR.rotateZ(theta);
	}
	
	public int getRGB() {
		return RGB;
	}
	
	public Point3D[] getPoints() {
		return (new Point3D[] {
			UL, UR, LR, LL
		});
	}
	
	public double avgX() {
		return (UL.x + UR.x + LL.x + LR.x) / 4D;
	}
	
	public double avgZ() {
		return (UL.z + UR.z + LL.z + LR.z) / 4D;
	}
	
	public double avgHeight() {
		return (UL.y + UR.y + LL.y + LR.y) / 4D;
	}
	
	public void to2DCoords(int halfScreenX, int halfScreenY, int[] xs, int[] ys) {
		if (UL.z <= 0.0) {
			UL.z = 0.1;
		}
		if (UR.z <= 0.0) {
			UR.z = 0.1;
		}
		if (LL.z <= 0.0) {
			LL.z = 0.1;
		}
		if (LR.z <= 0.0) {
			LR.z = 0.1;
		}
		
		double x = (double)halfScreenX;
		double y = (double)halfScreenY;
		
		xs[0] = (int)(x + (UL.x * (50D / UL.z) * 10D));
		xs[1] = (int)(x + (UR.x * (50D / UR.z) * 10D));
		xs[2] = (int)(x + (LR.x * (50D / LR.z) * 10D));
		xs[3] = (int)(x + (LL.x * (50D / LL.z) * 10D));
		ys[0] = (int)(y - (UL.y * (50D / UL.z) * 10D));
		ys[1] = (int)(y - (UR.y * (50D / UR.z) * 10D));
		ys[2] = (int)(y - (LR.y * (50D / LR.z) * 10D));
		ys[3] = (int)(y - (LL.y * (50D / LL.z) * 10D));
	}
	
	@Override
	public int compareTo(MapTile other) {
		double thisZ = this.avgZ();
		double otherZ = this.avgZ();
		
		if (thisZ < otherZ) {
			return -1;
		} else if (thisZ == otherZ) {
			return 0;
		}
		return 1;
	}
}
