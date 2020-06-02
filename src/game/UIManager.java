package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import itf.IManager;
import itf.IPainter;
import scene.Scene;
import itf.*;
import java.awt.*;

public class UIManager implements IPainter, IManager {
	
	private static UIManager instance;
	
	private static Rectangle SCENE1_EASY_BUTTON = new Rectangle();
	private static Rectangle SCENE1_NORMAL_BUTTON = new Rectangle();
	private static Rectangle SCENE1_HARD_BUTTON = new Rectangle();
	private static Rectangle SCENE3_RETURN_BUTTON = new Rectangle();
	private static Rectangle SCENE2_CONTINUE_BUTTON = new Rectangle();
	
	private static final int SCENE2_BLOCK_OFFSET_X = 800;
	private static final int[] SCENE2_BLOCK_OFFSET_Y = new int[] {0, 200, 400, 600};
	private static final int SCENE2_BLOCK_PROFILE_OFFSET_X = 40;
	private static final int SCENE2_BLOCK_PROFILE_OFFSET_Y = 18;
	private static final int SCENE2_BLOCK_PROFILE_WIDTH = 120;
	private static final int SCENE2_BLOCK_PROFILE_HEIGHT = 120;
	private static final int[] SCENE2_BLOCK_ITEM_OFFSET_X = new int[] {0, 70, 145}; 
	private static final int SCENE2_BLOCK_ITEM_OFFSET_Y = 150; 
	private static final int SCENE2_BLOCK_ITEM_WIDTH = 50; 
	private static final int SCENE2_BLOCK_ITEM_HEIGHT = 50;  
	private static final int SCENE2_BAR_OFFSET_Y = 715;
	private static final int SCENE2_BAR_STOP_OFFSET_X = 0;
	private static final Rectangle SCENE2_BAR_STOP_OFFSET = new Rectangle(0, SCENE2_BAR_OFFSET_Y, 85, 85);
	private static final int SCENE2_BAR_TIME_OFFSET_X = 85;
	private static final int SCENE2_BAR_ITEM_OFFSET_X = 715;
	private static final Rectangle SCENE2_BAR_ITEM_OFFSET = new Rectangle(SCENE2_BAR_ITEM_OFFSET_X, SCENE2_BAR_OFFSET_Y, 85, 85);
	
	
	private UIManager() {
		// stage1
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		Image img_easy = rsc.getResource("button_easy");
		int x = img_easy.getWidth(null);
		int y = img_easy.getHeight(null);
		UIManager.SCENE1_EASY_BUTTON.x = (SceneManager.GAME_SOLUTION_X - x) / 2;
		UIManager.SCENE1_EASY_BUTTON.y = (SceneManager.GAME_SOLUTION_Y / 2 + y) / 2;
		UIManager.SCENE1_EASY_BUTTON.width = x;
		UIManager.SCENE1_EASY_BUTTON.height = y;
		
		Image img_normal = rsc.getResource("button_normal");
		x = img_normal.getWidth(null);
		y = img_normal.getHeight(null);
		UIManager.SCENE1_NORMAL_BUTTON.x = (SceneManager.GAME_SOLUTION_X - x) / 2;
		UIManager.SCENE1_NORMAL_BUTTON.y = (SceneManager.GAME_SOLUTION_Y / 2 + 4 * y) / 2;
		UIManager.SCENE1_NORMAL_BUTTON.width = x;
		UIManager.SCENE1_NORMAL_BUTTON.height = y;
		
		Image img_hard = rsc.getResource("button_hard");
		x = img_hard.getWidth(null);
		y = img_hard.getHeight(null);
		UIManager.SCENE1_HARD_BUTTON.x = (SceneManager.GAME_SOLUTION_X - x) / 2;
		UIManager.SCENE1_HARD_BUTTON.y = (SceneManager.GAME_SOLUTION_Y / 2 + 7 * y) / 2;
		UIManager.SCENE1_HARD_BUTTON.width = x;
		UIManager.SCENE1_HARD_BUTTON.height = y;
		
		// stage3
		Image img_return = rsc.getResource("button_return");
		x = img_return.getWidth(null);
		y = img_return.getHeight(null);
		UIManager.SCENE3_RETURN_BUTTON.x = (SceneManager.GAME_SOLUTION_X - x) / 2;
		UIManager.SCENE3_RETURN_BUTTON.y = (SceneManager.GAME_SOLUTION_Y + y) / 2;
		UIManager.SCENE3_RETURN_BUTTON.width = x;
		UIManager.SCENE3_RETURN_BUTTON.height = y;
		
		// stage2
		Image img_continue = rsc.getResource("button_continue");
		x = img_continue.getWidth(null);
		y = img_continue.getHeight(null);
		UIManager.SCENE2_CONTINUE_BUTTON.x = (SceneManager.GAME_SOLUTION_X - x) / 2;
		UIManager.SCENE2_CONTINUE_BUTTON.y = (SceneManager.GAME_SOLUTION_Y + 4 * y) / 2;
		UIManager.SCENE2_CONTINUE_BUTTON.width = x;
		UIManager.SCENE2_CONTINUE_BUTTON.height = y;
		
	}
	
