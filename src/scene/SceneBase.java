package scene;

public abstract class SceneBase {
	
	public static final int STATE_NORMAL = 1;
	public static final int STATE_DONE = 2;
	public static final int STATE_ERROR = 3;
	public static final int STATE_STOP = 4;
	
	private int state = STATE_NORMAL;

	public final static int STATE_UPDATING = 3;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}