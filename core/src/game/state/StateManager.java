package game.state;


import java.util.Stack;

import com.badlogic.gdx.utils.Timer;

import game.Main;

public class StateManager {

	public enum State {MENU, PLAY, GAME_OVER, PAUSE};
	private Main game;
	private Stack<GameState> states = new Stack<GameState>();
	
	public StateManager(Main game) {
		this.game = game;
	}

	public boolean pushState(final State state) {

		if(state == State.PLAY) {
			final SPlay play = new SPlay(game);
			Timer.schedule(new Timer.Task() {
				public void run() {
					states.pop();
					states.push(play);
					SPlay.started = true;
				}
			}, 3);
			states.push(new SLoad(game));
		}
		else if(state == State.MENU)
			states.push(new SMenu(game));
		else if(state == State.GAME_OVER)
			states.push(new SOver(game));
		else if(state == State.PAUSE)
			states.push(new SPause(game));
		
		return false;
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
