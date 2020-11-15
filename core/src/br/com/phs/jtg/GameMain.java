package br.com.phs.jtg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.phs.jtg.helpers.GameManager;
import br.com.phs.jtg.scenes.MainMenu;

public class GameMain extends Game {

	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GameManager.INSTANCE.initializeGameData();
		setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getSpriteBatch() { return this.batch; }
}
