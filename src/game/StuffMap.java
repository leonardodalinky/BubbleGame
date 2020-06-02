package game;

import java.awt.Point;
import java.util.Date;
import java.util.Random;

import scene.Scene;

/**
 * 生成stuffs的地图,不包括人
 * @author Link
 *
 */
public class StuffMap {
	private static final int N = 0;	// none
	private static final int B = 1;	// breakable
	private static final int U = 2;	// unbreakable
	private static final int P = 3;	// pickable
	
	private static Random random = new Random((new Date()).getTime());
	
	private static final int[][] EASY_MAP = new int[][] {
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{B, B, B, B, B, B, B, B, B, B, B, B, B},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{B, B, B, B, B, B, U, B, B, B, B, B, B},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{B, B, B, B, B, B, B, B, B, B, B, B, B},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N},
		{N, N, N, B, N, N, B, N, N, B, N, N, N}
	};
	
	private static final int[][] NORMAL_MAP = new int[][] {
		{N, B, N, U, B, N, B, U, N, N, B, U, N},
		{N, B, N, N, B, B, N, B, N, B, B, N, N},
		{N, B, U, N, N, B, U, B, N, N, U, B, N},
		{N, B, N, B, N, B, N, B, N, B, B, B, N},
		{B, U, N, B, B, U, B, B, B, U, B, B, B},
		{N, N, B, N, N, B, N, N, N, N, N, B, B},
		{U, N, B, N, U, B, B, B, U, N, N, N, U},
		{N, N, B, N, B, B, P, N, N, B, N, N, N},
		{B, B, N, U, B, B, N, U, N, B, N, U, N},
		{N, N, N, N, B, N, N, N, N, B, N, N, N},
		{B, B, U, B, B, B, U, B, B, B, U, B, B},
		{N, N, B, B, B, N, N, B, N, N, B, N, N},
		{N, U, B, N, B, U, N, B, N, U, B, N, N},
		{N, N, B, N, B, N, N, N, N, N, B, B, N},
		{N, N, B, N, U, N, B, N, U, N, B, N, N}
	};
	
	private static final int[][] HARD_MAP = new int[][] {	
		{N, N, B, N, B, N, B, N, B, N, B, N, N},
		{N, B, N, U, N, B, N, B, N, U, N, B, N},
		{B, N, B, N, B, N, B, N, B, N, B, N, B},
		{N, B, N, U, N, U, N, U, N, U, N, B, N},
		{B, N, B, N, B, N, B, N, B, N, B, N, B},
		{N, U, N, U, N, B, N, B, N, U, N, U, N},
		{B, N, B, N, B, N, B, N, B, N, B, N, B},
		{N, B, N, U, N, B, N, B, N, U, N, B, N},
		{B, N, B, N, B, N, B, N, B, N, B, N, B},
		{N, U, N, U, N, U, N, U, N, U, N, U, N},
		{B, N, B, N, B, N, B, N, B, N, B, N, B},
		{N, B, N, U, N, U, N, U, N, U, N, B, N},
		{B, N, B, N, B, N, B, N, B, N, B, N, B},
		{N, B, N, U, N, B, N, B, N, U, N, B, N},
		{N, N, B, N, B, N, B, N, B, N, B, N, N}
	};
	
	public static void createNewStuffMap(Grid[][] stuffs, int width, int height, int mode) {
		// 根据难度,设置选用的地图
		int[][] map = null;
		switch (mode) {
		case Scene.MODE_EASY:
			map = EASY_MAP;
			break;
		case Scene.MODE_NORMAL:
			map = NORMAL_MAP;
			break;
		case Scene.MODE_HARD:
			map = HARD_MAP;
			break;
		default:
			break;
		}
		
		for (int i = 0;i < width;++i) {
			for (int j = 0;j < height;++j) {
				Grid grid = stuffs[i][j];
				grid.setBomb(null);
				grid.clearPlayers();
				switch (map[i][j]) {
				case B:
				{
					BreakableStuff t = new BreakableStuff();
					t.setCenter(new Point(i, j));
					grid.setBreakableStuff(t);
					grid.setUnbreakableStuff(null);
					grid.setPickableStuff(null);
					break;
				}
				case U:
				{
					UnbreakableStuff t = new UnbreakableStuff();
					t.setCenter(new Point(i, j));
					grid.setBreakableStuff(null);
					grid.setUnbreakableStuff(t);
					grid.setPickableStuff(null);
					break;
				}
				case P:
				{
					// 随机生成道具;
					PickableStuff t = null;
					int r = random.nextInt(100);
					if (r >= 95)
						t = new PickableStuff(PickableStuff.PICKSTUFF_POTION);
					else if (r >= 80)
						t = new PickableStuff(PickableStuff.PICKSTUFF_BOMB);
					else if (r >= 65)
						t = new PickableStuff(PickableStuff.PICKSTUFF_POWER);
					else {
						break;
					}
					t.setCenter(new Point(i, j));
					grid.setBreakableStuff(null);
					grid.setUnbreakableStuff(null);
					grid.setPickableStuff(t);
					break;
				}
				default:
					break;
				}
			}
		}
	}
}
