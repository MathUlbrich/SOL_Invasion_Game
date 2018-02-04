package game.state;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static game.Assets.BACKGROUND_IMAGE;
import static game.Assets.getMap;
import static game.Assets.getTextureAsset;
import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;
import static game.WorldVars.BARRAGE_MASK;
import static game.WorldVars.PLAYER_MASK;
import static game.WorldVars.PPM;
import static game.WorldVars.SECURITY_MASK;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import game.Assets;
import game.BodyCollector;
import game.ContactResolver;
import game.DataSecurityFactory;
import game.HUD;
import game.Main;
import game.Settings;
import game.input.InputHandler;
import game.input.InputState;
import game.objects.DataPiece;
import game.objects.GOManager;
import game.objects.Player;
import game.state.StateManager.State;

public class SPlay extends GameState {
	
	enum PlayState { ONGAME, PAUSE, BEGIN, CATCHSTORM }
	
	private OrthographicCamera camera;
	private Box2DDebugRenderer debug;
	private ContactResolver resolver;
	private Sprite[] bg;
	private World world;
	public static boolean isOver = false;
	private Player player;
	private BodyCollector collector;
	private HUD hud;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private PlayState currentState;
	private InputHandler inputHandler;
	
	public static float stageProgress;
	public static int score;
	public static boolean started;
	
	// SETTINGS OF PAUSE MENU
	private Stage pauseStage;
	private TextButton resumeBtn;
	private TextButton menuBtn;
	
	// SETTINGS OF BEGIN
	private final Stage startStage;
	
	public SPlay(final Main game) {
		super(game);
		
		// DEFINE THE OBJECTS USED ON THIS CLASS
		camera = new OrthographicCamera();
		debug = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);
		resolver = new ContactResolver(game);
		player = new Player(world, new Vector2(V_WIDTH/2f, 100f));
		collector = new BodyCollector();
		hud = new HUD(player);
		score = 0;
		
