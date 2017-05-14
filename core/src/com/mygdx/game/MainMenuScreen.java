package com.mygdx.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
	
	private Skin skin;
	private Stage stage;
	private TextButton newGame;
	private TextButton loadGame;
	private TextButton exitGame;
	private Window mapSize;
	private Window loadOptions;
	private TextArea customMapLen;
	private TextArea customMapWidth;
	private ImageButton tenXten;
	private ImageButton twentyXtwenty;
	private ImageButton fiftyXfifty;
	private ImageButton createCustom;
	private Table logoTable;
	private Table menuItems;
	private Table rootTable;
	private Table customMapSize;
	private Table loadTable;
	private Dialog wrongValues;

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
		mapSize.setFillParent(true);
		
		loadOptions = new Window("Choose a game to load", skin);
		loadOptions.setDebug(true);
		loadOptions.setVisible(false);
		loadOptions.setFillParent(true);
		
		loadTable = new Table();
		loadOptions.add(loadTable).expand().fill().row();
		loadOptions.add(GUIelements.getBackButton(skin)).prefWidth(250);
		
		customMapSize = new Table();
		
		Table customSizes = new Table();
		customMapLen = new TextArea("", skin);
		customMapLen.setMessageText("length");
		customMapWidth = new TextArea("", skin);
		customMapWidth.setMessageText("width");
		
		TextFieldFilter filter = new TextFieldFilter.DigitsOnlyFilter();
		customMapLen.setTextFieldFilter(filter);
		customMapWidth.setTextFieldFilter(filter);

		customSizes.add(customMapLen).width(192/2).uniform();
		customSizes.add(customMapWidth).width(192/2).uniform();
		
		tenXten = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("10x10up.png"))),
				new TextureRegionDrawable(new TextureRegion(new Texture("10x10down.png"))));
		twentyXtwenty = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("20x20up.png"))),
				new TextureRegionDrawable(new TextureRegion(new Texture("20x20down.png"))));
		fiftyXfifty = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("50x50up.png"))),
				new TextureRegionDrawable(new TextureRegion(new Texture("50x50down.png"))));
		createCustom = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("customup.png"))),
				new TextureRegionDrawable(new TextureRegion(new Texture("customdown.png"))));
		
		tenXten.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        		 game.setScreen(new MyGdxGame(game, 10, 10));
					 dispose();
	        }
	    });
		
		twentyXtwenty.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        		 game.setScreen(new MyGdxGame(game, 20, 20));
					 dispose();
	        }
	    });
		
		fiftyXfifty.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        		 game.setScreen(new MyGdxGame(game, 50, 50));
					 dispose();
	        }
	    });
		
		createCustom.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	String mapLen = customMapLen.getText();
	        	String mapWidth = customMapWidth.getText();
	        	if(mapLen != "" && mapWidth != "" && mapLen.matches("[0-9]+") && mapWidth.matches("[0-9]+") && mapWidth.length() < 4 && mapLen.length() < 4) {
	        		int length = Integer.parseInt(mapLen);
	        		int width = Integer.parseInt(mapWidth);
	        		 game.setScreen(new MyGdxGame(game, length, width));
					 dispose();
	        	} else {
	        		wrongValues.show(stage);
	        	}
	        }
	    });
		
		//////// error dialog /////////////
		wrongValues = new Dialog("WRONG MAP SIZE VALUES", skin);
		wrongValues.setZIndex(10);
		TextButton okBtn = new TextButton("OK", skin);	
		okBtn.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	((Dialog) actor.getParent()).hide();
	        }
	    });
		wrongValues.setName("error");
		wrongValues.add(okBtn);
		/////////////////////////
	
		mapSize.add(tenXten).pad(5f);
		mapSize.add(twentyXtwenty).pad(5f);
		mapSize.add(fiftyXfifty).pad(5f);
		mapSize.add(createCustom).pad(5f);
		mapSize.row();
		mapSize.add(GUIelements.getBackButton(skin)).fill();
		mapSize.add();
		mapSize.add();
		mapSize.add(customSizes);
		
		newGame.pad(5f).addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				 mapSize.setVisible(true); 
			}
		});
		
		loadGame.pad(5f).addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				 // show all save files in game folder, load buttons near each save filename
				loadTable.clear();
				loadOptions.setVisible(true);
				// read all save files
				FileHandle[] files = Gdx.files.local("../").list();
				for(FileHandle file: files) {
					if(file.extension().equals("ser")) {
						loadOption(file, ""+file.lastModified());
					}
				}
				
			}
		});
		
		
		exitGame.pad(5f).addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				GUIelements.sureAboutClosingDialog(stage, skin);
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
		stage.addActor(mapSize);
		stage.addActor(loadOptions);
		
	}
	
	private void loadOption(FileHandle file, String date) {
		String name = file.name();
		if(name != "" && date != "") { 
			Date d = new Date(Long.parseLong(date));
			TextArea text = new TextArea(name+ " " + d.toGMTString(), skin);
			text.setDisabled(true);
			TextButton load = new TextButton("Load", skin);
			load.setName(file.toString());
			load.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        		 loadGame(new FileHandle(actor.getName()));
		        }
		    });
			loadTable.add(text).minWidth(256);
			loadTable.add(load);
			loadTable.row();
		} else {
			System.out.println("Came empty string");
		}
	}
	
	private void loadGame(FileHandle file) {
		ArrayList<Object> heroAndMapgen = SaveSystem.loadGame(file.toString());
		Hero hero = (Hero) heroAndMapgen.get(0);
		MapGenerator mg = (MapGenerator) heroAndMapgen.get(1);
		HashMap<String, Integer> stat = (HashMap<String, Integer>) heroAndMapgen.get(2);
		HashMap<Location, Monster> staticMonsters = (HashMap<Location, Monster>) heroAndMapgen.get(3);
		if(!hero.equals(null) && !mg.equals(null) && !stat.equals(null) && !staticMonsters.equals(null)) {
			game.setScreen(new MyGdxGame(game, hero, mg, stat, staticMonsters));
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
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
