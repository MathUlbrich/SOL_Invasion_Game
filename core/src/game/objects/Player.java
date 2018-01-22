package game.objects;

import static game.Assets.YUSAKU_SPRITES;
import static game.Assets.getTextureAsset;
import static game.WorldVars.BARRAGE_MASK;
import static game.WorldVars.DATAPIECE_MASK;
import static game.WorldVars.DATASTORM_MASK;
import static game.WorldVars.PLAYER_MASK;
import static game.WorldVars.PPM;
import static game.WorldVars.SECURITY_MASK;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import game.Bullet;
import game.input.InputState;

public class Player implements GameObject {
	
	public enum State { RUNNING, ATTACKING }
	
	private World world;
	private Body body;
	private Fixture fixture;
	private State currentState;
	private State oldState;
	private Vector2 startPos;
	private Sprite form;
	private float stateTimer;
	private Animation<TextureRegion> runningAnimation;
	private Animation<TextureRegion> attackingAnimation;
	private boolean inAttacking;
	public Bullet bullets;
	
	public Player(World world, Vector2 startPos) {
		this.world = world;
		this.startPos = startPos;
		
		stateTimer = 0;
		bullets = new Bullet(3);
		bullets.charge(3);

		defineAnimations();
		defineObject();
	}
	
	public void draw(Batch batch) {
		batch.begin();
		form.draw(batch);
		batch.end();
	}
	
	private void defineAnimations() {
		
		Texture texture = getTextureAsset(YUSAKU_SPRITES);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
		// RUNNING FRAMES
		frames.add(new TextureRegion(texture, 0, 0, 202, 314));
		frames.add(new TextureRegion(texture, 203, 0, 202, 314));
		
		runningAnimation = new Animation<TextureRegion>(0.6f, frames);
		runningAnimation.setPlayMode(PlayMode.LOOP);
		
		// ATTACKING FRAMES
		frames.clear();
		frames.add(new TextureRegion(texture, 0, 0, 202, 314));
		
		attackingAnimation = new Animation<TextureRegion>(0.4f, frames);
		attackingAnimation.setPlayMode(PlayMode.LOOP);
		
		frames.clear();
		
		form = new Sprite(runningAnimation.getKeyFrame(stateTimer));
		form.setSize(form.getWidth() / (PPM * 1.8f), form.getHeight() / (PPM * 1.8f));
		form.setOrigin(form.getWidth()/2, form.getHeight()/2);
	}
	
	private void defineObject() {
		short mask = SECURITY_MASK | DATASTORM_MASK | DATAPIECE_MASK | BARRAGE_MASK;

		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(startPos.x / PPM, startPos.y / PPM);
		
		PolygonShape shape = new PolygonShape();
		shape.set(new Vector2[] { 
				new Vector2(-(form.getWidth()/2) + (40/PPM), -form.getHeight()/2),
				new Vector2((form.getWidth()/2) - (50/PPM), -form.getHeight()/2),
				new Vector2((form.getWidth()/2) - (40/PPM), 0),
				new Vector2((form.getWidth()/2) - (10/PPM), 0),
				new Vector2((form.getWidth()/2) - (10/PPM), (form.getHeight()/2) - (20/PPM)),
				new Vector2(-(form.getWidth()/2) + (10/PPM), (form.getHeight()/2) - (20/PPM)),
				new Vector2(-(form.getWidth()/2) + (10/PPM), 0),
				new Vector2(-(form.getWidth()/2) + (30/PPM), 0)
		});
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = PLAYER_MASK;
		fdef.filter.maskBits = mask;
		
		body = world.createBody(def);
		fixture = body.createFixture(fdef);
		body.setUserData(this);
		fixture.setUserData("player");
		
		//shape.dispose();
	}
	
	public void update(float dt) {
		updateInput(dt);
		updatePosition(); 
		updateState();
		form.setRegion(getFrame());
		
		stateTimer = oldState == currentState ? stateTimer + dt : 0;
		
		if(oldState != currentState) oldState = currentState;
		
		if(currentState == State.ATTACKING && stateTimer >= attackingAnimation.getAnimationDuration())
			inAttacking = false;		
	}
	
	private void updateInput(float dt) {
		
		//Anda para a direita
		if(InputState.isDown(InputState.D)) {
			body.setLinearVelocity(new Vector2(2.4f, 1.8f));
			body.setTransform(body.getPosition().x, body.getPosition().y, 75);
			form.setRotation(-25);
		}
		
		//Anda para a esquerda
		else if(InputState.isDown(InputState.A)) {
			body.setLinearVelocity(new Vector2(-2.4f, 1.8f));
			body.setTransform(body.getPosition().x, body.getPosition().y, -75);
			form.setRotation(25);
		}
		
		else {
			body.setLinearVelocity(new Vector2(0, 1.8f));
			body.setTransform(body.getPosition().x, body.getPosition().y, 0);
			form.setRotation(0);
		}
		
		//Atira o kuriball
		if(InputState.isPressed(InputState.P) && bullets.getNum() > 0) {
			shotKuriball();
			stateTimer = 0;
		}
		
		if(InputState.isDown(InputState.W))
			body.setLinearVelocity(body.getLinearVelocity().x, 2.3f);
		else
			body.setLinearVelocity(body.getLinearVelocity().x, 0.5f);
		
		
	}
	
	private void updatePosition() {
		form.setPosition(
			body.getPosition().x - form.getWidth()/2, 
			body.getPosition().y - form.getHeight()/2
		);
	}
	
	private void updateState() {
		if(inAttacking)
			currentState = State.ATTACKING;
		else
			currentState = State.RUNNING;
	}
	
	public TextureRegion getFrame() {
		switch(currentState) {
			case RUNNING:
				return runningAnimation.getKeyFrame(stateTimer);
			case ATTACKING:
				return attackingAnimation.getKeyFrame(stateTimer);
			default:
				return runningAnimation.getKeyFrame(stateTimer);
		}
	}
	
	private void die() {
		//Faz alguma coisa antes de ser destruido
	}
	
	public void destroyObject() {
		die();
		world.destroyBody(body);
	}
	
	private void shotKuriball() {
		inAttacking = true;
		
		bullets.shot();
		
		GOManager.instance.addGameObject(new Kuriball(
			world, 
			new Vector2(
				(body.getPosition().x * PPM) - 40f, (body.getPosition().y * PPM) + 110f)
			)
		);
	}
	
	public Body getBody() {
		return body;
	}
	
}
