package game.movements.behaviors;

import static game.Settings.V_WIDTH;
import static game.WorldVars.PPM;
import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import game.HUD;
import game.objects.Security;

public class SecurityBehavior {
	
	Body body; // body
	int direction; // 1 to right and -1 to left
	Vector2 force; // force tha impulses to screen
	Vector2 position;
	float maxForce;
	static boolean highForce = false;
	
	public SecurityBehavior(Security security) {
		direction = random(0, 10) > 7 ? 1 : -1;
		maxForce = highForce ? 0.8f : 1.6f;
		body = security.getBody();
		position = body.getPosition();
		force = new Vector2();
		highForce = !highForce;
	}
	
	public void act() {
		if(HUD.inAlert) {
			force.x = ((V_WIDTH / PPM) - position.x);
			force.y = 0;
			force.nor();
			force.x *= maxForce  * direction;
		
			position.add(force);
			
			if(position.x >= V_WIDTH/PPM)
				direction = -1;
			else if(position.x <= 0)
				direction = 1;
			
			body.setLinearVelocity(force);
		}
		else
			body.setLinearVelocity(0, 0);
	}
	
	public void changeDirection() {
		direction *= -1;
	}

}
