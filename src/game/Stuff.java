package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import itf.IPainter;
import itf.*;
import java.awt.*;

public abstract class Stuff extends Hitbox implements IPainter {

	public static final int STUFF_BOMB = 1;
	public static final int STUFF_PICK = 2;
	public static final int STUFF_UNBREAK = 3;
	public static final int STUFF_BREAK = 4;
	private int stuffType;
	
	public Stuff(int stuffType) {
		this.stuffType = stuffType;
	}

	public int getStuffType() {
		return this.stuffType;
	}

	public abstract Image getImage();
	
	@Override
	public void paint(Graphics g) {
		Point loc = getCenter();
		Image img = getImage();
		int x = img.getWidth(null);
		int y = img.getHeight(null);
		int ox = loc.x * StuffManager.GRID_WIDTH + StuffManager.DRAWRECT.x;
		int oy = loc.y * StuffManager.GRID_HEIGHT + StuffManager.DRAWRECT.y;
		g.drawImage(getImage(), 
				ox + (StuffManager.GRID_WIDTH - x) / 2, oy + (StuffManager.GRID_HEIGHT - y), 
				x, y, null);
	}

	public abstract void update(int milliseconds);
}