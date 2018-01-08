package game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import game.objects.GOManager;
import game.objects.Security;
import game.objects.Security.DataType;

public abstract class DataSecurityFactory {
	
	public static Security createPurpleDataSecurity(World world, Vector2 pos) {
		Security ds = new Security(world, pos, DataType.PURPLE);
		GOManager.instance.addGameObject(ds);
		return ds;
	}
	
	public static Security createGreenDataSecurity(World world, Vector2 pos) {
		Security ds = new Security(world, pos, DataType.GREEN);
		GOManager.instance.addGameObject(ds);
		return ds;
	}
	
}
