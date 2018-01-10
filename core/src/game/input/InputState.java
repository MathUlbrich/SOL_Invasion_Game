package game.input;


public class InputState {
	
	public static final KeyState A     = new KeyState(0);
	public static final KeyState D     = new KeyState(1);
	public static final KeyState O     = new KeyState(2);
	public static final KeyState P     = new KeyState(3);
	public static final KeyState SPACE = new KeyState(4);
	
	public static void setPressed(KeyState key) {
		key.pressed = true;
		key.down = true;
	}
	
	public static void abortPress(KeyState key) {
		key.down = false;
	}
	
	public static boolean isPressed(KeyState key) {
		return key.pressed;
	}
	
	public static boolean isDown(KeyState key) {
		return key.down;
	}
	
	public static void update() {
		A.pressed = false;
		D.pressed = false;
		O.pressed = false;
		P.pressed = false;
		SPACE.pressed = false;
	}
	
}
