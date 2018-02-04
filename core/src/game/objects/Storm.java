package game.objects;

import static game.WorldVars.DATASTORM_MASK;
import static game.WorldVars.PLAYER_MASK;
import static game.WorldVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import game.Assets;
import game.FixtureFactory;

public class Storm implements GameObject {
	
	// BOX2D DATA
	private World world;
	private Vector2 startPos;
	private Body body;
	private Fixture fixture;
	private Sprite form;
	private Animation<TextureRegion> animation;
	private Animation<TextureRegion> breaking;
	private float stateTimer;
	//private StormSteeringBehavior behavior;
	
	public Storm(World world, Vector2 startPos) {
		this.world = world;
		this.startPos = startPos;
		stateTimer = 0;
		defineObject();
		defineAnimations();
		defineBehavior();
		
//		for(Vector2 v : behavior.getPath()) {
//			Vector2 newVector = new Vector2(v.x * PPM, v.y * PPM);
//			FixtureFactory.createCircleB2DObject(world, BodyType.DynamicBody, newVector, 30, (short)2, (short)4);
//		}
	}

	private void defineBehavior() {
//		behavior = new StormSteeringBehavior(body, 30f/PPM);
//		behavior.setPath(behavior.generateHorizontalPath());
	}
	
	private void defineObject() {
		
		short mask = PLAYER_MASK;
		
		fixture = FixtureFactory.createRactangleB2DObject(
			world, 
			BodyType.DynamicBody, 
			startPos, 
			new float[] {159f/4.5f, 206f/4.5f}, 
			DATASTORM_MASK, 
			mask
		);
		
		body = fixture.getBody();
		body.setUserData(this);
		fixture.setUserData("data-storm");
	}
	
	private void defineAnimations() {
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		Texture texture = Assets.getTextureAsset(Assets.DATASTORM_SPRITES);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		frames.add(new TextureRegion(texture, 160, 0, 154, 206));
		frames.add(new TextureRegion(texture, 0, 0, 159, 206));
		
		animation = new Animation<TextureRegion>(0.3f, frames);
		animation.setPlayMode(PlayMode.LOOP);
		
		frames.clear();
		
		breaking = new Animation<TextureRegion>(0.3f,frames);
		animation.setPlayMode(PlayMode.LOOP);
		
		form = new Sprite(animation.getKeyFrame(stateTimer));
		form.setSize(form.getWidth() / (PPM * 2.5f), form.getHeight() / (PPM * 2.5f));
	}
	
	public void draw(Batch batch) {
		batch.begin();
		form.draw(batch);
		batch.end();
	}
	
	public void update(float dt) {
		updatePosition(dt);
		form.setRegion(getFrame());
		stateTimer += dt;
//		behavior.update(dt);
	}
	
	private void updatePosition(float dt) {
		form.setPosition(
				body.getPosition().x - form.getWidth()/2, 
				body.getPosition().y - form.getHeight()/2
		);
	}
	
	public TextureRegion getFrame() {
		return animation.getKeyFrame(stateTimer);
	}
	
	public void die() {

	}
	
	public void destroyObject() {
		die();
		world.destroyBody(body);
	}
	
	public Body getBody() {
		return body;
	}
	
	public void dispose() {
		form.getTexture().dispose();
	}
	
}
