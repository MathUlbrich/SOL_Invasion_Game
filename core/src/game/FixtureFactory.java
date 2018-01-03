package game;

import static game.WorldVars.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class FixtureFactory {
	
	/**
	 * Apos retornar o objeto fixture, use-o para obter o body do mesmo.
	 * @param world
	 * @param type
	 * @param startPos
	 * @param shapeSize
	 * @param user
	 * @param userData
	 * @param category
	 * @param mask
	 * @return um objeto fixture
	 */	
	public static Fixture createCircleB2DObject(World world, BodyType type, Vector2 startPos, float shapeSize, short category, short mask) {
		BodyDef def = new BodyDef();
		def.type = type;
		def.position.set(startPos.x / PPM, startPos.y / PPM);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(shapeSize / PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = category;
		fdef.filter.maskBits = mask;
		
		Body body = world.createBody(def);
		Fixture fixture = body.createFixture(fdef);
		shape.dispose();
		return fixture;
	}
	
	/**
	 * 
	 * @param world
	 * @param type
	 * @param startPos
	 * @param vertices
	 * @param user
	 * @param userData
	 * @param category
	 * @param mask
	 * @return um objeto fixture que pode-se resgatar um body
	 */
	public static Fixture createRactangleB2DObject(World world, BodyType type, Vector2 startPos, float[] hxXhy, short category, short mask) {
		BodyDef def = new BodyDef();
		def.type = type;
		def.position.set(startPos.x / PPM, startPos.y / PPM);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hxXhy[0] / PPM, hxXhy[1] / PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = category;
		fdef.filter.maskBits = mask;
		
		Body body = world.createBody(def);
		Fixture fixture = body.createFixture(fdef);
		shape.dispose();
		return fixture;
	}
	
}
