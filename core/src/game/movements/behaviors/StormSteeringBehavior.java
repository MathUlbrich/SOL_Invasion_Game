package game.movements.behaviors;

import static game.Settings.V_WIDTH;
import static game.WorldVars.PPM;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath.LinePathParam;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import game.movements.SteeringBehaviorEntity;

public class StormSteeringBehavior {
	
	SteeringBehaviorEntity entity;
	Array<Vector2> path;
	
	public StormSteeringBehavior(Body body, float boundingRadius) {
		entity = new SteeringBehaviorEntity(body, boundingRadius);
	}
	
	public void update(float dt) {
		entity.update(dt);
	}
	
	public void setPath(Array<Vector2> path) {	
		this.path = path;
		LinePath<Vector2> linePath = new LinePath<Vector2>(path);
		
		FollowPath<Vector2, LinePathParam> behavior = new FollowPath<Vector2, LinePathParam>(entity, linePath)
				.setTimeToTarget(0.1f)
				.setArrivalTolerance(1)
				.setDecelerationRadius(80)
				.setPathOffset(30f);
		entity.setBehavior(behavior);
	}
	
	public Array<Vector2> generateHorizontalPath() {
		Array<Vector2> array = new Array<Vector2>();
		array.add(new Vector2(0, entity.getBody().getPosition().y));
		array.add(new Vector2(V_WIDTH / PPM, entity.getBody().getPosition().y));
		return array;
	}
	
	public Array<Vector2> generateVerticalPath() {
		return null;
	}
	
	public Array<Vector2> getPath() {
		return path;
	}
	
}
