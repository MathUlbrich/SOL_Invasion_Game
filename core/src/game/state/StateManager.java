package game.state;


import java.util.Stack;

import game.Main;

public class StateManager {

	public enum State {MENU, PLAY, GAME_OVER};
	private Main game;
	private Stack<GameState> states = new Stack<GameState>();
	
	public StateManager(Main game) {
		this.game = game;
	}

	public void pushState(State state) {
		if(state == State.PLAY)
			states.push(new SPlay(game));
		else if(state == State.MENU)
			states.push(new SMenu(game));
		else if(state == State.GAME_OVER)
			states.push(new SOver(game));
	}

	public void backState() {
		states.pop();
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
