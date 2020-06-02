package game;

import java.awt.Image;
import java.awt.*;

public class PickableStuff extends Stuff {
	
	public static final int PICKSTUFF_POTION 	= 1;
	public static final int PICKSTUFF_BOMB 		= 2;
	public static final int PICKSTUFF_POWER 	= 3;
	private int pickType;

	public PickableStuff(int pickType) {
		super(Stuff.STUFF_PICK);
		this.setPickType(pickType);
	}

	@Override
	public Image getImage() {
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		return rsc.getResource(String.format("item_pick%d", pickType));
	}

	@Override
	public void update(int milliseconds) {
		// TODO Auto-generated method stub
		
	}

	public int getPickType() {
		return pickType;
	}

	public void setPickType(int pickType) {
		this.pickType = pickType;
	}

	@Override()
	public int getStuffType() {
		throw new UnsupportedOperationException();
	}
}