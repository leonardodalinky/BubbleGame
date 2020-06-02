package itf;

public interface IEventSetter {
	public static final int KEY_RIGHT = 1;
	public static final int KEY_LEFT = 2;
	public static final int KEY_UP = 4;
	public static final int KEY_DOWN = 8;
	public static final int KEY_SPACE = 16;
	public static final int MOUSE_NOCLICK = -1;
	
	public void setKeyPressed(int keyNo);
	public void setKeyReleased(int keyNo);
	public void setMouseClickEvent(int x, int y);
}