	public static UIManager getInstance() {
		if (instance == null)
			instance = new UIManager();
		return instance;
	}
	
	public static boolean isInRect(int x, int y, Rectangle rect) {
		if (x > rect.x && x < rect.x + rect.width && y > rect.y && y < rect.y + rect.height)
			return true;
		return false;
	}

	@Override
	public void paint(Graphics graphics) {
		switch (SceneManager.getInstance().getAttribute().getStageNo()) {
		case Scene.STAGE_1:
			paint_stage1(graphics);
			break;
		case Scene.STAGE_2:
			paint_stage2(graphics);
			break;
		case Scene.STAGE_3:
			paint_stage3(graphics);
			break;
		default:
			break;
		}
	}
	
	private void paint_stage1(Graphics g) {
		// paint stage1
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		g.drawImage(rsc.getResource("scene1_bg"), 0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y, null);
		g.drawImage(rsc.getResource("button_easy"), SCENE1_EASY_BUTTON.x, SCENE1_EASY_BUTTON.y, 
				SCENE1_EASY_BUTTON.width, SCENE1_EASY_BUTTON.height, null);
		g.drawImage(rsc.getResource("button_normal"), SCENE1_NORMAL_BUTTON.x, SCENE1_NORMAL_BUTTON.y, 
				SCENE1_NORMAL_BUTTON.width, SCENE1_NORMAL_BUTTON.height, null);
		g.drawImage(rsc.getResource("button_hard"), SCENE1_HARD_BUTTON.x, SCENE1_HARD_BUTTON.y, 
				SCENE1_HARD_BUTTON.width, SCENE1_HARD_BUTTON.height, null);
		
	}
	
