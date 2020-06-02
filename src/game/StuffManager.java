package game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

import itf.IManager;
import itf.IPainter;
import scene.Scene;
import itf.*;
import java.awt.*;

public class StuffManager implements IPainter, IManager {
	
	public static final int STATE_NORMAL = 1;
	public static final int STATE_STOP = 2;

	public static final int GRID_COUNT_X = 15;
	public static final int GRID_COUNT_Y = 13;
	public static final int GRID_WIDTH = 53;
	public static final int GRID_HEIGHT = 55;
	public static final Rectangle DRAWRECT = new Rectangle(2, 0, 800, 715);
	
	public static final int CHARACTER_COUNT = 4;
	
	// map array of stuffs
	private Grid[][] stuffs = new Grid[GRID_COUNT_X][GRID_COUNT_Y];
	// players
	private Player[] players = new Player[CHARACTER_COUNT];
	
	// background map
	private BgMap bgMap;

	
	public static int BLOCK_WIDTH = 15;
	public static int BLOCK_HEIGHT = 13;
	private Rectangle drawRect;
	private static StuffManager instance;


	public StuffManager(int stageMode) {
		init(stageMode);
	}
	
	private void init(int stageMode) {
		this.bgMap = BgMap.createNewMap();
		initItem(stageMode);
		initPlayer(CHARACTER_COUNT);
	}

	@Override
	public void update(int milliseconds) {
		// splash begin to count, remove 'done' stuff
		for (int i = 0;i < GRID_COUNT_X;++i) {
			for (int j = 0;j < GRID_COUNT_Y;++j) {
				Bomb bomb = stuffs[i][j].getBomb();
				BreakableStuff breakableStuff = stuffs[i][j].getBreakableStuff();
				if (bomb != null) {
					bomb.setNewSplash(false);
				}
				if (breakableStuff != null && breakableStuff.getBreakStuffState() == BreakableStuff.BRAEKSTUFF_DONE) {
					breakableStuff.update(milliseconds);
				}
			}
		}
		// handle bomb first
		for (int i = 0;i < GRID_COUNT_X;++i) {
			for (int j = 0;j < GRID_COUNT_Y;++j) {
				Bomb bomb = stuffs[i][j].getBomb();
				if (bomb != null) {
					if (!bomb.isNewSplash())
						bomb.update(milliseconds);
					if (bomb.getBombState() == Bomb.BOMB_DONE) {
						stuffs[i][j].setBomb(null);
					}
				}
			}
		}
		
		// TODO: if you want, add some anime on breaking or else
		
		// handle player move and decision
		for (int i = 0;i < CHARACTER_COUNT;++i) {
			Player p = players[i];
			Point prev_loc = p.getCenter();
			stuffs[prev_loc.x][prev_loc.y].setPlayer(i, null);
			p.update(milliseconds);
			stuffs[p.getCenter().x][p.getCenter().y].setPlayer(i, p);
		}
	}

	@Override
	public void paint(Graphics g) {
		Scene nowScene = SceneManager.getInstance().getAttribute();
		if (nowScene.getStageNo() != Scene.STAGE_2 || nowScene.getState() != Scene.STATE_NORMAL)
			return;
		// TODO
		// background first
		g.drawImage(bgMap.getBackground(), DRAWRECT.x, DRAWRECT.y, DRAWRECT.width, DRAWRECT.height, null);
		
		// draw stuffs
		for (int i = 0;i < GRID_COUNT_X;++i) {
			for (int j = 0;j < GRID_COUNT_Y;++j) {
				stuffs[i][j].paint(g);
			}
		}
	}
	
	/**
	 * 重置地图上的breakable、unbreakable和pickable物品
	 * @param stageMode 关卡难度
	 */
	private void initItem(int stageMode) {
		for (int i = 0;i < GRID_COUNT_X;++i) {
			for (int j = 0;j < GRID_COUNT_Y;++j) {
				stuffs[i][j] = new Grid();
			}
		}
		StuffMap.createNewStuffMap(stuffs, GRID_COUNT_X, GRID_COUNT_Y, stageMode);
		
		// TODO: 设置人物初始位置
		players[0] = new PPlayer(0);
		players[0].setCenter(new Point(0, 0));
		for (int i = 1;i < CHARACTER_COUNT;++i) {
			players[i] = new NPlayer(i);
		}
		players[1].setCenter(new Point(14, 0));
		players[2].setCenter(new Point(0, 12));
		players[3].setCenter(new Point(14, 12));
		
		Random random = new Random((new Date()).getTime());
		// 随机交换人物位置
		for (int i = CHARACTER_COUNT - 1;i >= 1;--i) {
			int ex = random.nextInt(i + 1);
			Point nowLoc = players[i].getCenter();
			Point exLoc = players[ex].getCenter();
			players[i].setCenter(exLoc);
			players[ex].setCenter(nowLoc);
		}
	}
	
	/**
	 * 重置角色
	 * @param count 角色数量
	 */
	private void initPlayer(int count) {
		
	}

	public Player[] getPlayers() {
		return players;
	}

	public BgMap getBgMap() {
		return this.bgMap;
	}

	private void setBgMap(BgMap bgMap) {
		this.bgMap = bgMap;
	}

	public Grid[][] getStuffs() {
		return stuffs;
	}

	public void setStuffs(Grid[][] stuffs) {
		this.stuffs = stuffs;
	}

	public Rectangle getDrawRect() {
		return this.drawRect;
	}

	public void setDrawRect(Rectangle value) {
		this.drawRect = value;
	}

	public static StuffManager getInstance() {
		return this.instance;
	}

	public void init(Rectangle drawRect) {
		throw new UnsupportedOperationException();
	}

	@Override()
	public void update(double seconds) {
		throw new UnsupportedOperationException();
	}

	private StuffManager() {
		throw new UnsupportedOperationException();
	}


}