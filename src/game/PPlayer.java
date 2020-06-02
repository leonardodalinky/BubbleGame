package game;

import java.awt.Image;
import java.awt.Point;

import scene.Scene;
import java.awt.*;

public class PPlayer extends Player {

	public PPlayer(int id) {
		super(Player.TYPE_PLAYER, id);
	}

	@Override
	public Image getImage() {
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		int state = getState();
		if (state == STATE_DEAD) {
			return rsc.getResource("player1_deading");
		}
		else if (isDeading()) {
			return rsc.getResource("player1_deading");
		}
		else {
			return rsc.getResource(String.format("player1_%d", getMoveDir()));
		}
	}

	@Override
	public void update(int milliseconds) {
		// TODO Auto-generated method stub
		int state = getState();
		Scene nowScene = SceneManager.getNowScene();
		StuffManager stuffManager = nowScene.getStuffManager();
		Grid[][] stuffs = stuffManager.getStuffs();
		Point p = getCenter();
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
		
		// 下炸弹
		int keyState = nowScene.getKeyState();
		if ((keyState & Scene.KEY_SPACE) != 0) {
			if (!isDeading()) 
				layBomb();
			nowScene.setKeyReleased(Scene.KEY_SPACE);
		}
		
		if (state == STATE_STAND) {
			if ((keyState & Scene.KEY_UP) != 0 && canMove(DIRECTTION_UP)) {
				setMoveDir(DIRECTTION_UP);
				setState(STATE_MOVING);
			}
			else if ((keyState & Scene.KEY_DOWN) != 0 && canMove(DIRECTTION_DOWN)) {
				setMoveDir(DIRECTTION_DOWN);
				setState(STATE_MOVING);
			}
			else if ((keyState & Scene.KEY_LEFT) != 0 && canMove(DIRECTTION_LEFT)) {
				setMoveDir(DIRECTTION_LEFT);
				setState(STATE_MOVING);
			}
			else if ((keyState & Scene.KEY_RIGHT) != 0 && canMove(DIRECTTION_RIGHT)) {
				setMoveDir(DIRECTTION_RIGHT);
				setState(STATE_MOVING);
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