	private void paint_stage2(Graphics g) {
		Scene nowScene = SceneManager.getInstance().getAttribute();
		int state = nowScene.getState();
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		Player[] players = nowScene.getStuffManager().getPlayers();
		if (state == Scene.STATE_STOP) {
			g.drawImage(rsc.getResource("scene2_stop_bg"), 0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y, null);
			g.drawImage(rsc.getResource("button_return"), SCENE3_RETURN_BUTTON.x, SCENE3_RETURN_BUTTON.y, 
					SCENE3_RETURN_BUTTON.width, SCENE3_RETURN_BUTTON.height, null);
			g.drawImage(rsc.getResource("button_continue"), SCENE2_CONTINUE_BUTTON.x, SCENE2_CONTINUE_BUTTON.y, 
					SCENE2_CONTINUE_BUTTON.width, SCENE2_CONTINUE_BUTTON.height, null);
		}
		else if (state == Scene.STATE_NORMAL) {
			g.drawImage(rsc.getResource("scene2_bg"), 0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y, null);
			
			// bar
			g.drawImage(rsc.getResource("bar_stop"), SCENE2_BAR_STOP_OFFSET_X, SCENE2_BAR_OFFSET_Y, 
					SCENE2_BAR_STOP_OFFSET.width, SCENE2_BAR_STOP_OFFSET.height, null);
			
			// time
			Font tFont = g.getFont();
			g.setFont(new Font("宋体", Font.PLAIN, 80));
			g.drawString(String.format("%d秒", nowScene.getGameTime() / 1000), 
					SCENE2_BAR_TIME_OFFSET_X, SceneManager.GAME_SOLUTION_Y - 20);
			g.setFont(tFont);
			
			// player1's item
			if (players[0].getItemCount() > 0) {
				g.drawImage(rsc.getResource("bar_item"), SCENE2_BAR_ITEM_OFFSET.x, SCENE2_BAR_ITEM_OFFSET.y, 
						SCENE2_BAR_ITEM_OFFSET.width, SCENE2_BAR_ITEM_OFFSET.height, null);
			}
			tFont = g.getFont();
			g.setFont(new Font("宋体", Font.PLAIN, 60));
			g.drawString(String.format("%dx", players[0].getItemCount()), 
					SCENE2_BAR_ITEM_OFFSET_X - 80, SceneManager.GAME_SOLUTION_Y - 30);
			g.setFont(tFont);
			
			tFont = g.getFont();
			g.setFont(new Font("宋体", Font.PLAIN, 20));
			for (int i = 0;i < StuffManager.CHARACTER_COUNT;++i) {
				// profile image
				g.drawImage(rsc.getResource(String.format("profile_player%d", i + 1)), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_PROFILE_OFFSET_X, SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_PROFILE_OFFSET_Y, 
						SCENE2_BLOCK_PROFILE_WIDTH, SCENE2_BLOCK_PROFILE_HEIGHT, null);
				// item 1: bombs
				g.drawImage(rsc.getResource("profile_bombs"), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_ITEM_OFFSET_X[0], SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_ITEM_OFFSET_Y, 
						SCENE2_BLOCK_ITEM_WIDTH, SCENE2_BLOCK_ITEM_HEIGHT, null);
				g.drawString(String.format("x%d", players[i].getBomb()), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_ITEM_OFFSET_X[0] + SCENE2_BLOCK_ITEM_WIDTH, 
						SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_ITEM_OFFSET_Y + SCENE2_BLOCK_ITEM_HEIGHT);
				// item 2: power
				g.drawImage(rsc.getResource("profile_power"), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_ITEM_OFFSET_X[1], SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_ITEM_OFFSET_Y, 
						SCENE2_BLOCK_ITEM_WIDTH, SCENE2_BLOCK_ITEM_HEIGHT, null);
				g.drawString(String.format("x%d", players[i].getBombPower()), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_ITEM_OFFSET_X[1] + SCENE2_BLOCK_ITEM_WIDTH, 
						SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_ITEM_OFFSET_Y + SCENE2_BLOCK_ITEM_HEIGHT);
				// item 3: healing potion
				g.drawImage(rsc.getResource("profile_potion"), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_ITEM_OFFSET_X[2], SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_ITEM_OFFSET_Y, 
						SCENE2_BLOCK_ITEM_WIDTH, SCENE2_BLOCK_ITEM_HEIGHT, null);
				g.drawString(String.format("x%d", players[i].getItemCount()), 
						SCENE2_BLOCK_OFFSET_X + SCENE2_BLOCK_ITEM_OFFSET_X[2] + SCENE2_BLOCK_ITEM_WIDTH - 20, 
						SCENE2_BLOCK_OFFSET_Y[i] + SCENE2_BLOCK_ITEM_OFFSET_Y + SCENE2_BLOCK_ITEM_HEIGHT);
			}
			g.setFont(tFont);
		}
	}
	
