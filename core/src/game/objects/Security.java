package game.objects;

import static game.WorldVars.KURIBALL_MASK;
import static game.WorldVars.OUTSTAGE_MASK;
import static game.WorldVars.PLAYER_MASK;
import static game.WorldVars.PPM;
import static game.WorldVars.SECURITY_MASK;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
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

public class Security implements GameObject {
	
	public enum DataType { PURPLE, GREEN }
	public enum State { STAND, BREAKING }
	
	private World world;
	private Body body;
	private Fixture fixture;
	private Vector2 startPos;
	private Sprite form;
	private DataType type;
	private State currentState;
	private float stateTimer;
	private Animation<TextureRegion> breaking;
	private TextureRegion stand;
	public boolean inConflict;
	private boolean destroyed;
	
	public Security(World world, Vector2 startPos, DataType type) {
		this.world = world;
		this.startPos = startPos;
		this.type = type;
		stateTimer = 0;
		defineObject();
		defineAnimations();
	}
	
	private void defineAnimations() {
		Texture texture;
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		texture = Assets.getTextureAsset(Assets.SECURITIES_SPRITES);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		switch(type) {
			case PURPLE:
				stand = new TextureRegion(texture, 0, 183, 84, 136);
				frames.add(new TextureRegion(texture, 334, 0, 84, 136));
				frames.add(new TextureRegion(texture, 240, 0, 93, 146));
				frames.add(new TextureRegion(texture, 131, 0, 108, 160));
				frames.add(new TextureRegion(texture, 0, 0, 130, 182));
				break;
			case GREEN:
				stand = new TextureRegion(texture, 419, 0, 84, 136);
				frames.add(new TextureRegion(texture, 334, 0, 84, 136));
				frames.add(new TextureRegion(texture, 240, 0, 93, 146));
				frames.add(new TextureRegion(texture, 131, 0, 108, 160));
				frames.add(new TextureRegion(texture, 0, 0, 130, 182));
				break;
		}
		
		breaking = new Animation<TextureRegion>(0.04f, frames);
		frames.clear();
		
		form = new Sprite(stand);
		form.setSize(form.getWidth() / PPM, form.getHeight() / PPM);
		body.setUserData(this);
	}
	
	private void defineObject() {
		
		short mask = PLAYER_MASK | OUTSTAGE_MASK | KURIBALL_MASK;
		
		fixture = FixtureFactory.createRactangleB2DObject(
			world, 
			BodyType.DynamicBody, 
			startPos, 
			new float[] {84/2f, 136/2f}, 
			SECURITY_MASK, 
			mask
		);
		
		body = fixture.getBody();
		body.setUserData(this);
		fixture.setUserData("data-security");
		fixture.setSensor(true);
	}
	
	public void draw(Batch batch) {
		batch.begin();
		form.draw(batch);
		batch.end();
	}
	
	public void update(float dt) {
		updatePosition();
		updateState();
		form.setRegion(getFrame());
		
		if(currentState == State.BREAKING)
			stateTimer += dt;
		
		if(breaking.isAnimationFinished(stateTimer))
			destroyed = true;
	}
	
	private void updatePosition() {
		form.setPosition(
				body.getPosition().x - form.getWidth()/2, 
				body.getPosition().y - form.getHeight()/2
		);
	}
	
	public void changeType(DataType type) {
		this.type = type;
		defineAnimations();
	}
	
	private void updateState() {
		if(inConflict)
			currentState = State.BREAKING;
		else
			currentState = State.STAND;
	}
	
	private TextureRegion getFrame() {
		if(currentState == State.BREAKING)
			return breaking.getKeyFrame(stateTimer);
		else
			return stand;
	}
	
	public DataType getType() {
		return type;
	}
	
	private void die() {
		
	}
	
	public void destroyObject() {
		die();
		world.destroyBody(body);
	}
	
	public Body getBody() {
		return body;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
}
