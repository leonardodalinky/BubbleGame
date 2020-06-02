package game;

import java.awt.Image;
import java.awt.*;

public class UnbreakableStuff extends Stuff {

	public UnbreakableStuff() {
		super(Stuff.STUFF_UNBREAK);
	}

	@Override
	public Image getImage() {
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		return rsc.getResource("item_unbreak");
	}

	@Override
	public void update(int milliseconds) {
		// nothing
		
	}

	@Override()
	public int getStuffType() {
		throw new UnsupportedOperationException();
	}
}