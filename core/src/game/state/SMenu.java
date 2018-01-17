package game.state;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static game.Assets.GAMEFONT;
import static game.Assets.GAMEFONT_GRADIENT;
import static game.Assets.PROXY_CIRCLE;
import static game.Assets.PROXY_MENU;
import static game.Assets.getTextureAsset;
import static game.Settings.VERSION;
import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.Main;
import game.Settings;
import game.state.StateManager.State;

public class SMenu extends GameState {
	
	private BitmapFont writter = new BitmapFont(Gdx.files.internal(GAMEFONT_GRADIENT));
	private BitmapFont whiteWritter = new BitmapFont(Gdx.files.internal(GAMEFONT));
	private OrthographicCamera camera;
	private Table table;
	private Sprite bg;
	private final Stage stage;
	private Sprite[] circles;
	
	public SMenu(final Main game) {
		super(game);
		
		// CREATE CAMERA
		camera = new OrthographicCamera(Settings.V_WIDTH, Settings.V_HEIGHT);
		
		// CREATE AND DEFINE TABLE SETTINGS
		table = new Table();
		table.setFillParent(true);
		table.setTransform(true);
		table.setOrigin(table.getPrefWidth()/2, table.getPrefHeight()/2);
		
		// CREATE THE MENU BACKGROUND IMAGE
		bg = new Sprite(new TextureRegion(getTextureAsset(PROXY_MENU)));
		bg.setPosition(-V_WIDTH/2f,-V_HEIGHT/2f);
		
		// DEFINE THE CIRCLE'S TEXTURE
		Texture texture = getTextureAsset(PROXY_CIRCLE);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		circles = new Sprite[3];
		
		// CREATE THE CRICLE 1
		circles[0] = new Sprite(new TextureRegion(texture));
		circles[0].setOrigin(circles[0].getWidth()/2, circles[0].getHeight()/2);
		circles[0].setPosition(-215,-205);
		
		// CREATE THE CRICLE 2
		circles[1] = new Sprite(new TextureRegion(texture));
		circles[1].setSize(circles[1].getWidth()/1.5f, circles[1].getHeight()/1.5f);
		circles[1].setOrigin(circles[1].getWidth()/2, circles[1].getHeight()/2);
		circles[1].setPosition(-144,-134);
		
		// CREATE THE CRICLE 3
		circles[2] = new Sprite(new TextureRegion(texture));
		circles[2].setSize(circles[2].getWidth()/2.5f, circles[2].getHeight()/2.5f);
		circles[2].setOrigin(circles[2].getWidth()/2, circles[2].getHeight()/2);
		circles[2].setPosition(-84,-74);
		
		// SET THE GAME LABEL 'BUTTONS' SETTINGS
		TextButtonStyle style = new TextButtonStyle();
		style.font = writter;
		
		// CREATE THE TEXT BUTTONS
		final TextButton startLbl = new TextButton("START GAME", style);
		final TextButton settingsLbl = new TextButton("SETTINGS", style);
		final TextButton creditsLbl = new TextButton("CREDITS", style);
		
		startLbl.addAction(sequence(alpha(0), fadeIn(0.4f)));
		settingsLbl.addAction(sequence(alpha(0), fadeIn(0.4f)));
		creditsLbl.addAction(sequence(alpha(0), fadeIn(0.4f)));
		
		// DEFINE THE VERSION LABEL STYLE
		LabelStyle lblStyle = new LabelStyle();
		lblStyle.font = whiteWritter;
		
		// CREATE THE VERSION LABEL
		Label version = new Label("VERSION " + VERSION, lblStyle);
		version.setPosition(450, 10);
		
		// CREATE THE CONTAINER FOR THE TEXT BUTTONS
		final Container<TextButton> container1 = new Container<TextButton>(startLbl);
		final Container<TextButton> container2 = new Container<TextButton>(settingsLbl);
		final Container<TextButton> container3 = new Container<TextButton>(creditsLbl);
		
		container1.setTransform(true);
		container2.setTransform(true);
		container3.setTransform(true);
		
		container1.setOrigin(container1.getWidth()/2, container1.getHeight()/2);
		container2.setOrigin(container2.getWidth()/2, container2.getHeight()/2);
		container3.setOrigin(container3.getWidth()/2, container3.getHeight()/2);
		
		// ADD EVENTS TO CONTAINERS
		container1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//container1.addAction(parallel(fadeOut(0.5f), moveTo(container1.getX() + 60f, container1.getY(), 0.7f)));
				container1.addAction(scaleBy(0.2f, 0.2f, 0.5f, Interpolation.circleOut));
				stage.addAction(sequence(delay(0.5f), fadeOut(0.5f), run(new Runnable() {
					public void run() {
						game.manager.pushState(State.PLAY);
					}
				})));
			}
		});
		
		container2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				container2.addAction(parallel(fadeOut(0.5f), moveTo(container2.getX() + 60f, container2.getY(), 0.7f)));
				stage.addAction(sequence(delay(0.5f), fadeOut(0.5f)));
			}
		});
		
		container3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				container3.addAction(parallel(fadeOut(0.5f), moveTo(container3.getX() + 60f, container3.getY(), 0.7f)));
				stage.addAction(sequence(delay(0.5f), fadeOut(0.5f)));
			}
		});
		
		// ADD THE LABELS TO TABLE
		table.add(container1).center().padBottom(10);
		table.row();
		table.add(container2).center().padBottom(10);
		table.row();
		table.add(container3).center().padBottom(10);
		table.row();
		
		// CREATE AND CONFIGURE THE STAGE
		stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportWidth), batch);
		stage.addActor(table);
		//container1.setPosition(200, 200);
		//stage.addActor(container1);
		stage.addActor(version);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void update(float deltaTime) {
		stage.act(deltaTime);
		
		// ROTATE THE CIRCLES
		circles[0].setRotation(circles[0].getRotation()+0.2f);
		circles[1].setRotation(circles[1].getRotation()-0.2f);
		circles[2].setRotation(circles[2].getRotation()+0.2f);
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		
		// DRAW THE BACKGROUND
		batch.begin();
		bg.draw(batch);
		batch.end();
		
		// DRAW THE CIRCLES
		batch.begin();
		for(Sprite s : circles)
			s.draw(batch);
		batch.end();
		
		// DRAW THE BUTTONS
		stage.draw();
	}

}
