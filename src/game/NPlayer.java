package game;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayDeque;

import scene.Scene;
import java.awt.*;

public class NPlayer extends Player {

	public NPlayer(int id) {
		super(Player.TYPE_NONPLAYER, id);
	}

	public static final int STRATEGY_RIGHT = 1;
	public static final int STRATEGY_LEFT = 2;
	public static final int STRATEGY_UP = 3;
	public static final int STRATEGY_NONE = 5;
	public static final int STRATEGY_DOWN = 4;
	public static final int STRATEGY_BOMB = 6;
	public static final int STRATEGY_ITEM = 7;
	
	// 不可修改,下面策略将其作为硬编码
	private static final int[] DIRECTION_DX = new int[]{0, 0, -1, 1};
	private static final int[] DIRECTION_DY = new int[]{-1, 1, 0, 0};
	
	private static final int WEIGHT_POTION = 100;
	private static final int WEIGHT_BOMB = 90;
	private static final int WEIGHT_POWER = 85;
	private static final int WEIGHT_BREAKABLE = 15;
	private static final int WEIGHT_CHARACTER = 20;
	private static final int WEIGHT_EXPLOSION = -150;

	/**
	 * ai算法策略核心
	 * @return
	 */
	public int calStrategy() {
		// 进站立时进行决策
		int state = getState();
		if (state != STATE_STAND)
			return STRATEGY_NONE;
		
		int[] board = new int[StuffManager.GRID_COUNT_X * StuffManager.GRID_COUNT_Y];
		int[] boardStep = new int[StuffManager.GRID_COUNT_X * StuffManager.GRID_COUNT_Y];	// 记录每一个格子的祖先位置
		Point loc = getCenter();
		
		// 嗑药为第一件大事
		if (isDeading() && getDeadingTime() > 2000) {
			return STRATEGY_ITEM;
		}
		
		// 连通支评估
		StuffManager stuffManager = SceneManager.getNowScene().getStuffManager();
		Grid[][] stuffs = stuffManager.getStuffs();
		ArrayDeque<Point> q = new ArrayDeque<Point>(StuffManager.GRID_COUNT_X * StuffManager.GRID_COUNT_Y);	// 队列进行bfs
		q.addFirst(loc);
		while (!q.isEmpty()) {
			int size = q.size();
			for (int c = 0;c < size;++c) {
				Point p = q.removeLast();
				int pIndex = calIndex(p);
				// 已访问节点
				if (board[pIndex] != 0)
					continue;
				
				// 先对自己评价
				board[pIndex] = 1;
				Grid pGrid = stuffs[p.x][p.y];
				Bomb b = pGrid.getBomb();
				if (pGrid.getUnbreakableStuff() != null || pGrid.getBreakableStuff() != null ||
						(b != null && b.getBombState() == Bomb.BOMB_STAND && (loc.x != b.getCenter().x || loc.y != b.getCenter().y))) {
					// 不可穿过的物体,权值恒为1,且不继续bfs
					continue;
				}
				if (pGrid.getPickableStuff() != null) {
					switch (pGrid.getPickableStuff().getPickType()) {
					case PickableStuff.PICKSTUFF_POTION:
						board[pIndex] += WEIGHT_POTION;
						break;
					case PickableStuff.PICKSTUFF_BOMB:
						board[pIndex] += WEIGHT_BOMB;
						break;
					case PickableStuff.PICKSTUFF_POWER:
						board[pIndex] += WEIGHT_POWER;
						break;
					default:
						break;
					}
				}
				
				// 再叠加周围物品的评价
				for (int i = 0;i < 4;++i) {
					int new_x = p.x + DIRECTION_DX[i];
					int new_y = p.y + DIRECTION_DY[i];
					if (new_x < 0 || new_x >= StuffManager.GRID_COUNT_X ||
							new_y < 0 || new_y >= StuffManager.GRID_COUNT_Y)
						continue;
					int new_pIndex = calIndex(new_x, new_y);
					// 先不考虑炸弹
					Grid new_pGrid = stuffs[new_x][new_y];
					if (new_pGrid.getUnbreakableStuff() != null) {
						// 不可穿过的物体,权值恒为1,且不继续bfs
						continue;
					}
					else if (new_pGrid.getBreakableStuff() != null) {
						board[pIndex] += WEIGHT_BREAKABLE;
					}
					else if (new_pGrid.getPickableStuff() != null) {
						switch (new_pGrid.getPickableStuff().getPickType()) {
						case PickableStuff.PICKSTUFF_POTION:
							board[pIndex] += WEIGHT_POTION / 4;
							break;
						case PickableStuff.PICKSTUFF_BOMB:
							board[pIndex] += WEIGHT_BOMB / 4;
							break;
						case PickableStuff.PICKSTUFF_POWER:
							board[pIndex] += WEIGHT_POWER / 4;
							break;
						default:
							break;
						}
					}
					
					// 将新的位置放入队列
					if (boardStep[new_pIndex] == 0) {
						q.push(new Point(new_x, new_y));
						switch (i) {
						case 0:
							boardStep[new_pIndex] = DIRECTTION_DOWN;
							break;
						case 1:
							boardStep[new_pIndex] = DIRECTTION_UP;
							break;
						case 2:
							boardStep[new_pIndex] = DIRECTTION_RIGHT;
							break;
						case 3:
							boardStep[new_pIndex] = DIRECTTION_LEFT;
							break;
						}
						
					}
				}
				
			}
		}
		
		// 增加人物的评分
		Player[] players = stuffManager.getPlayers();
		for (int i = 0;i < StuffManager.CHARACTER_COUNT;++i) {
			if (players[i] == this || players[i].getState() == STATE_DEAD)
				continue;
			board[calIndex(players[i].getCenter())] += WEIGHT_CHARACTER;
		}
		
		// 炸弹权值调整
		for (int i = 0;i < StuffManager.GRID_COUNT_X;++i) {
			for (int j = 0;j < StuffManager.GRID_COUNT_Y;++j) {
				Bomb b = stuffs[i][j].getBomb();
				if (b == null || b.getBombState() != Bomb.BOMB_STAND) {
					continue;
				}
				
				Point bombCenter = b.getCenter();
				board[calIndex(bombCenter)] += WEIGHT_EXPLOSION;
				int power = players[b.getOwnerID()].getBombPower();
				for (int d = 0;d < 4;++d) {
					int dx = DIRECTION_DX[d];
					int dy = DIRECTION_DY[d];
					for (int dis = 1;dis <= power;++dis) {
						int newx = bombCenter.x + dx * dis;
						int newy = bombCenter.y + dy * dis;
						if (newx < 0 || newx >= StuffManager.GRID_COUNT_X || newy < 0 || newy >= StuffManager.GRID_COUNT_Y)
							continue;
						// 拒绝障碍物
						if (stuffs[newx][newy].getBreakableStuff() != null || stuffs[newx][newy].getBreakableStuff() != null ||
						(stuffs[newx][newy].getBomb() != null && stuffs[newx][newy].getBomb().getBombState() == Bomb.BOMB_STAND)) {
							break;
						}
						board[calIndex(newx, newy)] += WEIGHT_EXPLOSION;
					}
				}
			}
		}
		
		// 选择最高分点
		int maxScore = board[calIndex(loc)];
		int target_x = loc.x;
		int target_y = loc.y;
		for (int i = 0;i < StuffManager.GRID_COUNT_X;++i) {
			for (int j = 0;j < StuffManager.GRID_COUNT_Y;++j) {
				int index = calIndex(i, j);
				if (boardStep[index] != 0 && maxScore < board[index]) {
					target_x = i;
					target_y = j;
					maxScore = board[index];
				}
			}
		}
		
		int now_x = target_x;
		int now_y = target_y;
		int target_walkStrategy = STRATEGY_NONE;
		if (maxScore != board[calIndex(loc)]) {
			// 追寻回最近路线
			int prevStrategy = boardStep[calIndex(target_x, target_y)];

			while (true){
				int prev_x = now_x, prev_y = now_y;
				prevStrategy = boardStep[calIndex(prev_x, prev_y)];
				switch (prevStrategy) {
				case DIRECTTION_UP:
					prev_y -= 1;
					break;
				case DIRECTTION_DOWN:
					prev_y += 1;
					break;
				case DIRECTTION_LEFT:
					prev_x -= 1;
					break;
				case DIRECTTION_RIGHT:
					prev_x += 1;
					break;
				default:
					break;
				}
				
				if (prev_x == loc.x && prev_y == loc.y) {
					target_walkStrategy = ((prevStrategy - 1) ^ 1) + 1;
					break;
				}
				else {
					now_x = prev_x;
					now_y = prev_y;
				}
			}
		}
		else if (maxScore >= 15) {
			return STRATEGY_BOMB;
		}
		
		// 判断是否放炸弹
		// 安全时才去炸人
		if (board[calIndex(loc)] >= 0) {
			for (int i = 0;i < StuffManager.CHARACTER_COUNT;++i) {
				if (players[i] == this || players[i].getState() == Player.STATE_DEAD)
					continue;
				// 保证能炸的到
				Point otherLoc = players[i].getCenter();
				if (otherLoc.x == loc.x && Math.abs(otherLoc.y - loc.y) <= getBombPower()) {
					int miny = Math.min(otherLoc.y ,loc.y);
					int maxy = Math.max(otherLoc.y ,loc.y);
					int t;
					for (t = miny + 1; t <= maxy - 1;++t) {
						if (stuffs[loc.x][t].getUnbreakableStuff() != null || stuffs[loc.x][t].getBreakableStuff() != null ||
								(stuffs[loc.x][t].getBomb() != null && stuffs[loc.x][t].getBomb().getBombState() == Bomb.BOMB_STAND)) {
							break;
						}
					}
					if (t == maxy) {
						return STRATEGY_BOMB;
					}
				}
				else if (otherLoc.y == loc.y && Math.abs(otherLoc.x - loc.x) <= getBombPower()) {
					int minx = Math.min(otherLoc.x ,loc.x);
					int maxx = Math.max(otherLoc.x ,loc.x);
					int t;
					for (t = minx + 1; t <= maxx - 1;++t) {
						if (stuffs[t][loc.y].getUnbreakableStuff() != null || stuffs[t][loc.y].getBreakableStuff() != null ||
								(stuffs[t][loc.y].getBomb() != null && stuffs[t][loc.y].getBomb().getBombState() == Bomb.BOMB_STAND)) {
							break;
						}
					}
					if (t == maxx) {
						return STRATEGY_BOMB;
					}
				}
			}
		}

		return target_walkStrategy;
	}
	
