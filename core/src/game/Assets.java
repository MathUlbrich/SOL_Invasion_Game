package game;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	
	private static final AssetManager manager = new AssetManager();
	
	public static final String YUSAKU_SPRITES = "Yusaku.png";
	public static final String KURIBALL_SPRITE = "Kuriball.png";
	public static final String GREEN_SECURITY_SPRITE = "green-security.png";
	public static final String PURPLE_SECURITY_SPRITE = "purple-security.png";
	public static final String BACKGROUND_IMAGE = "bg.png";
	public static final String DATASTORM_SPRITES = "storm.png";
	public static final String SECURITIES_SPRITES = "securities.png";
	public static final String START_SPRITES = "start.png";
	public static final String KURIBALL_FRONT = "kuri-front.png";
	
	public static void load() {
		manager.load(YUSAKU_SPRITES, Texture.class);
		manager.load(KURIBALL_SPRITE, Texture.class);
		manager.load(GREEN_SECURITY_SPRITE, Texture.class);
		manager.load(PURPLE_SECURITY_SPRITE, Texture.class);
		manager.load(BACKGROUND_IMAGE, Texture.class);
		manager.load(DATASTORM_SPRITES, Texture.class);
		manager.load(SECURITIES_SPRITES, Texture.class);
		manager.load(START_SPRITES, Texture.class);
		manager.load(KURIBALL_FRONT, Texture.class);
		
		manager.finishLoading();
	}
	
	public static Texture getTextureAsset(String asset) {
		return manager.get(asset);
	}
	
}
