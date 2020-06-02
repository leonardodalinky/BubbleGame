package game;

import java.awt.Image;
import java.awt.Point;
import java.util.Date;
import java.util.Random;
import java.util.*;
import java.awt.*;

public class BreakableStuff extends Stuff {
	public static final int BREAKSTUFF_NORMAL = 1;
	public static final int BRAEKSTUFF_DONE = 2;
	public static Random random = new Random((new Date()).getTime());
	private int breakStuffState = BREAKSTUFF_NORMAL;

	public BreakableStuff() {
		super(Stuff.STUFF_BREAK);
	}

	@Override
	public Image getImage() {
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		return rsc.getResource("item_break");
	}

	@Override
	public void update(int milliseconds) {
		StuffManager stuffManager = SceneManager.getNowScene().getStuffManager();
		Grid[][] stuffs = stuffManager.getStuffs();
		Point loc = getCenter();
		if (breakStuffState == BRAEKSTUFF_DONE) {
			PickableStuff t = null;
			int r = random.nextInt(100);
			if (r >= 95)
				t = new PickableStuff(PickableStuff.PICKSTUFF_POTION);
			else if (r >= 80)
				t = new PickableStuff(PickableStuff.PICKSTUFF_BOMB);
			else if (r >= 65)
				t = new PickableStuff(PickableStuff.PICKSTUFF_POWER);
			if (t != null) 
				t.setCenter(loc);
			stuffs[loc.x][loc.y].setBreakableStuff(null);
			stuffs[loc.x][loc.y].setPickableStuff(t);
		}
		
	}

	public int getBreakStuffState() {
		return breakStuffState;
	}

	public void setBreakStuffState(int breakStuffState) {
		this.breakStuffState = breakStuffState;
	}

	@Override()
	public int getStuffType() {
		throw new UnsupportedOperationException();
	}
	
	
}