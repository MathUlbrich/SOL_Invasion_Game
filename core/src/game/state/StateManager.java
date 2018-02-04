package game.state;


import java.util.Stack;

import com.badlogic.gdx.utils.Timer;

import game.Main;

public class StateManager {

	public enum State {MENU, PLAY, GAME_OVER, PAUSE, LOAD};
	private Main game;
	private Stack<GameState> states = new Stack<GameState>();
	
	public StateManager(Main game) {
		this.game = game;
	}

	public boolean pushState(final State state) {

		if(state == State.PLAY) {
			SPlay play = new SPlay(game);
			states.pop();
			states.push(play);
		}
		else if(state == State.MENU)
			states.push(new SMenu(game));
		else if(state == State.GAME_OVER)
			states.push(new SOver(game));
		else if(state == State.PAUSE)
			states.push(new SPause(game));
		else if(state == State.LOAD)
			states.push(new SLoad(game));
		
		return false;
	}

	public void backState() {
		states.pop();
		states.get(states.size()-1).resume();
	}
	
	public GameState getCurrentState() {
		return states.peek();
	}
	
	public void update(float dt) {
		states.peek().update(dt);
	}
	
	public void render() {
		states.peek().render();
	}
	
	public void dispose() {
		
	}

}
