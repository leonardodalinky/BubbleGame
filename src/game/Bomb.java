package game;

import java.awt.Image;
import java.awt.Point;
import java.awt.*;

public class Bomb extends Stuff {

	public static final int BOMB_STAND = 1;	// 炸弹正在蓄力
	public static final int BOMB_EXPLODE = 2;	// 炸弹正在炸水花
	public static final int BOMB_DONE = 3;	// 炸弹已完成,可以消失
	public static final int EXPLODE_THRESHOLD = 2500;
	public static final int DONE_THRESHOLD = 1000;
	private static final int[] DIRECTION_DX = new int[]{0, 0, -1, 1};
	private static final int[] DIRECTION_DY = new int[]{-1, 1, 0, 0};
	private int ownerID;
	private int livingTime = 0;
	private int bombState = BOMB_STAND;
	// 保证水花时钟同步
	private boolean isNewSplash = false;
	
	public static int BOMB_REMAINING = 3;

	public Bomb(int ownerID) {
		super(Stuff.STUFF_BOMB);
		this.ownerID = ownerID;
	}

	public int getOwnerID() {
		return this.ownerID;
	}

	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}

	public int getLivingTime() {
		return this.livingTime;
	}

	public void setLivingTime(int livingTime) {
		this.livingTime = livingTime;
	}

	/**
	 * 更新炸弹的状态,并且更新被他影响的物体的状态
	 * @param milliseconds
	 */
	public void update(int milliseconds) {
		livingTime += milliseconds;
		if (bombState == BOMB_STAND && livingTime >= EXPLODE_THRESHOLD && livingTime < EXPLODE_THRESHOLD + DONE_THRESHOLD) {
			// trigger explosion
			triggerExplosion(this);
		}
		else if (bombState == BOMB_EXPLODE && livingTime >= DONE_THRESHOLD) {
			bombState = BOMB_DONE;
		}
	}

	@Override
	public Image getImage() {
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		if (bombState == BOMB_STAND) {
			int index = (livingTime / 300) % 3 + 1;
			return rsc.getResource(String.format("item_bomb%d", index));
		}
		else if (bombState == BOMB_EXPLODE) {
			Grid[][] stuffs = SceneManager.getNowScene().getStuffManager().getStuffs();
			Point loc = getCenter();
			for (int dir = 0;dir < 4;++dir) {
				int dx = DIRECTION_DX[dir];
				int dy = DIRECTION_DY[dir];
				int x = loc.x + dx;
				int y = loc.y + dy;
				if (x < 0 || x >= StuffManager.GRID_COUNT_X || y < 0 || y >= StuffManager.GRID_COUNT_Y)
					continue;
				if (stuffs[x][y].getBomb() != null && stuffs[x][y].getBomb().bombState == BOMB_EXPLODE) {
					if (dir == 0 || dir == 1) {
						return rsc.getResource("item_bombu");
					}
					else {
						return rsc.getResource("item_bombr");
					}
				}
			}
			return rsc.getResource("item_bombu");
		}
		else {
			return null;
		}
	}

	public int getBombState() {
		return bombState;
	}

	public void setBombState(int bombState) {
		this.bombState = bombState;
	}

	public static void triggerExplosion(Bomb bomb) {
		if (bomb.getBombState() != BOMB_STAND) return;
		StuffManager stuffManager = SceneManager.getNowScene().getStuffManager();
		Player ownerPlayer = stuffManager.getPlayers()[bomb.getOwnerID()];
		Grid[][] stuffs = stuffManager.getStuffs();
		Point loc = bomb.getCenter();
		
		int nowPlayerBomb = ownerPlayer.getBomb();
		if (nowPlayerBomb < ownerPlayer.getBombLimit()) {
			ownerPlayer.setBomb(nowPlayerBomb + 1);
		}
		
		// 炸弹爆炸延伸,清楚pick和break,记录下一轮需要引发的炸弹
		int bombPower = ownerPlayer.getBombPower();
		Bomb[] bombChain = new Bomb[4];
		int bombChainCount = 0;
		// 移除自身
		stuffs[loc.x][loc.y].setBomb(null);
		for (int dir = 0;dir < 4;++dir) {
			int dx = DIRECTION_DX[dir];
			int dy = DIRECTION_DY[dir];
			for (int i = 0; i <= bombPower;++i) {
				int next_x = loc.x + dx * i;
				int next_y = loc.y + dy * i;
				if (next_x < 0 || next_x >= StuffManager.GRID_COUNT_X || next_y < 0 || next_y >= StuffManager.GRID_COUNT_Y)
					break;
				Grid grid = stuffs[next_x][next_y];
				// 清除pick和break
				if (grid.getUnbreakableStuff() != null)
					break;
				if (grid.getPickableStuff() != null)
					grid.setPickableStuff(null);
				if (grid.getBreakableStuff() != null) {
					grid.getBreakableStuff().setBreakStuffState(BreakableStuff.BRAEKSTUFF_DONE);
					break;
				}
				// 炸死人
				Player[] players = grid.getPlayers();
				for (int j = 0;j < StuffManager.CHARACTER_COUNT;++j) {
					if (players[j] != null && players[j].getState() != Player.STATE_DEAD) {
						players[j].setDeading(true);
						players[j].setDeadingTime(0);
					}
				}
				// 炸弹连锁
				Bomb gridBomb = grid.getBomb();
				if (gridBomb != null && gridBomb.getBombState() == BOMB_STAND) {
					bombChain[bombChainCount++] = gridBomb;
				}
				// 生成水花
				Bomb newBomb = new Bomb(bomb.getOwnerID());
				newBomb.setBombState(BOMB_EXPLODE);
				newBomb.setNewSplash(true);
				newBomb.setCenter(new Point(next_x, next_y));
				grid.setBomb(newBomb);
			}
		}
		// 引爆链式
		for (int i = 0;i < bombChainCount;++i) {
			triggerExplosion(bombChain[i]);
		}
	}

	public boolean isNewSplash() {
		return isNewSplash;
	}

	public void setNewSplash(boolean isNewSplash) {
		this.isNewSplash = isNewSplash;
	}

	/**
	 * @return  return BOMB_xxxxxx
	 */
	public int update(double seconds) {
		throw new UnsupportedOperationException();
	}

	@Override()
	public int getStuffType() {
		throw new UnsupportedOperationException();
	}
}