package game;

import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Effects {
	
	private float alertDuration;
	private boolean inAlert = false;
	private long timer, startTime;
	
	private Effects() {}
	
	public static final Effects instance = new Effects();
	
	
	public void alertEffect(final float duration) {
		
		if(!inAlert) {
			alertDuration = duration;
			inAlert = true;
			timer = 0;
			startTime = System.currentTimeMillis();
		}
				
		if(timer < duration) {
			timer += System.currentTimeMillis() - startTime;
			
			System.out.println("Time: " + (timer / 1000) + " seconds");
		}
		else inAlert = false;
	}
	
	public void startEffect() {
		
	}
	
}