	public static int calIndex(int x, int y) {
		return StuffManager.GRID_COUNT_Y * x + y;
	}
	
	public static int calIndex(Point p) {
		return StuffManager.GRID_COUNT_Y * p.x + p.y;
	}

	@Override
	public Image getImage() {
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		if (getDeadingTime() > deadTimeThreshold)
			return null;
		
		int state = getState();
		if (state == STATE_DEAD) {
			return rsc.getResource(String.format("player%d_dead", getId() + 1));
		}
		else if (isDeading()) {
			return rsc.getResource(String.format("player%d_deading", getId() + 1));
		}
		else {
			return rsc.getResource(String.format("player%d_%d", getId() + 1, getMoveDir()));
		}

	}

	@Override
	public void update(int milliseconds) {
		// TODO need to be completed
		int state = getState();
		Scene nowScene = SceneManager.getNowScene();
		StuffManager stuffManager = nowScene.getStuffManager();
		Grid[][] stuffs = stuffManager.getStuffs();
		Point p = getCenter();
		
		if (state == STATE_DEAD) {
			int t = getDeadingTime();
			t += milliseconds;
			setDeadingTime(t);
			return;
		}

		// handle deading
		if (isDeading()) {
			int t = getDeadingTime();
			t += milliseconds;
			if (t >= deadTimeThreshold) {
				setState(STATE_DEAD);
			}
			setDeadingTime(t);
		}
		else {
			Player[] players = stuffs[p.x][p.y].getPlayers();
			if (state == STATE_STAND) {
				for (int i = 0;i < StuffManager.CHARACTER_COUNT;++i) {
					if (players[i] != null && players[i] != this && players[i].isDeading()) {
						players[i].setState(STATE_DEAD);
					}
				}
			}
		}
		
		// pick item
		PickableStuff pickableStuff = stuffs[p.x][p.y].getPickableStuff();
		if (pickableStuff != null) {
			switch (pickableStuff.getPickType()) {
			case PickableStuff.PICKSTUFF_POTION:
				setItemCount(getItemCount() + 1);
				break;
			case PickableStuff.PICKSTUFF_BOMB:
			{
				int t = getBombLimit();
				if (t >= bombLimitMaximum)
					break;
				setBombLimit(t + 1);
				setBomb(getBomb() + 1);
				break;
			}
			case PickableStuff.PICKSTUFF_POWER:
			{
				int t = getBombPower();
				if (t >= bombPowerLimit)
					break;
				setBombPower(t + 1);
				break;
			}
			default:
				break;
			}
			stuffs[p.x][p.y].setPickableStuff(null);
		}
		
		
		if (state == STATE_STAND) {
			int strategy = calStrategy();
			switch (strategy) {
			case STRATEGY_RIGHT:
				if (!canMove(DIRECTTION_RIGHT)) break;
				setMoveDir(DIRECTTION_RIGHT);
				setState(STATE_MOVING);
				break;
			case STRATEGY_LEFT:
				if (!canMove(DIRECTTION_LEFT)) break;
				setMoveDir(DIRECTTION_LEFT);
				setState(STATE_MOVING);
				break;
			case STRATEGY_UP:
				if (!canMove(DIRECTTION_UP)) break;
				setMoveDir(DIRECTTION_UP);
				setState(STATE_MOVING);
				break;
			case STRATEGY_DOWN:
				if (!canMove(DIRECTTION_DOWN)) break;
				setMoveDir(DIRECTTION_DOWN);
				setState(STATE_MOVING);
				break;
			case STRATEGY_BOMB:
				if (isDeading())
					break;
				layBomb();
				break;
			case STRATEGY_ITEM:
				useItem();
				break;
			case STRATEGY_NONE:
			default:
				break;
			}
		}
		else if (state == STATE_MOVING) {
			int t = getMovingTime();
			if (isDeading()) {
				t += milliseconds / 4;
			}
			else {
				t += milliseconds;
			}
			setMovingTime(t);
			if (t >= movingTimeThreshold) {
				// 更新位置
				int dir = getMoveDir();
				int x = p.x;
				int y = p.y;
				switch (dir) {
				case MOVINGDIR_UP:
					y -= 1;
					break;
				case MOVINGDIR_DOWN:
					y += 1;
					break;
				case MOVINGDIR_LEFT:
					x -= 1;
					break;
				case MOVINGDIR_RIGHT:
					x += 1;
					break;
				default:
					break;
				}
				
				this.setCenter(new Point(x, y));
				stuffs[p.x][p.y].setPlayer(getId(), null);
				stuffs[x][y].setPlayer(getId(), this);
				
				setState(STATE_STAND);
				setMovingTime(0);
			}
		}
	}

	@Override()
	public int getType() {
		throw new UnsupportedOperationException();
	}

}