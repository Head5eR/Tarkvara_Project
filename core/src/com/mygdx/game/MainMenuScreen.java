package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
	
	private Skin skin;
	private Stage stage;
	private TextButton newGame;
	private TextButton loadGame;
	private TextButton exitGame;
	private Window mapSize;
	private TextArea customMapLen;
	private TextArea customMapWidth;
	private Button tenXten;
	private Button twentyXtwenty;
	private Button fiftyXfifty;
	private Table logoTable;
	private Table menuItems;
	private Table rootTable;

	final GameLauncher game;
	
	OrthographicCamera camera;
	
	public MainMenuScreen(final GameLauncher game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport(camera));
		Gdx.input.setInputProcessor(stage); // IMPORTANT
		
		Image logo = new Image(new Texture("pixel_m.jpeg"));
		
		rootTable = new Table();
		rootTable.setFillParent(true);
		
		logoTable = new Table();
		menuItems = new Table();
		
		newGame = new TextButton("New Game", skin);
		loadGame = new TextButton("Load Game", skin);
		exitGame = new TextButton("Exit", skin);
		
		mapSize = new Window("Map sizes", skin);
		mapSize.setVisible(false);
		mapSize.debug();
		mapSize.setFillParent(true);
		
		tenXten = new Button(skin);
		twentyXtwenty = new Button(skin);
		fiftyXfifty = new Button(skin);
				
		newGame.pad(5f).addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				 //mapSize.setVisible(true);
				 game.setScreen(new MyGdxGame(game));
				 dispose();
			}
		});
		
		loadGame.pad(5f).addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				 // show all save files in game folder, load buttons near each save filename
				ArrayList<Object> heroAndMapgen = SaveSystem.loadGame("../test.ser");
				Hero hero = (Hero) heroAndMapgen.get(0);
				MapGenerator mg = (MapGenerator) heroAndMapgen.get(1);
				if(!hero.equals(null) && !mg.equals(null)) {
					game.setScreen(new MyGdxGame(game, hero, mg));
				}
			}
		});
		
		
		exitGame.pad(5f).addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

		menuItems.add(newGame).expand().fill().space(5f).size(200f, 50f);
		menuItems.row();
		menuItems.add(loadGame).expand().fill().space(5f).size(200f, 50f);
		menuItems.row();
		menuItems.add(exitGame).expand().fill().space(5f).size(200f, 50f);

		stage.addActor(rootTable);
		//stage.setDebugAll(true);

		rootTable.add(logoTable).expandY().top().row();
		rootTable.add(menuItems).expand().top().row();
		logoTable.add(logo);
		rootTable.add(mapSize);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);       
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

}
