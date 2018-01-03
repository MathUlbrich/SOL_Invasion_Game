package game.objects;


import com.badlogic.gdx.graphics.g2d.Batch;

public interface GameObject {
	
	public void update(float dt);
	
	public void draw(Batch batch);
	
	public void destroyObject();
	
}
