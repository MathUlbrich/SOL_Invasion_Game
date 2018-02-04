package game.state;

import static game.Assets.GAMEFONT_GRADIENT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.Main;
import game.Settings;

public class SPause extends GameState {

	private BitmapFont writter = new BitmapFont(Gdx.files.internal(GAMEFONT_GRADIENT));
	private OrthographicCamera camera;
	private Table table;
	private Stage stage;
	
	public SPause(final Main game) {
		super(game);
		
		// CREATE CAMERA
		camera = new OrthographicCamera(Settings.V_WIDTH, Settings.V_HEIGHT);
		
		// CREATE AND DEFINE TABLE SETTINGS
		table = new Table();
		table.setFillParent(true);
		
		// SET THE GAME LABEL 'BUTTONS' SETTINGS
		TextButtonStyle style = new TextButtonStyle();
		style.font = writter;
		
		TextButton startLbl = new TextButton("RESUME", style);
		TextButton settingsLbl = new TextButton("MAIN MENU", style);
		
		// ADD THE FUNCTIONS TO TEXTBUTTONS
		startLbl.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.manager.backState();
			}
		});
		
		// ADD THE LABELS TO TABLE
		table.add(startLbl).center().padBottom(10);
		table.row();
		table.add(settingsLbl).center().padBottom(10);
		table.row();
		
		// CREATE AND CONFIGURE THE STAGE
		stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportWidth), batch);
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void update(float deltaTime) {
		stage.act(deltaTime);
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		
		// DRAW THE BUTTONS
		stage.draw();
	}

}
