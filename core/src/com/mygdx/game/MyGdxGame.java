package com.mygdx.game;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

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
	private boolean fightInProgress = false;
	private Table ftable;
	private TextButton btn1;
	private TextButton btn2;
	private Window monsterFwin;
	private Window heroFwin;
	private TextArea heroinfo;
	private int equipPageNr;
	private int invPageNr;
	
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
		
		
		invinfo = new TextArea("Inventory contents", skin);
		invinfo.setPrefRows(13);
		equipinfo = new TextArea("Contents", skin);
		equipinfo.setPrefRows(13);
		
		equipPageNr = 1;
		invPageNr = 1;

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
		Gdx.input.setInputProcessor(stage); // IMPORTANT
		uitable = new Table();
		//uitable.setFillParent(true);
		uitable.align(Align.topRight);
		uitable.setPosition(Gdx.graphics.getWidth()/2, 0);
		uitable.setHeight(Gdx.graphics.getHeight());
		uitable.setWidth(Gdx.graphics.getWidth()/2);
		
		
		uitable.add(mobwin);
		uitable.row();
		uitable.add(equipwin);
		uitable.add(invwin);
		uitable.getCell(equipwin).prefWidth(160);
		uitable.getCell(invwin).prefWidth(160);
		
		ftable = new Table(skin);
		ftable.setVisible(false);
		monsterFwin = new Window("Monster statistics", skin);
		mobinfo = new TextArea("", skin);
		mobinfo.setPrefRows(3);
		monsterFwin.add(mobinfo);
		
		heroFwin = new Window("Hero statistics", skin);
		heroinfo = new TextArea("", skin);
		heroFwin.add(heroinfo).fill().expand();
		heroFwin.row();
		btn1 = new TextButton("Choose attack", skin);
		btn1.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            System.out.println("Button1 Pressed");
	        }
	    });
		heroFwin.add(btn1);
		heroFwin.row();
		btn2 = new TextButton("Choose defence", skin);
		btn2.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            System.out.println("Button2 Pressed");
	        }
	    });
		
		heroFwin.add(btn2);
		
		ftable.setBackground("textfield");;
		ftable.setHeight(Gdx.graphics.getHeight()/2);
		ftable.setWidth(Gdx.graphics.getWidth()/2);
		ftable.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		ftable.add(new Image(textures2.get(4))).expandX();
		
		ftable.add(new Image(textures2.get(3))).expandX();
		ftable.row();
		ftable.add(heroFwin).fill().expand();
		ftable.add(monsterFwin).fill().expand();
		
		stage.addActor(ftable);
		stage.addActor(uitable);

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

		if(!fightInProgress) {
			movementUpdate();
			handleInput();
		} else {
			handleFightInput();
			//fight(hero, new Monster());
		}
		
		
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
		stage.setDebugAll(true);
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
		Cell imgcell = ftable.getCells().get(1);
		int foundindex = mobnames.indexOf(mob.getName().toLowerCase());
		if (foundindex >= 0) {
			imgcell.setActor(new Image(mobtextures.get(foundindex)));
		} else {
			foundindex = randInt;
			imgcell.setActor(new Image(textures2.get(foundindex)));
		}
		mobinfo.setText(mob.toString());	
		//System.out.println(mob.getBody());
	}
	
	private void fight(Hero h, Monster m) {
		fightInProgress = true;
		Monster mob = new Monster();
		System.out.println(mob);
		//mob.getBody().getBodyParts();
		//hero.get
		
	}
	
	private void handleFightInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
	    	//if(chooseAttack.selected()) {}
			// if(chooseDef.selected()) {}
	    }
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    	if(ftable.isVisible()) {
	    		fightInProgress = false;
	    		ftable.setVisible(false);
	    	}
	    }
		
	}
	
	private void movementUpdate() {
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
	    	hero.move(map,2);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
	    	hero.move(map,1);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
	    	hero.move(map,8);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
	    	hero.move(map,4);
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
	    		hero.unequip(1 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(1 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(2 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(2 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(3 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(3 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(4 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(4 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(5 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		hero.equipFromInv(5 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
	    	if(equipwin.isVisible()) {
	    		if(hero.getSlotsArraySize() - 5*equipPageNr > 0) {
	    			equipPageNr++;
	    			updateInvEquip();
	    		}
	    	} else if (invwin.isVisible()) {
	    		if(hero.getInvSize() - 5*invPageNr > 0) {
	    			invPageNr++;
	    			updateInvEquip();
	    		}
	    	}
	    }
	    
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
	    	if(equipwin.isVisible()) {
	    		if(equipPageNr > 1) {
	    			equipPageNr--;
	    			updateInvEquip();
	    		}
	    	} else if (invwin.isVisible()) {
	    		if(invPageNr > 1) {
	    			invPageNr--;
	    			updateInvEquip();
	    		}
	    	}
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
	    if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
	    	if(uitable.isVisible()) {
	    		uitable.setVisible(false);
	    	} else {
	    		uitable.setVisible(true);
	    	}
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    		genMob();
	    		heroinfo.setText("Health: " + hero.getHealth() + "\n" + "Armor: " + hero.getArmor() + "\n" + "Damage: " + hero.getAttackDamage());
	    		fightInProgress = true;
	    		ftable.setVisible(true);
	    }
	    
	    camera.update();
	 }
	
	public void updateInvEquip() {
		equipinfo.setText(hero.getAllEquiped(equipPageNr));
		invinfo.setText(hero.getInventory(invPageNr));	
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
