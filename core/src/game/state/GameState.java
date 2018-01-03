package game.state;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;

public abstract class GameState {
	
	protected Main game;
	protected SpriteBatch batch;
	
	public GameState(Main game) {
		this.game = game;
		batch = game.batch;
	}
	
	public abstract void update(float deltaTime);
	public abstract void render();

}
