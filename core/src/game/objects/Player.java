package game.objects;

import static game.Assets.YUSAKU_SPRITES;
import static game.Assets.getTextureAsset;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import game.Bullet;
import game.FixtureFactory;
import game.input.InputState;

public class Player implements GameObject {
	
	public enum State {
		RUNNING, ATTACKING
	}
	
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

		defineObject();
		defineAnimations();
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
		
		frames.add(new TextureRegion(texture, 0, 0, 188, 317));
		frames.add(new TextureRegion(texture, 189, 0, 188, 317));
		
		runningAnimation = new Animation<TextureRegion>(0.4f, frames);
		runningAnimation.setPlayMode(PlayMode.LOOP);
		
		frames.clear();
		frames.add(new TextureRegion(texture));
		
		attackingAnimation = new Animation<TextureRegion>(0.4f, frames);
		attackingAnimation.setPlayMode(PlayMode.LOOP);
		
		frames.clear();
		
		form = new Sprite(runningAnimation.getKeyFrame(stateTimer));
		form.setSize(form.getWidth() / (PPM * 2), form.getHeight() / (PPM * 2));
	}
	
	private void defineObject() {
		short mask = SECURITY_MASK | DATASTORM_MASK | DATAPIECE_MASK;
		
		fixture = FixtureFactory.createRactangleB2DObject(
			world, 
			BodyType.KinematicBody, 
			startPos, 
			new float[] { 192f/4, 334f/4}, 
			PLAYER_MASK, 
			mask	
		);
		
		body = fixture.getBody();
		body.setUserData(this);
		fixture.setUserData("player");
	}
	
	public void update(float dt) {
		updateInput();
		updatePosition(); 
		updateState();
		form.setRegion(getFrame());
		
		stateTimer = oldState == currentState ? stateTimer + dt : 0;
		
		if(oldState != currentState) oldState = currentState;
		
		if(currentState == State.ATTACKING && stateTimer >= attackingAnimation.getAnimationDuration())
			inAttacking = false;
	}
	
	private void updateInput() {
		
		//Anda para a direita
		if(InputState.isDown(InputState.D))
			body.setLinearVelocity(new Vector2(2.4f, 1.2f));
		
		//Anda para a esquerda
		else if(InputState.isDown(InputState.A))
			body.setLinearVelocity(new Vector2(-2.4f, 1.2f));
		
		else
			body.setLinearVelocity(new Vector2(0, 1.2f));
		
		//Atira o kuriball
		if(InputState.isPressed(InputState.P) && bullets.getNum() > 0) {
			shotKuriball();
			stateTimer = 0;
		}
		
		//InputState.update();
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
