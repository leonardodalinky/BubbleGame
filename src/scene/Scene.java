package scene;

import java.awt.Graphics;

import game.SceneManager;
import game.StuffManager;
import game.UIManager;
import itf.IEventSetter;
import itf.IPainter;
import itf.*;
import game.*;
import java.awt.*;

public class Scene extends SceneBase implements IEventSetter, IPainter {
	
	public static final int STAGE_NONE = 0;
	public static final int STAGE_1 = 1;
	public static final int STAGE_2 = 2;
	public static final int STAGE_3 = 3;
	
	public static final int MODE_NONE = 0;
	public static final int MODE_EASY = 1;
	public static final int MODE_NORMAL = 2;
	public static final int MODE_HARD = 3;
	public static final int MODE_LOSE = 1;
	public static final int MODE_WIN = 2;
	
	// record which key is pressed now
	private int keyState = 0;
	private int mouseClick_x = MOUSE_NOCLICK;
	private int mouseClick_y = MOUSE_NOCLICK;

	private StuffManager stuffManager = null;
	
	private int stageNo = STAGE_NONE;
	private int stageMode = MODE_NONE;
	
	private int gameTime = 120 * 1000;
	
	private BgMap bgMap;

	public Scene(int stageNo, int stageMode) {
		this.setStageNo(stageNo);
		this.setStageMode(stageMode);
		if (stageNo == STAGE_2)
			stuffManager = new StuffManager(stageMode);
	}
	
	@Override
	public void setKeyPressed(int keyNo) {
		this.setKeyState(this.getKeyState() | keyNo);
	}
	
	@Override
	public void setKeyReleased(int keyNo) {
		this.setKeyState(this.getKeyState() & (~keyNo));
	}
	
	@Override
	public void setMouseClickEvent(int x, int y) {
		setMouseClick_x(x);
		setMouseClick_y(y);
	}

	public int update(int milliseconds) {
		int nowState = getState();
		UIManager.getInstance().update(milliseconds);
		if (stuffManager != null && nowState == STATE_NORMAL && SceneManager.getNowScene().getStageNo() == getStageNo())
			stuffManager.update(milliseconds);
		return nowState;
	}
	
	@Override
	public void paint(Graphics graphics) {
		// TODO Auto-generated method stub
		int nowState = getState();
		UIManager.getInstance().paint(graphics);
		if (stuffManager != null && nowState == STATE_NORMAL) stuffManager.paint(graphics);
	}

	public int getKeyState() {
		return keyState;
	}

	public void setKeyState(int keyState) {
		this.keyState = keyState;
	}

	public int getMouseClick_x() {
		return mouseClick_x;
	}

	public void setMouseClick_x(int mouseClick_x) {
		this.mouseClick_x = mouseClick_x;
	}

	public int getMouseClick_y() {
		return mouseClick_y;
	}

	public void setMouseClick_y(int mouseClick_y) {
		this.mouseClick_y = mouseClick_y;
	}
	
	public void clearMouseClick() {
		this.mouseClick_x = MOUSE_NOCLICK;
		this.mouseClick_y = MOUSE_NOCLICK;
	}

	public int getStageNo() {
		return stageNo;
	}

	public void setStageNo(int stageNo) {
		this.stageNo = stageNo;
	}

	public int getStageMode() {
		return stageMode;
	}

	public void setStageMode(int stageMode) {
		this.stageMode = stageMode;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	public StuffManager getStuffManager() {
		return stuffManager;
	}

	public BgMap getBgMap() {
		return this.bgMap;
	}

	public void setBgMap(BgMap value) {
		this.bgMap = value;
	}

	public Scene(int stageNo) {
		throw new UnsupportedOperationException();
	}

	public static Scene createStageScene(int stageNo) {
		throw new UnsupportedOperationException();
	}

}