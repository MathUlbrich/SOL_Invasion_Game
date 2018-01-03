package game.state;

import static game.Assets.BACKGROUND_IMAGE;
import static game.Assets.getTextureAsset;
import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;
import static game.WorldVars.PPM;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import game.BodyCollector;
import game.ContactResolver;
import game.DataSecurityFactory;
import game.HUD;
import game.Main;
import game.input.InputHandler;
import game.objects.GOManager;
import game.objects.Player;
import game.objects.Storm;

public class SPlay extends GameState {
	
	private OrthographicCamera camera;
	private Box2DDebugRenderer debug;
	private ContactResolver resolver;
	private Sprite[] bg;
	private World world;
	public static boolean isOver = false;
	private Player player;
	private Storm storm;
	private BodyCollector collector;
	private HUD hud;
	
	public SPlay(Main game) {
		super(game);
		
		camera = new OrthographicCamera();
		debug = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);
		resolver = new ContactResolver(game);
		player = new Player(world, new Vector2(-200f, 70f));
		collector = new BodyCollector();
		
		bg = new Sprite[5];
		
		for(int i = 0; i < bg.length; i++) {
			bg[i] = new Sprite(getTextureAsset(BACKGROUND_IMAGE));
			bg[i].getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			bg[i].setSize(bg[i].getWidth() / (PPM * 1f), bg[i].getHeight() / (PPM * 1f));
			bg[i].setPosition(-V_WIDTH / (PPM *2f), bg[i].getHeight() * i);
		}		
		
		for(int i = 0; i < 10; i++) {
			if(new Random().nextBoolean())
				DataSecurityFactory.createGreenDataSecurity(world);
			else
				DataSecurityFactory.createPurpleDataSecurity(world);
		}
		
		storm = new Storm(world, new Vector2(0, 600f));
		
		camera.setToOrtho(false, V_WIDTH / PPM, V_HEIGHT /PPM );
		camera.position.x = 2;
		
		world.setContactListener(resolver);
		Gdx.input.setInputProcessor(new InputHandler());
	}
	
	
	@Override
	public void update(float deltaTime) {
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		camera.position.y += 0.02f;
		
		world.step(deltaTime, 6, 2);
		
		player.update(deltaTime);
		storm.update(deltaTime);
		
		GOManager.instance.update(deltaTime);
		
		collector.collectBodiesToRemove();
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		
		for(Sprite s : bg) {
			batch.begin();
			s.draw(batch);
			batch.end();
		}
		
		player.draw(batch);
		
		storm.draw(batch);
		GOManager.instance.draw(batch);
		
		debug.render(world, camera.combined);
				
	}

}
