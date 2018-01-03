package game.objects;

import static game.Assets.KURIBALL_SPRITE;
import static game.Assets.getTextureAsset;
import static game.WorldVars.KURIBALL_MASK;
import static game.WorldVars.PPM;
import static game.WorldVars.SECURITY_MASK;

import game.FixtureFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class Kuriball implements GameObject {
	
	private Sprite form;
	private World world;
	private Body body;
	private Fixture fixture;
	private Vector2 startPos;
	public boolean avaliable;
	
	public Kuriball(World world, Vector2 startPos) {
		this.startPos = startPos;
		this.world = world;
		defineObject();
		defineAnimations();
	}
	
	
	public void update(float dt) {
		updatePosition();
	}
	
	public void draw(Batch batch) {
		batch.begin();
		form.draw(batch);
		batch.end();
	}
	
	private void updatePosition() {
		form.setPosition(
				body.getPosition().x - form.getWidth()/2, 
				body.getPosition().y - form.getHeight()/2
		);
	}
	
	private void defineObject() {
		short mask = SECURITY_MASK;
		
		fixture = FixtureFactory.createCircleB2DObject(
			world, 
			BodyType.KinematicBody, 
			startPos, 
			30f, 
			KURIBALL_MASK, 
			mask	
		);
		
		body = fixture.getBody();
		body.setUserData(this);
		fixture.setUserData("kuriball");
		
		body.setLinearVelocity(0, 5f);
	}
	
	private void defineAnimations() {
		Texture texture = getTextureAsset(KURIBALL_SPRITE);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		form = new Sprite(getTextureAsset(KURIBALL_SPRITE));
		form.setSize(form.getWidth() / (PPM * 2.8f), form.getHeight() / (PPM * 2.8f));
		
		body.setUserData(this);
	}


	public void die() {
		
	}
	
	public void destroyObject() {
		die();
		world.destroyBody(body);
	}
	
}
