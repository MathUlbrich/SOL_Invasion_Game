package game.objects;

import static game.Assets.DATAPIECE_SPRITES;
import static game.Assets.getTextureAsset;
import static game.WorldVars.DATAPIECE_MASK;
import static game.WorldVars.PLAYER_MASK;
import static game.WorldVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import game.FixtureFactory;

public class DataPiece implements GameObject {
	
	private World world;
	private Body body;
	private Fixture fixture;
	private Vector2 startPos;
	private Sprite form;
	
	public DataPiece(World world, Vector2 startPos) {
		this.world = world;
		this.startPos = startPos;
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
	
	private void defineAnimations() {
		Texture texture = getTextureAsset(DATAPIECE_SPRITES);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		form = new Sprite(new TextureRegion(texture));
		form.setSize(form.getWidth() / (2 * PPM), form.getHeight() / (2 * PPM));
		body.setUserData(this);
	}
	
	private void defineObject() {	
		fixture = FixtureFactory.createRactangleB2DObject(
			world, 
			BodyType.DynamicBody, 
			startPos, 
			new float[] {170/4f, 110/4f}, 
			DATAPIECE_MASK, 
			PLAYER_MASK
		);
		
		body = fixture.getBody();
		body.setUserData(this);
		fixture.setUserData("data-piece");
		//fixture.setSensor(true);
	}

	@Override
	public void destroyObject() {
		world.destroyBody(body);
	}
	
}
