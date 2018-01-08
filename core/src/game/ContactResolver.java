package game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Queue;

import game.objects.GOManager;
import game.objects.GameObject;
import game.objects.Player;
import game.objects.Security;
import game.objects.Security.DataType;
import game.state.SPlay;
import game.state.StateManager.State;

public class ContactResolver implements ContactListener {

	private static Queue<Body> bodiesToRemove = new Queue<Body>();
	private Main game;
	
	public ContactResolver(Main game) {
		this.game = game;
	}
	
	@Override
	public void beginContact(Contact contact) {	
	
		if(contact.getFixtureA().getUserData().equals("player") &&
				contact.getFixtureB().getUserData().equals("data-security")) {
			
			Security ds = ( (Security) contact.getFixtureB().getBody().getUserData());;
			
			if( ds.getType() == DataType.GREEN ) {
				bodiesToRemove.addFirst(contact.getFixtureB().getBody());
				((Security)contact.getFixtureB().getBody().getUserData()).inConflict = true;
				
				for(GameObject go : GOManager.instance.getGameObjects()) {
					if(go instanceof Security)
						((Security)go).changeType(DataType.PURPLE);
				}
				
				// PLAY THE SECURITY DESTROYING SOUND EFFECT
				
				Sound sound = Assets.getSoundEffect(Assets.BREAKING_EFFECT);
				sound.setVolume(sound.play(), 1);

				//APPLY RED FADE EFFECT
				
				
			}
			else if( ds.getType() == DataType.PURPLE ) {
				
				game.manager.pushState(State.GAME_OVER);
			}
		}
		
		if(contact.getFixtureB().getUserData().equals("kuriball") && 
				contact.getFixtureA().getUserData().equals("data-security")) {
			bodiesToRemove.addFirst(contact.getFixtureA().getBody());
			bodiesToRemove.addFirst(contact.getFixtureB().getBody());
			((Security)contact.getFixtureA().getBody().getUserData()).inConflict = true;
			
			Sound sound = Assets.getSoundEffect(Assets.BREAKING_EFFECT);
			sound.setVolume(sound.play(), 1);
			
		}
		
		if(contact.getFixtureA().getUserData().equals("player") && 
				contact.getFixtureB().getUserData().equals("data-storm")) {
			bodiesToRemove.addFirst(contact.getFixtureB().getBody());
			((Player)contact.getFixtureA().getBody().getUserData()).bullets.charge(3);
		}
		
		if(contact.getFixtureA().getUserData().equals("player") &&
				contact.getFixtureB().getUserData().equals("data-piece")) {
			bodiesToRemove.addFirst(contact.getFixtureB().getBody());
			SPlay.score += 200;
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	
	public static Queue<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}
	
}
