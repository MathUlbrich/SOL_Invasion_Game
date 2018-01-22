package game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {
	
	private static final AssetManager manager = new AssetManager();
	
	// MUST BE LOADED
	public static final String YUSAKU_SPRITES = "playmaker.png";
	public static final String KURIBALL_SPRITE = "Kuriball.png";
	public static final String GREEN_SECURITY_SPRITE = "green-security.png";
	public static final String PURPLE_SECURITY_SPRITE = "purple-security.png";
	public static final String BACKGROUND_IMAGE = "bg.png";
	public static final String DATASTORM_SPRITES = "storm.png";
	public static final String SECURITIES_SPRITES = "securities.png";
	public static final String START_SPRITES = "start.png";
	public static final String KURIHUD = "kurihud.png";
	public static final String DATAPIECE_SPRITES = "dp.png";
	public static final String PROGRESS_BAR = "ProgressBar/bar.png";
	public static final String PROGRESS_KNOB_B = "ProgressBar/knob.png";
	public static final String PROGRESS_KNOB = "ProgressBar/saku.png";
	public static final String RED_EFFECT = "red-effect.png";
	
	public static final String GAMESKIN = "skin/gameskin.json";
	public static final String GAMEMAP = "map.tmx"; 
	public static final String BREAKING_EFFECT = "break-effect.mp3";
	public static final String GAMEFONT = "font/font.fnt";
	public static final String GAMEFONT_GRADIENT = "font/font-gradient.fnt";
	public static final String WARNING_FONT = "font/warning.fnt";
	
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
		manager.load(PROGRESS_BAR, Texture.class);
		manager.load(PROGRESS_KNOB_B, Texture.class);
		manager.load(PROGRESS_KNOB, Texture.class);
		manager.load(RED_EFFECT, Texture.class);
		
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
	
}
