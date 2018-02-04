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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
	private Sprite bg;
	private final Stage stage;
	private Sprite[] circles;
	
	public SMenu(final Main game) {
		super(game);
		
		// CREATE CAMERA
		camera = new OrthographicCamera(Settings.V_WIDTH, Settings.V_HEIGHT);
		
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
		final TextButton startLbl = new TextButton("INTO THE VRAINS", style);
		final TextButton creditsLbl = new TextButton("CREDITS", style);
		
		startLbl.addAction(sequence(alpha(0), fadeIn(0.4f)));
		creditsLbl.addAction(sequence(alpha(0), fadeIn(0.4f)));
		
		// DEFINE THE VERSION LABEL STYLE
		LabelStyle lblStyle = new LabelStyle();
		lblStyle.font = whiteWritter;
		
		// CREATE THE VERSION LABEL
		Label version = new Label("VERSION " + VERSION, lblStyle);
		version.setPosition(450, 10);
		
		// CREATE THE CONTAINER FOR THE TEXT BUTTONS
		final Container<TextButton> startContainer = new Container<TextButton>(startLbl);
		final Container<TextButton> creditsContainer = new Container<TextButton>(creditsLbl);
		
		// DEFINE BUTTON MUTABLE FORM
		startContainer.setTransform(true);
		creditsContainer.setTransform(true);
		
		// DEFINE THE BUTTONS ORIGIN
		startContainer.setOrigin(startContainer.getWidth()/2, startContainer.getHeight()/2);
		creditsContainer.setOrigin(creditsContainer.getWidth()/2, creditsContainer.getHeight()/2);
		
		// DEFINE THE BUTTONS POSITION
		startContainer.setPosition(V_WIDTH/2, V_HEIGHT/2);
		creditsContainer.setPosition(V_WIDTH/2, V_HEIGHT/2.5f);
		
		startContainer.setScale(3);
		
		startContainer.addAction(sequence(scaleTo(1, 1, 0.8f)));
		
		// ADD EVENTS TO CONTAINERS
		startContainer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				startContainer.addAction(scaleBy(0.4f, 0.4f, 0.5f, Interpolation.circleOut));
				for(Actor a : stage.getActors())
					a.setTouchable(Touchable.disabled);
				stage.addAction(sequence(delay(0.5f), fadeOut(0.5f), run(new Runnable() {
					public void run() {
						game.manager.pushState(State.LOAD);
					}
				})));
			}
		});
		
		creditsContainer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				creditsContainer.addAction(parallel(fadeOut(0.5f), moveTo(creditsContainer.getX() + 60f, creditsContainer.getY(), 0.7f)));
				stage.addAction(sequence(delay(0.5f), fadeOut(0.5f)));
			}
		});
		
		// CREATE AND CONFIGURE THE STAGE
		stage = new Stage(new FitViewport(camera.viewportWidth, camera.viewportWidth), batch);
		//stage.addActor(table);

		stage.addActor(startContainer);
		stage.addActor(creditsContainer);
		stage.addActor(version);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void resume() {
		
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
