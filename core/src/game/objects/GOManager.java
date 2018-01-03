package game.objects;


import java.util.LinkedList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.Batch;

public class GOManager {
	
	public static GOManager instance = new GOManager();
	private List<GameObject> objs = new LinkedList<GameObject>();
	
	private GOManager() {}
	
	public void draw(Batch batch) {
		for(GameObject go : objs)
			go.draw(batch);
	}
	
	public void update(float dt) {
		for(GameObject go : objs)
			go.update(dt);
	}
	
	public void addGameObject(GameObject go) {
		objs.add(go);
	}
	
	public void removeGameObject(GameObject go) {
		objs.remove(go);
	}
	
	public List<GameObject> getGameObjects() {
		return objs;
	}
}
