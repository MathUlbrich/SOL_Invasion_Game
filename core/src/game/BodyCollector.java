package game;

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