	private void paint_stage3(Graphics g) {
		// paint stage2
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		g.drawImage(rsc.getResource("scene3_bg"), 0, 0, SceneManager.GAME_SOLUTION_X, SceneManager.GAME_SOLUTION_Y, null);
		g.drawImage(rsc.getResource("button_return"), SCENE3_RETURN_BUTTON.x, SCENE3_RETURN_BUTTON.y, 
				SCENE3_RETURN_BUTTON.width, SCENE3_RETURN_BUTTON.height, null);
		
		Color c = g.getColor();
		g.setColor(Color.black);
		Font tFont = g.getFont();
		g.setFont(new Font("宋体", Font.PLAIN, 60));
		if (SceneManager.getInstance().getAttribute().getStageMode() == Scene.MODE_WIN)
			g.drawString("You Win", 400, 300);
		else
			g.drawString("You Lose", 400, 300);
		g.setColor(c);
		g.setFont(tFont);
	}

	@Override
	public void update(int milliseconds) {
		switch (SceneManager.getInstance().getAttribute().getStageNo()) {
		case Scene.STAGE_1:
			update_stage1(milliseconds);
			break;
		case Scene.STAGE_2:
			update_stage2(milliseconds);
			break;
		case Scene.STAGE_3:
			update_stage3(milliseconds);
			break;
		default:
			break;
		}

	}
	
	private void update_stage1(int milliseconds) {
		Scene nowScene = SceneManager.getInstance().getAttribute();
		if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE1_EASY_BUTTON)) {
			nowScene.clearMouseClick();
			SceneManager.getInstance().switchStage(Scene.STAGE_2, Scene.MODE_EASY);
		}
		else if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE1_NORMAL_BUTTON)) {
			nowScene.clearMouseClick();
			SceneManager.getInstance().switchStage(Scene.STAGE_2, Scene.MODE_NORMAL);
		}
		else if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE1_HARD_BUTTON)) {
			nowScene.clearMouseClick();
			SceneManager.getInstance().switchStage(Scene.STAGE_2, Scene.MODE_HARD);
		}
	}
	
	private void update_stage2(int milliseconds) {
		// 更新时间信息,判断暂停的界面
		Scene nowScene = SceneManager.getInstance().getAttribute();
		int state = nowScene.getState();
		Player[] players = nowScene.getStuffManager().getPlayers();
		if (players[0].getState() == Player.STATE_DEAD) {
			SceneManager.getInstance().switchStage(Scene.STAGE_3, Scene.MODE_LOSE);
			return;
		}
		else if (players[1].getState() == Player.STATE_DEAD && players[2].getState() == Player.STATE_DEAD &&
			players[3].getState() == Player.STATE_DEAD) {
			SceneManager.getInstance().switchStage(Scene.STAGE_3, Scene.MODE_WIN);
			return;
		}
			
		// update time
		if (state == Scene.STATE_NORMAL) {
			if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE2_BAR_STOP_OFFSET)) {
				// set stop state
				nowScene.setState(Scene.STATE_STOP);
				nowScene.clearMouseClick();
			}
			if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE2_BAR_ITEM_OFFSET)) {
				// use item
				players[0].useItem();
				nowScene.clearMouseClick();
			}
			
			nowScene.setGameTime(nowScene.getGameTime() - milliseconds);
			if (nowScene.getGameTime() <= 0) {
				SceneManager.getInstance().switchStage(Scene.STAGE_3, Scene.MODE_LOSE);
			}
		}
		else if (state == Scene.STATE_STOP) {
			if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE3_RETURN_BUTTON)) {
				// set stop state
				nowScene.setState(Scene.STATE_NORMAL);
				nowScene.clearMouseClick();
				SceneManager.getInstance().switchStage(Scene.STAGE_1, Scene.MODE_NONE);
			}
			else if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE2_CONTINUE_BUTTON)) {
				// set stop state
				nowScene.setState(Scene.STATE_NORMAL);
				nowScene.clearMouseClick();
			}
		}
	}
	
	private void update_stage3(int milliseconds) {
		Scene nowScene = SceneManager.getInstance().getAttribute();
		if (isInRect(nowScene.getMouseClick_x(), nowScene.getMouseClick_y(), SCENE3_RETURN_BUTTON)) {
			nowScene.clearMouseClick();
			SceneManager.getInstance().switchStage(Scene.STAGE_1, Scene.MODE_NONE);
		}
	}
}