package com.mygdx.game;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyGdxGame implements Screen {
	private final GameLauncher game;
	private ArrayList<Texture> mobtextures = new ArrayList<Texture>();
	private ArrayList<Texture> textures2 = new ArrayList<Texture>();
	private ArrayList<String> mobnames = new ArrayList<String>();
	private OrthographicCamera camera;
	private Rectangle tile;
	private Matrix map;
	private int winWidth = 800;
	private int winHeight = 800;
	private Location endPos;
	private Location startPos;
	private Hero hero;
	private Rectangle heroSprite;
	private MapGenerator mapgen;
	private Stage stage;
	private Table uitable;
	private Table mobtable;
	private Skin skin;
	private Window mobwin;
	private Window invwin;
	private Window equipwin;
	private TextArea mobinfo;
	private TextArea invinfo;
	private TextArea equipinfo;
	private final int MAP_WIDTH = 12;
	private final int MAP_HEIGHT = 14;
	
	public MyGdxGame (final GameLauncher game) {
		this.game = game;
		
		mobtextures.add(new Texture("skeleton.png"));
		mobtextures.add(new Texture("zombie.png"));
		mobtextures.add(new Texture("orc.png"));
		mobtextures.add(new Texture("goblin.png"));
		mobtextures.add(new Texture("vampire.png"));
		mobtextures.add(new Texture("spider.png"));
		
		
		for(Texture tex : mobtextures) {
			String filename = ((FileTextureData) tex.getTextureData()).getFileHandle().name();
			String name = filename.replace(".png", "");
			mobnames.add(name);
		}
		System.out.println(mobnames.toString());

		textures2.add(new Texture("tile texture_0.png"));
		textures2.add(new Texture("wall texture.png"));
		textures2.add(new Texture("start.png"));
		textures2.add(new Texture("exit.png"));
		textures2.add(new Texture("stick.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 800);
		
		///////////////////////// UI ////////////////////////////////////////
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		mobinfo = new TextArea("info", skin);
		mobinfo.setPrefRows(3);
		invinfo = new TextArea("Inventory contents", skin);
		invinfo.setPrefRows(13);
		equipinfo = new TextArea("Contents", skin);
		equipinfo.setPrefRows(13);

		mobwin = new Window("Generated monster", skin);
		
		mobtable = new Table();
		mobtable.setWidth(mobwin.getWidth());
		mobtable.setHeight(mobwin.getHeight());
		mobtable.add(new Image(textures2.get(3)));
		mobtable.add(mobinfo);
		
		mobwin.add(mobtable);

		invwin = new Window("Inventory", skin);
		invwin.setWidth(50);
		invwin.setHeight(50);
		invwin.add(invinfo);
		invwin.setVisible(false);
		
		equipwin = new Window("Equipment", skin);
		equipwin.setWidth(50);
		equipwin.setHeight(50);
		equipwin.add(equipinfo);
		equipwin.setVisible(false);
		
		stage = new Stage();
		uitable = new Table();
		uitable.setFillParent(true);
		uitable.align(Align.topRight);
		uitable.setHeight(Gdx.graphics.getHeight());
		uitable.setWidth(Gdx.graphics.getWidth());
		//uitable.setPosition(0, Gdx.graphics.getHeight());
		
		stage.addActor(uitable);
		uitable.add(mobwin);
		uitable.row();
		uitable.add(equipwin);
		uitable.add(invwin);
		uitable.getCell(equipwin).maxSize(300);
		
		/////////////////////////////////////////////////////////////////////
		mapgen = new MapGenerator(MAP_WIDTH,MAP_HEIGHT, true, true);
		mapgen.generateMap();
		map = mapgen.getMap();
		endPos = mapgen.getEndPos();
		startPos = mapgen.getStartPos();
		hero = new Hero(1,1,1,startPos);
		
		tile = new Rectangle();
		tile.width = 64;
		tile.height = 64;
		
		heroSprite = new Rectangle();
		heroSprite.width = 64;
		heroSprite.height = 64;
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput();
		movementUpdate();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		tile.setX(0);
		tile.setY(winHeight);

		for(int y=0; y<map.getWidth(); y++) {
			tile.setX(0);
			tile.setY(tile.y-tile.getHeight());
			
			for(int x=0; x<map.getLength(); x++) {
				int value = map.getCell(x, y);
				game.batch.draw(textures2.get(value), tile.x, tile.y);
				if(startPos.getX() == x && startPos.getY() == y) {
					game.batch.draw(textures2.get(2), tile.x, tile.y);
				}
				if(endPos.getX() == x && endPos.getY() == y) {
					game.batch.draw(textures2.get(3), tile.x, tile.y);
				}
				
				if(hero.getLoc().getX() == x && hero.getLoc().getY() == y) {
					heroSprite.x = tile.x;
					heroSprite.y = tile.y;
					game.batch.draw(textures2.get(4), heroSprite.x, heroSprite.y);
				}
				tile.setX(tile.x + tile.getWidth());
			}
		}
		
		game.batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		uitable.debug();
	}
	
	@Override
	public void dispose () {
		game.batch.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		camera.update();	
		stage.getViewport().update(width, height);
	}
	
	private void genMob() {
		Monster mob = new Monster();
		int randInt = (int) Math.floor(Math.random()*4);
		Cell imgcell = mobtable.getCells().get(0);
		int foundindex = mobnames.indexOf(mob.getName().toLowerCase());
		if (foundindex >= 0) {
			imgcell.setActor(new Image(mobtextures.get(foundindex)));
		} else {
			foundindex = randInt;
			imgcell.setActor(new Image(textures2.get(foundindex)));
		}
		mobinfo.setText(mob.toString());	
	}
	
	private void movementUpdate() {
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
	    	hero.move(map,2);
	    	genMob();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
	    	hero.move(map,1);
	    	genMob();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
	    	hero.move(map,8);
	    	genMob();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
	    	hero.move(map,4);
	    	genMob();
	    }
	    camera.update();
	}

	private void handleInput() {
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-10, 0, 0);
	    }
	    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	    	camera.translate(10, 0, 0);
	    }
	    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
	    	camera.translate(0, -10, 0);
	    }
	    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	    	camera.translate(0, 10, 0);
	    }
	    
	    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
	    	if(equipwin.isVisible()) {
	    		equipwin.setVisible(false);
	    	} else {
	    		invwin.setVisible(false);
	    		updateInvEquip();
	    		equipwin.setVisible(true);
	    	}
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
	    	if(invwin.isVisible()) {
	    		invwin.setVisible(false);
	    	} else {
	    		equipwin.setVisible(false);
	    		updateInvEquip();
	    		invwin.setVisible(true);
	    	}
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(1);
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(1);
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(2);
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(2);
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(3);
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(3);
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(4);
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(4);
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(5);
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(5);
	    	}
	    	updateInvEquip();
	    }
	    
	    if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
	    	 mapgen = new MapGenerator(MAP_WIDTH,MAP_HEIGHT, true, true);
	 		 mapgen.generateMap();
	 		 map = mapgen.getMap();
	 		 endPos = mapgen.getEndPos();
	 		 startPos = mapgen.getStartPos();
	 		 hero.setLoc(startPos);

	    }
	   
	    if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
	    	//if(camera.zoom < 1.30f) {
	    		camera.zoom += 0.02;
	    	//}
	    }
	    if (Gdx.input.isKeyPressed(Input.Keys.X)) {
	    	if(camera.zoom > 0.60) {
	    		camera.zoom -= 0.02;
	    	}
	    }
	    camera.update();
	 }
	
	public void updateInvEquip() {
		equipinfo.setText(hero.getAllEquiped());
		invinfo.setText(hero.getInventory());	
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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

}
