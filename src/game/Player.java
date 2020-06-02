package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import itf.IPainter;
import itf.*;
import java.awt.*;

public abstract class Player extends Hitbox implements IPainter {

	public static final int DIRECTTION_RIGHT = 1;
	public static final int DIRECTTION_LEFT = 2;
	public static final int DIRECTTION_UP = 3;
	public static final int DIRECTTION_DOWN = 4;
	public static final int STATE_STAND = 1;
	public static final int STATE_MOVING = 2;
	public static final int STATE_DEAD = 3;
	public static final int MOVINGDIR_NONE = 0;
	public static final int MOVINGDIR_RIGHT = 1;
	public static final int MOVINGDIR_LEFT = 2;
	public static final int MOVINGDIR_UP = 3;
	public static final int MOVINGDIR_DOWN = 4;
	public static final int TYPE_PLAYER = 1;
	public static final int TYPE_NONPLAYER = 2;
	private int type;
	private int id;
	private int state = STATE_STAND;
	private int moveDir = MOVINGDIR_NONE;
	private int movingTime = 0;
	protected static final int movingTimeThreshold = 200;
	private int bomb = 1;
	private int bombLimit = 1;
	protected static final int bombLimitMaximum = 10;
	private int itemCount = 0;

	private int bombPower = 1;
	protected static final int bombPowerLimit = 10;
	private boolean isDeading = false;
	private int deadingTime = 0;
	protected static final int deadTimeThreshold = 5000;

	public static int PLAYER = 1;
	public static int NONPLAYER = 2;
	private int ID;
	private int moveState = MOVINGDIR_NONE;
	private int health = 3;
	private static int healthLimit = 3;
	public static int bomPowerLimit = 10;

	public int getType() {
		return this.type;
	}

	public int getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getMoveDir() {
		return this.moveDir;
	}

	public void setMoveDir(int moveDir) {
		this.moveDir = moveDir;
	}

	public int getBomb() {
		return this.bomb;
	}

	public void setBomb(int bomb) {
		this.bomb = bomb;
	}

	/**
	 * 
	 * @param ID
	 */
	public Player(int type, int id) {
		this.type = type;
		this.id = id;
	}

	public abstract Image getImage();
	
	/**
	 * 嗑生命药
	 */
	protected void useItem() {
		if (isDeading && itemCount > 0) {
			--itemCount;
			isDeading = false;
			deadingTime = 0;
		}
	}

	/**
	 * 
	 * @param direction
	 * @return if can move, return 1
	 * else return 0
	 */
	public boolean canMove(int direction) {
		Point location = getCenter();
		StuffManager stuffManager = SceneManager.getNowScene().getStuffManager();
		int next_x = location.x;
		int next_y = location.y;
		switch (direction) {
		case MOVINGDIR_UP:
			next_y -= 1;
			break;
		case MOVINGDIR_DOWN:
			next_y += 1;
			break;
		case MOVINGDIR_LEFT:
			next_x -= 1;
			break;
		case MOVINGDIR_RIGHT:
			next_x += 1;
			break;
		default:
			return true;
		}
		
		if (next_x < 0 || next_x >= StuffManager.GRID_COUNT_X ||
				next_y < 0 || next_y >= StuffManager.GRID_COUNT_Y) {
			return false;
		}
		
		Grid grid = stuffManager.getStuffs()[next_x][next_y];
		Bomb b = grid.getBomb();
		UnbreakableStuff unb = grid.getUnbreakableStuff();
		BreakableStuff br = grid.getBreakableStuff();
		if (b != null && b.getBombState() == Bomb.BOMB_STAND) {
			return false;
		}
		else if (unb != null) {
			return false;
		}
		else if (br != null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		// paint player
		int dx = 0, dy = 0;
		switch (moveDir) {
		case MOVINGDIR_UP:
			dy = (-1) * (int)(((double)movingTime/(double)movingTimeThreshold) * StuffManager.GRID_HEIGHT);
			break;
		case MOVINGDIR_DOWN:
			dy = (int)(((double)movingTime/(double)movingTimeThreshold) * StuffManager.GRID_HEIGHT);
			break;
		case MOVINGDIR_LEFT:
			dx = (-1) * (int)(((double)movingTime/(double)movingTimeThreshold) * StuffManager.GRID_WIDTH);
			break;
		case MOVINGDIR_RIGHT:
			dx = (int)(((double)movingTime/(double)movingTimeThreshold) * StuffManager.GRID_WIDTH);
			break;
		default:
			break;
		}
		Image img = getImage();
		if (img == null) return;
		int img_width = img.getWidth(null);
		int img_height = img.getHeight(null);
		Point loc = getCenter();
		int x = loc.x * StuffManager.GRID_WIDTH + StuffManager.DRAWRECT.x + dx;
		int y = loc.y * StuffManager.GRID_HEIGHT + StuffManager.DRAWRECT.y + dy;
		g.drawImage(img, x + (StuffManager.GRID_WIDTH - img_width) / 2, y + (StuffManager.GRID_HEIGHT - img_height), 
				img_width, img_height, null);
	}
	
	public abstract void update(int milliseconds);
	
	public void layBomb() {
		if (isDeading || bomb <= 0) return;
		Point loc = getCenter();
		Grid[][] stuffs = SceneManager.getNowScene().getStuffManager().getStuffs();
		if (stuffs[loc.x][loc.y].getBomb() != null) return;
		Bomb b = new Bomb(id);
		b.setCenter(new Point(loc.x, loc.y));
		stuffs[loc.x][loc.y].setBomb(b);
		bomb -= 1;
	}

	public int getMovingTime() {
		return movingTime;
	}

	public void setMovingTime(int movingTime) {
		this.movingTime = movingTime;
	}

	public int getBombLimit() {
		return bombLimit;
	}

	public void setBombLimit(int bombLimit) {
		this.bombLimit = bombLimit;
	}

	public int getBombPower() {
		return bombPower;
	}

	public void setBombPower(int bombPower) {
		this.bombPower = bombPower;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDeading() {
		return isDeading;
	}

	public void setDeading(boolean isDeading) {
		this.isDeading = isDeading;
	}

	public int getDeadingTime() {
		return deadingTime;
	}

	public void setDeadingTime(int deadingTime) {
		this.deadingTime = deadingTime;
	}
	
	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getID() {
		return this.ID;
	}

	public int getMoveState() {
		return this.moveState;
	}

	public void setMoveState(int value) {
		this.moveState = value;
	}

	public int getHealth() {
		return this.health;
	}

	public void setHealth(int value) {
		this.health = value;
	}

	public Player(int ID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return  if can move, return 1else return 0
	 */
	public int move(int direction) {
		throw new UnsupportedOperationException();
	}
}