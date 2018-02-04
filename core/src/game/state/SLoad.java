package game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import game.Main;
import game.state.StateManager.State;

public class SLoad extends GameState {

	private Batch batch;
	private ProgressBar loadBar;
	private float progress;
	private BitmapFont writter;
	
	public SLoad(Main game) {
		super(game);
		
		// CREATE THE LOAD BAR SKIN
		Skin skin = new Skin(Gdx.files.internal("UploadBar/upload_bar.json"));
		
		// CREATE THE LOAD BAR OBJECT
		loadBar = new ProgressBar(0, 100, 1, false, skin);
		loadBar.setOrigin(loadBar.getWidth()/2, loadBar.getHeight()/2);
		loadBar.setPosition(50, 40);
		loadBar.getStyle().background.setMinWidth(300);
		
		// CREATE THE BATCH
		batch = new SpriteBatch();
		
		// CREATE THE BITMAP FONT
		writter = new BitmapFont();
		
		// CREATE THE TEXT TO DRAW WITH BITMAPFONT
		StringBuilder text = new StringBuilder("UPLOAD BACKGROUND");
		StringBuilder perc = new StringBuilder(((int) progress) + "%");
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void update(float deltaTime) {
		progress += deltaTime * 5;
		loadBar.setValue(progress);
		loadBar.act(deltaTime);
		
		if(loadBar.getValue() == 100) {
			// BREAK THE LOADING SCREEN AND CHANGE TO PLAY SCREEN
			
			game.manager.pushState(State.PLAY);
			
			Timer.schedule(new Timer.Task() {
				public void run() {
					SPlay.started = true;
				}
			}, 0.01f);
		}
	}

	@Override
	public void render() {
		
		// DRAW THE LOAD BAR
		batch.begin();
		loadBar.draw(batch, 1);
		batch.end();
		
		// DRAW THE PROGRESS TEXT
		//writter.draw(batch, "", x, y)
	}
		
}
