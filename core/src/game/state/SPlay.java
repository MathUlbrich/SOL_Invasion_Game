package game.state;

import static game.Assets.BACKGROUND_IMAGE;
import static game.Assets.getMap;
import static game.Assets.getTextureAsset;
import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;
import static game.WorldVars.PPM;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import game.BodyCollector;
import game.ContactResolver;
import game.DataSecurityFactory;
import game.HUD;
import game.Main;
import game.Settings;
import game.WorldVars;
import game.input.InputHandler;
import game.input.InputState;
import game.objects.DataPiece;
import game.objects.GOManager;
import game.objects.Player;
import game.objects.Storm;
import game.state.StateManager.State;

public class SPlay extends GameState {
	
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
	
	public static float stageProgress;
	public static int score;
	public static boolean started;
	
	public SPlay(Main game) {
		super(game);
		
		// DEFINE THE OBJECTS USED ON THIS CLASS
		camera = new OrthographicCamera();
		debug = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);
		resolver = new ContactResolver(game);
		player = new Player(world, new Vector2(V_WIDTH/2f, 300f));
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
			
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.filter.categoryBits = WorldVars.BARRAGE_MASK;
			fdef.filter.maskBits = WorldVars.PLAYER_MASK;
			
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
		for(int i = 0; i < 3; i++) {
			MapObject mapObj = map.getLayers().get("storm easy").getObjects().get("storm" + i);
			
			float x = (Float) mapObj.getProperties().get("x");
			float y = (Float) mapObj.getProperties().get("y");
			
			GOManager.instance.addGameObject(new Storm(world, new Vector2(x, y)));
		}
		
		// SET THE CAMERA SETTINGS
		camera.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT /PPM );
		camera.position.x = 0;
		
		// SET THE CONTACTLISTENER TO CONTACTRESOLVER
		world.setContactListener(resolver);
		
		// SET NEW INPUT CONTROLLER
		Gdx.input.setInputProcessor(new InputHandler());
		
		// ADD THE DATA PIECE
		for(int i = 0; i < 4; i++) {
			MapObject mapObj = map.getLayers().get("datas easy").getObjects().get("d" + i);
			
			float x = (Float) mapObj.getProperties().get("x");
			float y = (Float) mapObj.getProperties().get("y");
			
			GOManager.instance.addGameObject(new DataPiece(world, new Vector2(x, y)));
		}
		
		// TRANSLATE THE CAMERA TO MAP POSITION
		camera.translate(V_WIDTH/(2f * PPM), 0);
		
	}
	
	@Override
	public void update(float deltaTime) {
		if(!started) return;
		batch.setProjectionMatrix(camera.combined);
		
		if(InputState.isPressed(InputState.SPACE))
			game.manager.pushState(State.PAUSE);
		
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
		
		//DRAW THE PLAYER
		player.draw(batch);
		
		// DRAW ALL OTHER GAME OBJECTS 
		GOManager.instance.draw(batch);
		
		// DRAW THE HUD
		hud.render();
		
		// DRAW THE DEBUG MODE
		if(Settings.DEBUG_MODE)
			debug.render(world, camera.combined);	
	}

}
