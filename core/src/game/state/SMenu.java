package game.state;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import game.Main;

public class SMenu extends GameState {
	
	private BitmapFont writter = new BitmapFont();
	private OrthographicCamera camera;
	
	public SMenu(Main game) {
		super(game);
		camera = game.camera;
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		writter.draw(batch, "MENU STATE", 100, 100);
		batch.end();
	}

}
