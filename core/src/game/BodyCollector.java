package game;

import com.badlogic.gdx.physics.box2d.Filter;

import game.objects.GOManager;
import game.objects.GameObject;
import game.objects.Security;

public class BodyCollector {
	
	public void collectBodiesToRemove() {
		
		if (ContactResolver.getBodiesToRemove().size > 0) {
			
			GameObject obj = ((GameObject) ContactResolver.getBodiesToRemove().first().getUserData());
			
			try 
			{
				if(obj instanceof Security) {
					
					// This filter able you to don't touch two times consecutive of the same object
					
					Filter filter = ((Security)obj).getBody().getFixtureList().get(0).getFilterData();
					filter.categoryBits = WorldVars.UNTOUCHABLE_MASK;
					filter.maskBits = WorldVars.UNTOUCHABLE_MASK;
					filter.groupIndex = -1;
					((Security)obj).getBody().getFixtureList().get(0).setFilterData(filter);
					
					if(((Security)obj).isDestroyed()) {
						obj.destroyObject();
						GOManager.instance.removeGameObject(obj);
						ContactResolver.getBodiesToRemove().removeFirst();
					}
				}
				else {
					obj.destroyObject();
					GOManager.instance.removeGameObject(obj);
					ContactResolver.getBodiesToRemove().removeFirst();
				}
					
				
			} 
			catch(NullPointerException n) 
			{ 
				System.out.println("ERROR: AN ERROR WAS OCCURRED WHEN REMOVING THE OBJECT"); 
			}
			
		}
	}
	
}
