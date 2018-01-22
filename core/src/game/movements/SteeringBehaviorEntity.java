package game.movements;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SteeringBehaviorEntity implements Steerable<Vector2> {
	
	// BOX2D DATA
	Body body;
	
	// STEERABLE DATA WITH DEFAULT VALUES
	float maxLinearSpeed = 1.2f;
	float maxLinearAcceleration = 2;
	float maxAngularSpeed = 0;
	float maxAngularAcceleration = 0;
	float zeroThreshould = 0.1f;
	float boundingRadius;
	boolean independent = false;
	boolean tagged = true;
	
	// STEERABLE OBJECTS
	SteeringBehavior<Vector2> behavior;
	SteeringAcceleration<Vector2> acceleration;
	
	public SteeringBehaviorEntity(Body body, float boundingRadius) {
		this.body = body;
		this.boundingRadius = boundingRadius;
	}
	
	public SteeringBehavior<Vector2> getBehavior() {
		return behavior;
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior) {
		this.behavior = behavior;
	}
	
	public SteeringAcceleration<Vector2> getAcceleration() {
		return acceleration;
	}
	
	public void setAcceleration(SteeringAcceleration<Vector2> acceleration) {
		this.acceleration = acceleration;
	}
	
	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	@Override
	public float getOrientation() {
		return body.getAngle();
	}
	
	@Override
	public void setOrientation(float orientation) {
		body.setTransform(getPosition(), orientation);
	}
	
	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y); 
	}
	
	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}
	
	@Override
	public Location<Vector2> newLocation() {
		return new SteeringBehaviorLocation();
	}
	
	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroThreshould;
	}
	
	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		zeroThreshould = value;
	}
	
	@Override
	public float getMaxLinearSpeed() {
		return maxLinearSpeed;
	}
	
	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}
	
	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}
	
	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}
	
	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}
	
	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}
	
	@Override
	public float getMaxAngularAcceleration() {
		return maxLinearAcceleration;
	}
	
	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}
	
	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}
	
	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}
	
	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}
	
	@Override
	public boolean isTagged() {
		return tagged;
	}
	
	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}
	
}
