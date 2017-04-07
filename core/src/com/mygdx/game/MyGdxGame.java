package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

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
	private final int MAP_WIDTH = 40;
	private final int MAP_HEIGHT = 40;
	private boolean fightInProgress = false;
	private Table fightTable;
	private TextButton btn1;
	private TextButton btn2;
	private Window mobFwin;
	private Window heroFwin;
	private TextArea heroinfo;
	private int equipPageNr;
	private int invPageNr;
	private TextArea invPage;
	private TextArea equipPage;
	private Table actionsTable;
	private Monster activeMob;
	private TextArea pickedAttack;
	private TextArea pickedDefence;
	private TextButton startTheFight;
	private Window options;
	private TextField mapWidthTextField;
	private TextField mapHeightTextField;
	private java.util.Random randomizer;
	
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
		//System.out.println(mobnames.toString());

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
		invwin.row();
		invPage = new TextArea("", skin);
		invwin.add(invPage);
		
		equipwin = new Window("Equipment", skin);
		equipwin.setWidth(50);
		//equipwin.setHeight(50);
		equipwin.add(equipinfo);
		equipwin.setVisible(false);
		equipwin.row();
		equipPage = new TextArea("",skin);
		equipwin.add(equipPage);
		
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
		
		options = new Window("Options", skin);
		options.setVisible(false);
		mapHeightTextField = new TextField("Height: ", skin);
		mapWidthTextField = new TextField("Height: ", skin);
		options.add(mapHeightTextField);
		options.add(mapWidthTextField);
		//uitable.row();
		//uitable.add(options);
		
		fightTable = new Table(skin);
		fightTable.setVisible(false);
		mobFwin = new Window("Monster statistics", skin);
		mobinfo = new TextArea("", skin);
		mobinfo.setPrefRows(3);
		mobFwin.add(mobinfo);
		
		heroFwin = new Window("Hero statistics", skin);
		heroinfo = new TextArea("", skin);
		heroFwin.add(heroinfo).fill().expand();
		heroFwin.row();
		btn1 = new TextButton("Choose attack", skin);
		btn1.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            fillActionsTable(true);
	        }
	    });
		heroFwin.add(btn1);
		
		pickedAttack = new TextArea("",skin);
		heroFwin.add(pickedAttack);
		heroFwin.row();
		btn2 = new TextButton("Choose defence", skin);
		btn2.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            fillActionsTable(false);
	        }
	    });
		
		heroFwin.add(btn2);
		pickedDefence = new TextArea("",skin);
		heroFwin.add(pickedDefence);
		
		startTheFight = new TextButton("ATTACK!", skin);
		startTheFight.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	if(pickedDefence.getName() != null & pickedAttack.getName() != null & !FightSystem.isDead(activeMob) & !FightSystem.isDead(hero)) {
	        		hero.setAttackMove(Integer.parseInt(pickedAttack.getName()));
	        		hero.setDefenceMove(Integer.parseInt(pickedDefence.getName()));
	        		activeMob.setAttackMove(MathUtils.random(hero.getBody().getBodyParts().size()-1));
	        		FightSystem.fight(hero, activeMob);
	        		updateHeroMonsterInfo();
	        	}
	            
	        }
	    });
		heroFwin.row();
		heroFwin.add(startTheFight);
		
		fightTable.setBackground("textfield");
		fightTable.setHeight(Gdx.graphics.getHeight()/2);
		fightTable.setWidth(Gdx.graphics.getWidth()/2);
		fightTable.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		fightTable.add(new Image(textures2.get(4))).expandX();
		
		fightTable.add(new Image(textures2.get(3))).expandX();
		fightTable.row();
		fightTable.add(heroFwin).fill().expand();
		fightTable.add(mobFwin).fill().expand();
		
		actionsTable = new Table(skin);
		actionsTable.setVisible(false);
		actionsTable.setBackground("textfield");
		actionsTable.setHeight(Gdx.graphics.getHeight()/2);
		actionsTable.setWidth(Gdx.graphics.getWidth()/2);
		actionsTable.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		
		stage.addActor(fightTable);
		stage.addActor(uitable);
		stage.addActor(actionsTable);

		/////////////////////////////////////////////////////////////////////
		
		mapgen = new MapGenerator(MAP_WIDTH,MAP_HEIGHT, true, true);
		mapgen.generateMap();
		map = mapgen.getMap();
		endPos = mapgen.getEndPos();
		startPos = mapgen.getStartPos();
		hero = new Hero(10,10,10,startPos);
		
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
		activeMob = Monster.getMonster();
		int randInt = (int) Math.floor(Math.random()*4);
		Cell imgcell = fightTable.getCells().get(1);
		int foundindex = mobnames.indexOf(activeMob.getName().toLowerCase());
		if (foundindex >= 0) {
			imgcell.setActor(new Image(mobtextures.get(foundindex)));
		} else {
			foundindex = randInt;
			imgcell.setActor(new Image(textures2.get(foundindex)));
		}
		mobinfo.setText(activeMob.toString());	
		//System.out.println(mob.getBody());
	}
	
	private void handleFightInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    	if(fightTable.isVisible()) {
	    		fightInProgress = false;
	    		fightTable.setVisible(false);
	    		mobFwin.removeActor(mobFwin.findActor("lootbtn"));
	    		pickedAttack.setName(null);
	    		pickedAttack.setText("");
	    		pickedDefence.setName(null);
	    		pickedDefence.setText("");
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
	    	if(options.isVisible()) {
	    		options.setVisible(false);
	    	} else {
	    		options.setVisible(true);
	    	}
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    	
	    		genMob();
	    		updateHeroMonsterInfo();
	    		fightInProgress = true;
	    		fightTable.setVisible(true);
	    }
	    
	    camera.update();
	 }
	
	public void updateHeroMonsterInfo() {
		heroinfo.setText("Health: " + hero.getHp() + "/" + hero.getMaxHp() + "\n" + "Armor: " + hero.getArmor() + "\n" + "Attack: " + hero.getAttackDamageStr());
		mobinfo.setText(activeMob.toString());
		if(FightSystem.isDead(hero)) {
			heroinfo.appendText("\n DEAD");
		}
		if(FightSystem.isDead(activeMob)) {
			mobinfo.appendText("\n DEAD");
		}
		createLoot();
	}
	
	
	public void createLoot() {
		if(FightSystem.isDead(activeMob) & mobFwin.findActor("lootbtn") == null) {
			mobinfo.setText(activeMob.toString() + "\n" + "DEAD");
			mobFwin.row();
			TextButton lootbtn = new TextButton("LOOT BODY", skin);
			lootbtn.setName("lootbtn");
			lootbtn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		            System.out.println("HERE COMES THE LOOT!");
		        }
		    });
			mobFwin.add(lootbtn);
		}
	}
	
	public void updateInvEquip() {
		equipinfo.setText(hero.getAllEquiped(equipPageNr));
		invinfo.setText(hero.getInventory(invPageNr));
		int numOfPages = (int) Math.round(Math.ceil((double) hero.getInvSize() / 5));
		invPage.setText(invPageNr + "/" + numOfPages); 
		numOfPages = (int) Math.round(Math.ceil((double) hero.getSlotsArraySize() / 5));
		equipPage.setText(equipPageNr + "/" + numOfPages); 
	}
	
	public void fillActionsTable(boolean isAttacking) {
		Body body;
		Bodypart picked;
		fightTable.setVisible(false);
		
		if(isAttacking) {
			body = activeMob.getBody();
			actionsTable.setName("Attack");
		} else {
			actionsTable.setName("Defence");
			body = hero.getBody();
		}
		
		for(Bodypart bp : body.getBodyParts()) {
			actionsTable.add(new TextArea(bp.getName(), skin));
			
			TextButton btn = new TextButton("pick",skin);
			
			btn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	Bodypart picked;
		        	int bodyPartIndex = (actor.getZIndex()-1) /2;
		       
		        	if(actor.getParent().getName() == "Attack") {
		    			picked = activeMob.getBody().getBodyParts().get(bodyPartIndex);
		    			pickedAttack.setText(picked.getName());
		    			pickedAttack.setName("" + bodyPartIndex);
		    		} else {
		    			picked = hero.getBody().getBodyParts().get(bodyPartIndex);
		    			pickedDefence.setText(picked.getName());
		    			pickedDefence.setName("" + bodyPartIndex);
		    		}
		        	//System.out.println(picked);
		        	actionsTable.setVisible(false);
		        	fightTable.setVisible(true);
		        	actionsTable.clear();
		        }
			});
			actionsTable.add(btn);
			actionsTable.row();
		}
		
		actionsTable.setVisible(true);
		
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
