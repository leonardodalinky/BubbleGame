package game;

import java.awt.Point;
import java.awt.*;

public class Hitbox {

	public static int HIT_RIGHT = 1;
	public static int HIT_UP = 3;
	public static int HIT_LEFT = 2;
	public static int HIT_DOWN = 4;
	private Point center;

	public Point getCenter() {
		return this.center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * 
	 * @param others
	 */
	public int checkHit(Hitbox others) {
		// deprecated
		throw new UnsupportedOperationException();
	}

}