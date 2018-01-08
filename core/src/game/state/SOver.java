package game.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import game.Main;

public class SOver extends GameState {

	private BitmapFont writter = new BitmapFont();
	
	public SOver(Main game) {
		super(game);
		writter.setColor(Color.BLACK);
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(game.camera.combined);
		batch.begin();
		writter.draw(batch, "GAME OVER STATE", 135, 160);
		batch.end();
	}

}
