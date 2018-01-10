package game.input;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter {
	
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.A) {
			InputState.setPressed(InputState.A); }
		else if(keycode == Keys.D)
			InputState.setPressed(InputState.D);
		else if(keycode == Keys.O)
			InputState.setPressed(InputState.O);
		else if(keycode == Keys.P)
			InputState.setPressed(InputState.P);
		else if(keycode == Keys.SPACE)
			InputState.setPressed(InputState.SPACE);
		
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		
		if(keycode == Keys.A)
			InputState.abortPress(InputState.A);
		else if(keycode == Keys.D)
			InputState.abortPress(InputState.D);
		else if(keycode == Keys.O)
			InputState.abortPress(InputState.O);
		else if(keycode == Keys.P)
			InputState.abortPress(InputState.P);
		else if(keycode == Keys.SPACE)
			InputState.abortPress(InputState.SPACE);
		
		return false;
	}
	
}
