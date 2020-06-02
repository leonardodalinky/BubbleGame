package game;

import java.awt.Component;
import java.awt.Graphics;

import scene.Scene;
import scene.*;
import java.awt.*;

public class SceneManager {
	
	public static byte[] updatePaintLock = new byte[0];
	
	public static final int GAME_SOLUTION_X = 1000;
	public static final int GAME_SOLUTION_Y = 800;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_STOP = 2;

	private static SceneManager instance;
	private ResourceLoader resourceLoader;
	private Scene attribute;
	private Integer currentStage = scene.Scene.STAGE_NONE;
	private int fps = 30;
	private int interval;	// milliseconds
	private Component compo;
	private int state = STATE_STOP;
	
	private Scene scene;

	private SceneManager() {
		SFXManager.getInstance().loop();
	}

	public static SceneManager getInstance() {
		if (instance == null) {
			instance = new SceneManager();
		}
		return instance;
	}
	
	public static Scene getNowScene() {
		return instance.getAttribute();
	}

	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Scene getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Scene scene) {
		this.attribute = scene;
	}

	public int getFps() {
		return this.fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}

	public Component getCompo() {
		return this.compo;
	}

	public void setCompo(Component compo) {
		this.compo = compo;
	}
	
	public void switchStage(int stageNo, int stageMode) {
		setCurrentStage(stageNo);
		this.attribute = new Scene(stageNo, stageMode);
	}

	public void start() {
		state = STATE_RUNNING;
		while(state == STATE_RUNNING) {
			try {
				synchronized (updatePaintLock) {
					attribute.update(interval);
				}
				Thread.sleep(interval);
				synchronized (updatePaintLock) {
					getCompo().repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		state = STATE_STOP;
	}
	
	public void paint(Graphics g) {
		synchronized (updatePaintLock) {
			if (attribute != null)
				attribute.paint(g);
		}
	}
	
	public void init(Component compo, int fps) {
		this.compo = compo;
		this.fps = fps;
		this.interval = 1000 / fps;
		this.resourceLoader = new ResourceLoader();
		this.resourceLoader.init();
		switchStage(Scene.STAGE_1, Scene.MODE_NONE);
	}

	public Scene getScene() {
		return this.scene;
	}

	public void setScene(Scene value) {
		this.scene = value;
	}

	public void switchStage(int stageNo) {
		throw new UnsupportedOperationException();
	}

}