package game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import game.Assets;
import game.Main;
import game.Settings;

public class SMenu extends GameState {
	
	private BitmapFont writter = new BitmapFont(Gdx.files.internal(Assets.GAMEFONT_GRADIENT));
	private OrthographicCamera camera;
	private Table table;
	
	public SMenu(Main game) {
		super(game);
		camera = new OrthographicCamera(Settings.V_WIDTH, Settings.V_HEIGHT);
		table = new Table();
		
		// SET THE GAME LABEL 'BUTTONS' SETTINGS
		TextButtonStyle style = new TextButtonStyle();
		style.font = writter;
		
		TextButton startLbl = new TextButton("START GAME", style);
		TextButton settingsLbl = new TextButton("SETTINGS", style);
		TextButton creditsLbl = new TextButton("CREDITS", style);
		
		// ADD THE LABELS TO TABLE
		table.add(startLbl).top();
		table.row();
		table.add(settingsLbl).center();
		table.row();
		table.add(creditsLbl).center();
		table.row();
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		writter.draw(batch, "START GAME", 190, 400);
//		writter.draw(batch, "SETTINGS", 210, 320);
//		writter.draw(batch, "CREDITS", 220, 240);
//		batch.end();
		
		batch.begin();
		table.draw(batch, 1);
		batch.end();
	}

}
