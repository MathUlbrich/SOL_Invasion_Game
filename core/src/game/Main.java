package game;

import static game.Settings.TITLE;
import static game.Settings.V_HEIGHT;
import static game.Settings.V_WIDTH;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.state.StateManager;
import game.state.StateManager.State;

public class Main extends ApplicationAdapter {
	
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public StateManager manager;
	
	@Override
	public void create () {
		Assets.load();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		manager = new StateManager(this);
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		//manager.pushState(State.PLAY);
		manager.pushState(State.MENU);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Limpando a tela
		manager.update(1/60f); //Chama o update do estado atual
		manager.render(); //Chama o render do estado atual
		Gdx.graphics.setTitle(TITLE + " --> FPS: " + Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void dispose () {
		manager.dispose();
	}
}
