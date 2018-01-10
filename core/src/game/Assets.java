package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	private static final AssetManager manager = new AssetManager();
	
	// MUST BE LOADED
	public static final String YUSAKU_SPRITES = "Yusaku.png";
	public static final String KURIBALL_SPRITE = "Kuriball.png";
	public static final String GREEN_SECURITY_SPRITE = "green-security.png";
	public static final String PURPLE_SECURITY_SPRITE = "purple-security.png";
	public static final String BACKGROUND_IMAGE = "bg.png";
	public static final String DATASTORM_SPRITES = "storm.png";
	public static final String SECURITIES_SPRITES = "securities.png";
	public static final String START_SPRITES = "start.png";
	public static final String KURIHUD = "kurihud.png";
	public static final String DATAPIECE_SPRITES = "dp.png";
	
	public static final String GAMESKIN = "skin/gameskin.json";
	public static final String GAMEMAP = "gamemap.tmx";
	public static final String BREAKING_EFFECT = "break-effect.mp3";
	public static final String GAMEFONT = "font/font.fnt";
	public static final String GAMEFONT_GRADIENT = "font/font-gradient.fnt";
	
	public static final String PROXY_MENU = "back-menu.png";
	public static final String PROXY_CIRCLE = "circle.png";
	
	public static void load() {
		manager.load(YUSAKU_SPRITES, Texture.class);
		manager.load(KURIBALL_SPRITE, Texture.class);
		manager.load(GREEN_SECURITY_SPRITE, Texture.class);
		manager.load(PURPLE_SECURITY_SPRITE, Texture.class);
		manager.load(BACKGROUND_IMAGE, Texture.class);
		manager.load(DATASTORM_SPRITES, Texture.class);
		manager.load(SECURITIES_SPRITES, Texture.class);
		manager.load(START_SPRITES, Texture.class);
		manager.load(KURIHUD, Texture.class);
		manager.load(DATAPIECE_SPRITES, Texture.class);
		manager.load(BREAKING_EFFECT, Sound.class);
		manager.load(PROXY_MENU, Texture.class);
		manager.load(PROXY_CIRCLE, Texture.class);
		
		manager.finishLoading();
	}
	
	public static Texture getTextureAsset(String asset) {
		return manager.get(asset);
	}
	
	public static Sound getSoundEffect(String effect) {
		return manager.get(effect);
	}
	
	public static TiledMap getMap() {
		return new TmxMapLoader().load(GAMEMAP);
	}
	
	public static Skin getProgressSkin() {
		return new Skin(Gdx.files.internal(GAMESKIN));
	}
	
}