		// CREATE THE TILED MAP WITH OBJECTS POSITIONS
		map = getMap();
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1f/PPM); //SCALE MAP TO PPM
		
		// SET MAP RENDERER CAMERA SETTINGS
		mapRenderer.setView(camera);
		mapRenderer.getViewBounds().x = -200f;
		
		// CREATE THE BACKGROUND SPRITES
		bg = new Sprite[17];
		for(int i = 0; i < bg.length; i++) {
			bg[i] = new Sprite(getTextureAsset(BACKGROUND_IMAGE));
			bg[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			bg[i].setSize(bg[i].getWidth() / (PPM * 1f), bg[i].getHeight() / (PPM * 1f));
			bg[i].setPosition(0, bg[i].getHeight() * i);
		}		
		
		float[] blocksX = new float[] {
				0, 
				V_WIDTH/PPM, 
				0, 
				0, 
				0, 
				V_WIDTH/PPM, 
				V_WIDTH/PPM, 
				V_WIDTH/PPM
		};
		float[] blocksY = new float[] {
				20f/PPM, 
				20f/PPM, 
				20f/PPM, 
				(V_HEIGHT - 170)/PPM, 
				(V_HEIGHT - 170)/PPM, 
				(V_HEIGHT - 170)/PPM, 
				(V_HEIGHT - 170)/PPM, 
				20f/PPM
		};
		
		// CREATE THE NON-PASS BLOCKS
		for(int i = 0; i < 8; i+=2) {
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.KinematicBody;
			bdef.position.x = 0;
			bdef.position.y = 0;
			bdef.linearVelocity.x = 0;
			bdef.linearVelocity.y = 1.8f;
			
			ChainShape shape = new ChainShape();
			shape.createChain(new Vector2[] { new Vector2(blocksX[i], blocksY[i]), new Vector2(blocksX[i+1], blocksY[i+1])});
			
			short mask = PLAYER_MASK | SECURITY_MASK;
			
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.filter.categoryBits = BARRAGE_MASK;
			fdef.filter.maskBits = mask;
			
			Body body = world.createBody(bdef);
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("barrage");
		}
		
		// CREATE THE SECURITIES
		for(int i = 0; i < 34; i++) {
			
			MapObject mapObj = map.getLayers().get("securities easy").getObjects().get("s" + i);
			
			float x = (Float) mapObj.getProperties().get("x");
			float y = (Float) mapObj.getProperties().get("y");
			
			if(new Random().nextBoolean())
				DataSecurityFactory.createGreenDataSecurity(world, new Vector2(x, y));
			else
				DataSecurityFactory.createPurpleDataSecurity(world, new Vector2(x, y));
		}
		
		// CREATE THE DATA STORMS POSSIBILITIES
		for(int i = 0; i < map.getLayers().get("storm easy").getObjects().getCount(); i++) {
			MapObject mapObj = map.getLayers().get("storm easy").getObjects().get("storm" + i);
			
			float x = (Float) mapObj.getProperties().get("x");
			float y = (Float) mapObj.getProperties().get("y");
			
			//GOManager.instance.addGameObject(new Storm(world, new Vector2(x, y)));
		}
		
		// SET THE CAMERA SETTINGS
		camera.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT /PPM );
		
		camera.position.x = 0;
		
		// SET THE CONTACTLISTENER TO CONTACTRESOLVER
		world.setContactListener(resolver);
		
		// SET NEW INPUT CONTROLLER
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		
		// ADD THE DATA PIECE
		for(int i = 0; i < 4; i++) {
			MapObject mapObj = map.getLayers().get("datas easy").getObjects().get("d" + i);
			
			float x = (Float) mapObj.getProperties().get("x");
			float y = (Float) mapObj.getProperties().get("y");
			
			GOManager.instance.addGameObject(new DataPiece(world, new Vector2(x, y)));
		}
		
		// TRANSLATE THE CAMERA TO MAP POSITION
		camera.translate(V_WIDTH/(2f * PPM), 0);
		
		// CREATE THE PAUSE BUTTONS
		BitmapFont font = new BitmapFont(Gdx.files.internal(Assets.PAUSE_FONT));
		TextButtonStyle style = new TextButtonStyle();
		style.font = font;
		
		resumeBtn = new TextButton("RESUME", style);
		menuBtn = new TextButton("MAIN MENU", style);
		
		Container<TextButton> resumeContainer = new Container<TextButton>(resumeBtn);
		Container<TextButton> menuContainer = new Container<TextButton>(menuBtn);
		
		resumeContainer.setTransform(true);
		menuContainer.setTransform(true);
		
		resumeContainer.setPosition(V_WIDTH/2f, V_HEIGHT/1.8f);
		menuContainer.setPosition(V_WIDTH/2f, V_HEIGHT/2.2f);
		
		resumeContainer.setScale(0.6f);
		menuContainer.setScale(0.6f);
		
		resumeContainer.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				currentState = PlayState.ONGAME;
				resume();
			}
		});
		
		menuContainer.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.manager.pushState(State.MENU);
			}
		});
		
		pauseStage = new Stage(new FitViewport(V_WIDTH, V_HEIGHT), batch);
		
		pauseStage.addActor(resumeContainer);
		pauseStage.addActor(menuContainer);
		
		// SET THE START STATE
		currentState = PlayState.BEGIN;
		
		// CREATE THE START STAGE
		Texture circleProxy = getTextureAsset(Assets.PROXY_CIRCLE);
		circleProxy.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Image circle = new Image(new TextureRegionDrawable(new TextureRegion(circleProxy)));
		circle.setOrigin(circleProxy.getWidth()/2, circleProxy.getHeight()/2);
		circle.setPosition(80, 190);
		circle.setScale(0.6f);
		circle.addAction(sequence(alpha(0), delay(1.5f), parallel(fadeIn(0.3f), rotateBy(60, 4), sequence(delay(2.3f), fadeOut(0.4f)))));
		
		Texture tx = getTextureAsset(Assets.WHITE_START_IMAGE);
		tx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		final Image image = new Image(new TextureRegionDrawable(new TextureRegion(tx)));
		image.setOrigin(tx.getWidth()/2, tx.getHeight()/2);
		image.setPosition(80, V_HEIGHT/2f);
		image.setScale(2.5f);
		
		image.addAction(
				sequence(
						alpha(0),
						delay(1.5f),
						parallel(fadeIn(0.4f), scaleTo(1, 1, 0.4f)),
						run(new Runnable() {
							public void run() {
								Texture newTex = getTextureAsset(Assets.START_IMAGE);
								newTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
								
								((TextureRegionDrawable)image.getDrawable()).setRegion(new TextureRegion(newTex));
							}
						}),
						delay(1.8f),
						parallel(
								scaleBy(0.3f, 0.3f, 0.4f, Interpolation.circle),
								fadeOut(0.4f)),
						run(new Runnable() {
							public void run() {
								currentState = PlayState.ONGAME;
								HUD.started = true;
							}
						})
				)
		);
		
		startStage = new Stage(new FitViewport(V_WIDTH, V_HEIGHT), batch);
		
		startStage.addActor(circle);
		startStage.addActor(image);
	}
	
	@Override
	public void resume() {
		// SET NEW INPUT CONTROLLER
		Gdx.input.setInputProcessor(inputHandler);
	}
	
	@Override
	public void update(float deltaTime) {
		if(!started) return;
		batch.setProjectionMatrix(camera.combined);
		
		// DEFINE THE PAUSE STATE OF GAME
		if(InputState.isPressed(InputState.SPACE)) {
			currentState = PlayState.PAUSE;
			InputState.abortAll();
			Gdx.input.setInputProcessor(pauseStage);
		}
		
		if(currentState == PlayState.ONGAME || currentState == PlayState.BEGIN) {
			// UPDATE THE CAMERA AND MOVE IT TO ABOVE
			camera.update();
			camera.position.y += 0.03f;
			stageProgress = (camera.position.y * PPM) + V_HEIGHT;
			
			// MAKE BOX2D WORLD ROUND
			world.step(deltaTime, 6, 2);
			
			// UPDATE THE PLAYER
			player.update(deltaTime);
			
			// UPDATE ALL OTHER GAME OBJECTS
			GOManager.instance.update(deltaTime);
			
			// UPDATE THE HUD
			hud.update(deltaTime);
			
			// COLLECT ALL BODIES MARKED TO BE REMOVED BY CONTACTRESOLVER CLASS
			collector.collectBodiesToRemove();
			
			// UPDATE THE INPUT TO NOT BREAK WITH PRESSED STATE CODE
			InputState.update();
			
			if(currentState == PlayState.ONGAME) {
				Gdx.input.setInputProcessor(inputHandler);
			}
			
			if(currentState == PlayState.BEGIN) {
				startStage.act();
				Gdx.input.setInputProcessor(null);
			}
		}
		
		else if(currentState == PlayState.PAUSE) {
			pauseStage.act();
			InputState.update();
		}
		
	}

	@Override
	public void render() {
		if(!started) return;
		
		batch.setProjectionMatrix(camera.combined);
		
		// DRAW THE BACKGROUND IMAGE
		for(Sprite s : bg) {
			batch.begin();
			s.draw(batch);
			batch.end();
		}
		
		// MAKE BACKGROUND MORE DARKER
		Sprite sp = new Sprite(getTextureAsset(Assets.DARKER_EFFECT));
		sp.setAlpha(0.2f);
		batch.begin();
		sp.draw(batch);
		batch.end();
		
		// DRAW THE PLAYER
		player.draw(batch);
		
		// DRAW ALL OTHER GAME OBJECTS 
		GOManager.instance.draw(batch);
		
		// DRAW THE HUD
		hud.render();
		
		// DRAW THE DEBUG MODE
		if(Settings.DEBUG_MODE)
			debug.render(world, camera.combined);
		
		if(currentState == PlayState.PAUSE) {
			sp.setAlpha(0.5f);
			batch.begin();
			sp.draw(batch);
			batch.end();
			pauseStage.draw();
		}
		else if(currentState == PlayState.BEGIN) {
			sp.setAlpha(0.5f);
			batch.begin();
			sp.draw(batch);
			batch.end();
			startStage.draw();
		}
			
	}

}
