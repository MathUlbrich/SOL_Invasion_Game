package game.state;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.Main;

public class SLoad extends GameState {

	private Batch batch;
	private BitmapFont writter;
	private StringBuilder text;
	
	public SLoad(Main game) {
		super(game);
		
		// CREATE THE BATCH
		batch = new SpriteBatch();
		
		// CREATE THE BITMAP FONT WRITTER
		writter = new BitmapFont();
		
		// CREATE THE STRING BUILDER
		text = new StringBuilder();
		text.append("LOADING");
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void render() {
		batch.begin();
		writter.draw(batch, text, 200f, 200f);
		batch.end();
	}
		
}
