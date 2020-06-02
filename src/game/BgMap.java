package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.*;

public class BgMap {

	private Image background;

	public Image getBackground() {
		return this.background;
	}

	public static BgMap createNewMap() {
		BgMap bgMap = new BgMap();
		ResourceLoader rsc = SceneManager.getInstance().getResourceLoader();
		Image bg1 = rsc.getResource("item_bg1");
		BufferedImage bg = new BufferedImage(StuffManager.DRAWRECT.width, StuffManager.DRAWRECT.height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = bg.getGraphics();
		for (int i = 0;i < StuffManager.GRID_COUNT_X;++i) {
			int offset_x = i * StuffManager.GRID_WIDTH;
			for (int j = 0;j < StuffManager.GRID_COUNT_Y;++j) {
				g.drawImage(bg1, offset_x, j * StuffManager.GRID_HEIGHT, StuffManager.GRID_WIDTH, StuffManager.GRID_HEIGHT, null);
			}
		}
		bgMap.background = bg;
		return bgMap;
	}

}