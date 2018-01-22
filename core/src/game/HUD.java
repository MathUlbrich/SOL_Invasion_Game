package game;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static game.Assets.PROGRESS_BAR;
import static game.Assets.PROGRESS_KNOB;
import static game.Assets.PROGRESS_KNOB_B;
import static game.Assets.RED_EFFECT;
import static game.Assets.WARNING_FONT;
import static game.Assets.getTextureAsset;
import static game.Settings.SCREEN_HEIGHT;
import static game.Settings.SCREEN_WIDTH;
import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import game.objects.Player;
import game.objects.Security;
import game.objects.Security.DataType;
import game.state.SPlay;

public class HUD {

	private Player player;
	private Batch hudBatch;
	private TextureRegion unused;
	private TextureRegion used;
	private int oldbullets;
	private ProgressBar bar;
	private BitmapFont scoreWritter;
	private Stage stage;
	private Image[] images;
	private Sprite alertEffect;
	private float alertTimer;
	public static boolean inAlert;
	private float alpha;
	private boolean flip;
	
	public HUD(final Player player) {
		this.player = player;
		
		// CONFIGURE THE KURIBALL BULLET HUD
		Texture texture = Assets.getTextureAsset(Assets.KURIHUD);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		hudBatch = new SpriteBatch();
		
		// CREATE THE ALERT EFFECT
		Texture alert = getTextureAsset(RED_EFFECT);
		alert.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		alertEffect = new Sprite(alert);
		
		// CREATE THE STAGE
		OrthographicCamera cam = new OrthographicCamera(V_WIDTH, V_HEIGHT);
		stage = new Stage(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, cam), hudBatch);
		
		unused = new TextureRegion(texture, 0, 0, 60, 65);
		used   = new TextureRegion(texture, 61, 0, 60, 65);
		
		oldbullets = player.bullets.getNum();
		
		float top = 490;
		
		// CREATE THE KURIBALLS IMAGE
		images = new Image[3];
		
		images[0] = new Image(unused);
		images[0].setPosition(10, top + 100);
		
		images[1] = new Image(unused);
		images[1].setPosition(60, top + 100);
		
		images[2] = new Image(unused);
		images[2].setPosition(110, top + 100);
		
		images[0].addAction(moveTo(10, top, 0.5f));
		images[1].addAction(moveTo(60, top, 0.5f));
		images[2].addAction(moveTo(110, top, 0.5f));
		
		// CONFIGURE AND CREATE THE PROGRESS BAR
		
		ProgressBarStyle barStyle = new ProgressBarStyle();
		barStyle.background = new TextureRegionDrawable(new TextureRegion(getTextureAsset(PROGRESS_BAR)));
		barStyle.knob = new TextureRegionDrawable(new TextureRegion(getTextureAsset(PROGRESS_KNOB)));
		barStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(getTextureAsset(PROGRESS_KNOB_B)));
		
		barStyle.background.setMinHeight(500f);
		
		bar = new ProgressBar(0, 27000, 2.7f, true, barStyle);
		bar.setPosition(V_WIDTH - 70, 230);
		bar.addAction(moveTo(V_WIDTH - 170, bar.getY(), 0.5f));
		
		// CREATE THE BITMAPFONT
		scoreWritter = new BitmapFont(Gdx.files.internal(Assets.GAMEFONT));

		stage.addActor(bar);
		stage.addActor(images[0]);
		stage.addActor(images[1]);
		stage.addActor(images[2]);
	}
	
	public void update(float dt) {
		
		// CHANGE THE TEXTURE OF UNUSED KURIBALL HUD TO USED
		if(player.bullets.getNum() < oldbullets) {
			oldbullets = player.bullets.getNum();
			images[oldbullets].setDrawable(new TextureRegionDrawable(used));
		}
		
		// CHANGE THE TEXTURE OF USED ALL KURIBALLS HUD TO UNUSED
		else if(player.bullets.getNum() > oldbullets) {
			oldbullets = player.bullets.getNum();
			for(int i = 0; i < oldbullets; i++) {
				images[i].setDrawable(new TextureRegionDrawable(unused));
			}
		}
		
		// IF SECURITY IS IN ALERT MODE, STARTS COUNTING THE TIME
		if(inAlert) {
			alertTimer += dt;
			
			alpha = flip ? alpha + 0.01f : alpha - 0.01f;
			
			if(alpha > 1)
				alpha = 1;
			else if(alpha < 0.5f)
				alpha = 0.5f;
			
			if(alpha == 1 || alpha == 0.5)
				flip = !flip;
	
			alertEffect.setAlpha(alpha);
		}

		// IF THE TIME OF ALERT IS HIGHER THAN 5 SEC
		if(alertTimer > 5 && alpha == 0.5f) {
			inAlert = false;
			alertTimer = 0;
			for(Security s : ContactResolver.changedSecurities)
				s.changeType(DataType.GREEN);
			ContactResolver.changedSecurities.clear();
		}
		
		// UPDATE THE PROGRESS BAR
		bar.setValue(SPlay.stageProgress);
		//bar.act(dt);
		stage.act(dt);
	}
	
	public void render() {
		
		// DRAW THE RED FADE EFFECT OF ALERT
		if(inAlert) {
			hudBatch.begin();
			alertEffect.draw(hudBatch);
			hudBatch.end();
		}
		
		stage.draw();
		
		// DRAW THE SCORE BITMAP
		hudBatch.begin();
		scoreWritter.draw(hudBatch, "SCORE: " + SPlay.score, V_WIDTH - 260f, V_HEIGHT - 170f);
		hudBatch.end();
		
	}
}
