package game;

import java.awt.Graphics;

import itf.IPainter;

public class Grid implements IPainter{
	
	private BreakableStuff breakableStuff = null;
	private UnbreakableStuff unbreakableStuff = null;
	private PickableStuff pickableStuff = null;
	private Player[] players = new Player[StuffManager.CHARACTER_COUNT];
	private Bomb bomb = null;

	public Grid() {
		
	}
	
	@Override
	public void paint(Graphics graphics) {
		if (unbreakableStuff != null) {
			unbreakableStuff.paint(graphics);
		}
		else if (breakableStuff != null) {
			breakableStuff.paint(graphics);
		}
		else if (pickableStuff != null) {
			pickableStuff.paint(graphics);
		}
		
		if (bomb != null) {
			bomb.paint(graphics);
		}
		
		for (int i = 0;i < StuffManager.CHARACTER_COUNT;++i) {
			if (players[i] != null) {
				players[i].paint(graphics);
			}
		}
	}

	public BreakableStuff getBreakableStuff() {
		return breakableStuff;
	}

	public void setBreakableStuff(BreakableStuff breakableStuff) {
		this.breakableStuff = breakableStuff;
	}

	public UnbreakableStuff getUnbreakableStuff() {
		return unbreakableStuff;
	}

	public void setUnbreakableStuff(UnbreakableStuff unbreakableStuff) {
		this.unbreakableStuff = unbreakableStuff;
	}

	public PickableStuff getPickableStuff() {
		return pickableStuff;
	}

	public void setPickableStuff(PickableStuff pickableStuff) {
		this.pickableStuff = pickableStuff;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public void clearPlayers() {
		for (int i = 0;i < StuffManager.CHARACTER_COUNT;++i) {
			players[i] = null;
		}
	}
	
	public Player getPlayer(int i) {
		return players[i];
	}
	
	public void setPlayer(int i, Player player) {
		players[i] = player;
	}

	public Bomb getBomb() {
		return bomb;
	}

	public void setBomb(Bomb bomb) {
		this.bomb = bomb;
	}

}
