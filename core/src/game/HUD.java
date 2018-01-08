package game;

import static game.Assets.getProgressSkin;
import static game.Settings.V_WIDTH;
import static game.Settings.V_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

import game.objects.Player;
import game.state.SPlay;

public class HUD {

	private Player player;
	private Batch hudBatch;
	private Sprite[] balls;
	private TextureRegion unused;
	private TextureRegion used;
	private int oldbullets;
	private ProgressBar bar;
	private BitmapFont scoreWritter;
	
	public HUD(Player player) {
		this.player = player;
		
		// CONFIGURE THE KURIBALL BULLET HUD
		Texture texture = Assets.getTextureAsset(Assets.KURIHUD);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		hudBatch = new SpriteBatch();
		
		unused = new TextureRegion(texture, 0, 0, 60, 65);
		used   = new TextureRegion(texture, 61, 0, 60, 65);
		
		balls = new Sprite[3];
		oldbullets = player.bullets.getNum();
		
		float top = 490;
		
		balls[0] = new Sprite(unused);
		balls[0].setPosition(10, top);
		
		balls[1] = new Sprite(unused);
		balls[1].setPosition(60f, top);
		
		balls[2] = new Sprite(unused);
		balls[2].setPosition(110f, top);
		
		// CONFIGURE AND CREATE THE PROGRESS BAR
		bar = new ProgressBar(0, 9000, 90, true, getProgressSkin());
		bar.setPosition(V_WIDTH - 170, 230);
		
		// CREATE THE BITMAPFONT
		scoreWritter = new BitmapFont(Gdx.files.internal(Assets.GAMEFONT));
	}
	
	public void update(float dt) {
		
		// CHANGE THE TEXTURE OF UNUSED KURIBALL HUD TO USED
		if(player.bullets.getNum() < oldbullets) {
			oldbullets = player.bullets.getNum();
			balls[oldbullets].setRegion(used);
		}
		
		// CHANGE THE TEXTURE OF USED ALL KURIBALLS HUD TO UNUSED
		else if(player.bullets.getNum() > oldbullets) {
			oldbullets = player.bullets.getNum();
			for(Sprite s : balls) {
				s.setRegion(unused);
			}
		}
		
		// UPDATE THE PROGRESS BAR
		bar.setValue(SPlay.stageProgress);
		bar.act(dt);
	}
	
	public void render() {
		
		// DRAW THE KURIBALLS
		for(Sprite s : balls) {
			hudBatch.begin();
			s.draw(hudBatch);
			hudBatch.end();
		}
		
		// DRAW THE PROGRESS BAR
		hudBatch.begin();
		bar.draw(hudBatch, 1);
		hudBatch.end();
		
		// DRAW THE SCORE BITMAP
		hudBatch.begin();
		scoreWritter.draw(hudBatch, "SCORE: " + SPlay.score, V_WIDTH - 260f, V_HEIGHT - 170f);
		hudBatch.end();
	}
}
