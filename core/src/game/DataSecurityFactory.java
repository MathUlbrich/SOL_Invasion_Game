package game;


import java.util.Random;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import game.objects.GOManager;
import game.objects.Security;
import game.objects.Security.DataType;

public abstract class DataSecurityFactory {
	
	public static Security createPurpleDataSecurity(World world) {
		Random random = new Random();
		Security ds = new Security(world, new Vector2(random.nextInt(800) - 800f, random.nextInt(900) + 400f), DataType.PURPLE);
		GOManager.instance.addGameObject(ds);
		return ds;
	}
	
	public static Security createGreenDataSecurity(World world) {
		Random random = new Random();
		Security ds = new Security(world, new Vector2(random.nextInt(400), random.nextInt(900) + 400f), DataType.GREEN);
		GOManager.instance.addGameObject(ds);
		return ds;
	}
	
}
