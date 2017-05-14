package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	private Location endPos;
	private Location startPos;
	private Hero hero;
	private Rectangle heroSprite;
	private MapGenerator mapgen;
	private Stage stage;
	private Table uitable;
	private Skin skin;
	private Window mobwin;
	private Window invwin;
	private Window equipwin;
	private TextArea mobinfo;
	private TextArea invinfo;
	private TextArea equipinfo;
	private int MAP_WIDTH = 12;
	private int MAP_HEIGHT = 14;
	private boolean fightInProgress = false;
	private Table fightTable;
	private Window mobFwin;
	private Window heroFwin;
	private TextArea heroinfo;
	private int equipPageNr;
	private int invPageNr;
	private TextArea invPage;
	private TextArea equipPage;
	private Table bodyPartsTable;
	private Monster activeMob;
	private TextButton startTheFight;
	private Table attackAndDefence;
	private Table chooseSlot;
	private AmbushSystem ambSystem;
	private static TextButton log;
	private Table logTable;
	private boolean pendingChooseTheSlotAction = false;
	private java.util.Random rand;
	private ArrayList<Location> deadends;
	private boolean showMap = false;
	private Texture darkness;
	private Window menu;
	private Table pauseMenuTable;
	private boolean paused = false;
	private TextButton exitButton;
	private TextButton saveButton;
	private Window saves;
	private Table saveTable;
	private TextArea saveTo;
	private Label potionAmount;
	private ImageButton potionButton;
	private HashMap<String, Integer> statistics;
	private HashMap<Location, Monster> staticMonsters;
	private Boss boss;
	private ArrayList<Texture> bossTextures = new ArrayList<Texture>();
	
	// here comes the light magic
	// used shader light making tutorial - 
	//www.alcove-games.com/opengl-es-2-tutorials/lightmap-shader-fire-effect-glsl/

	
	public static final float ambientIntensity = .7f;
	public static final Vector3 ambientColor = new Vector3(0.01f, 0.01f, 0.01f);
	private ShaderProgram shader;
	private ShaderProgram lightShader;
	private ShaderProgram defaultShader;
	private Texture light;
	private FrameBuffer fbo;
	final String pixelShader =  Gdx.files.internal("pixelShader.glsl").readString();
	final String vertexShader = Gdx.files.internal("vertexShader.glsl").readString();
	final String defaultPixelShader = Gdx.files.internal("defaultPixelShader.glsl").readString();
	
	//used to make the light flicker
	private boolean lightOscillate = true;
	public float zAngle;
	public static final float zSpeed = 15.0f;
	public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;
	
	
	public MyGdxGame (final GameLauncher game) {
		this.game = game;
		load();
		createStatistics();
		initialize();	
	}
	
	public MyGdxGame (final GameLauncher game, int width, int height) {
		this.MAP_WIDTH = width;
		this.MAP_HEIGHT = height;
		this.game = game;
		load();
		createStatistics();
		initialize();	
		generateStaticMobs();
	}
	
	public MyGdxGame (final GameLauncher game, Hero hero, MapGenerator mapgen, 
			HashMap<String, Integer> stat, HashMap<Location,Monster> staticMonsters) {
		this.game = game;
		this.statistics = stat;
		this.staticMonsters = staticMonsters;
		load();
		initialize(mapgen, hero);	
	}
	

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		checkIfPaused();

		if(!paused) {
			if(!pendingChooseTheSlotAction) {
				if(!fightInProgress) {
					movementUpdate();
					handleInput();
				} else {
					handleFightInput();
				}
			}
		}
		
		//camera.position.set(heroSprite.x, heroSprite.y, 0);
		
		zAngle += delta * zSpeed;
		while(zAngle > PI2)
			zAngle -= PI2;

		tile.setX(0);
		tile.setY(Gdx.graphics.getHeight());
		
		if(!showMap) {
			fbo.begin();
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.setShader(defaultShader);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.batch.begin();
			float lightSize = lightOscillate? (237.5f + 12.5f * (float)Math.sin(zAngle) + .2f*MathUtils.random()):250f;
			game.batch.draw(light, heroSprite.x +32 - lightSize*0.5f, heroSprite.y +32 + 0.5f - lightSize*0.5f, lightSize, lightSize);
			game.batch.end();
			fbo.end();
			
			
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.setShader(shader);
			game.batch.begin();
			fbo.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
			light.bind(0); //we force the binding of a texture on first texture unit to avoid artifacts
						   //this is because our default and ambient shader dont use multi texturing...
						   //you can basically bind anything, it doesnt matter
			
			int heroX = hero.getLoc().getX();
			int heroY = hero.getLoc().getY();
			
			int nearbyXone;
			int nearbyXtwo;
			int nearbyYone;
			int nearbyYtwo;
			
			for(int y=heroY-1; y<=heroY+1; y++) {
				tile.setX(0);
				tile.setY(tile.y-tile.getHeight());
				for(int x=heroX-1; x<=heroX+1; x++) {					
					if(y < map.getWidth() && y >= 0 && x < map.getLength() && x >= 0) {
						int value = map.getCell(x, y);
						Location currentLoc = new Location(x,y);
						
						game.batch.draw(textures2.get(value), tile.x, tile.y);
						
						if(staticMonsters.containsKey(currentLoc)) {
							int randInt = (int) Math.floor(Math.random()*4);
							int foundindex = mobnames.indexOf(staticMonsters.get(currentLoc).getName().toLowerCase());
							if (foundindex >= 0) {
								game.batch.draw(mobtextures.get(foundindex), tile.x, tile.y);
							} else {
								foundindex = randInt;
								game.batch.draw(textures2.get(foundindex), tile.x, tile.y);
							}
							
						}
						if(startPos.getX() == x && startPos.getY() == y) {
							game.batch.draw(textures2.get(2), tile.x, tile.y);
						}
						if(endPos.getX() == x && endPos.getY() == y) {
							game.batch.draw(textures2.get(3), tile.x, tile.y);
						}
						if(mapgen.getBossLoc().getX() == x && mapgen.getBossLoc().getY() == y) {
							game.batch.draw(bossTextures.get(boss.getLevel()), tile.x, tile.y);
						}
						
						if(Math.abs(y-heroY) == 1 && (Math.abs(x-heroX) == 1)) {
							//means corner
							nearbyXone = x;
							if(x > heroX) {
								nearbyXtwo = x-1;
							} else {
								nearbyXtwo = x+1;
							}
							nearbyYtwo = y;
							if(y > heroY) {
								nearbyYone = y-1;
							} else {
								nearbyYone = y+1;
							}
							if(map.getCell(nearbyXone, nearbyYone) == 1 && map.getCell(nearbyXtwo, nearbyYtwo) == 1) {
								game.batch.draw(darkness, tile.x, tile.y);
							}
						}
						
						
						if(hero.getLoc().getX() == x && hero.getLoc().getY() == y) {
							heroSprite.x = tile.x;
							heroSprite.y = tile.y;
							game.batch.draw(textures2.get(4), heroSprite.x, heroSprite.y);
							camera.position.set(heroSprite.x, heroSprite.y, 0);
							camera.update();
						}
//						if(deadends.contains(new Location(x,y))) {
//							game.batch.draw(textures2.get(2), tile.x, tile.y);
//						}
					} else {
						game.batch.draw(darkness, tile.x, tile.y);
					}
					tile.setX(tile.x + tile.getWidth());
				}
			}
			
		} else {
			game.batch.setProjectionMatrix(camera.combined);
			game.batch.setShader(defaultShader);
			game.batch.begin();
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
					
					Location currentLoc = new Location(x,y);
					
					if(staticMonsters.containsKey(currentLoc)) {
						int randInt = (int) Math.floor(Math.random()*4);
						int foundindex = mobnames.indexOf(staticMonsters.get(currentLoc).getName().toLowerCase());
						if (foundindex >= 0) {
							game.batch.draw(mobtextures.get(foundindex), tile.x, tile.y);
						} else {
							foundindex = randInt;
							game.batch.draw(textures2.get(foundindex), tile.x, tile.y);
						}
						
					}
					
					if(hero.getLoc().getX() == x && hero.getLoc().getY() == y) {
						heroSprite.x = tile.x;
						heroSprite.y = tile.y;
						game.batch.draw(textures2.get(4), heroSprite.x, heroSprite.y);
						camera.position.set(heroSprite.x, heroSprite.y, 0);
						camera.update();
					}
					if(deadends.contains(new Location(x,y))) {
						game.batch.draw(textures2.get(2), tile.x, tile.y);
					}
					tile.setX(tile.x + tile.getWidth());
				}
			}
		}
		game.batch.end();
		
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void dispose () {
		game.batch.dispose();
		skin.dispose();
		stage.dispose();
		shader.dispose();
		defaultShader.dispose();
		fbo.dispose();
		
	}
	
	@Override
	public void resize(int width, int height) {
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		camera.update();

		stage.getViewport().update(width, height, true);

		shader.begin();
		shader.setUniformf("resolution", width, height);
		shader.end();
	}
	
	private void genMob(Monster mob) {
		activeMob = mob;
		int randInt = (int) Math.floor(Math.random()*4);
		Cell imgcell = fightTable.getCells().get(1);
		int foundindex;
		if(Boss.class.isInstance(mob)) {
			System.out.println("Instance is boss!");
			Boss b = (Boss) mob;
			foundindex = b.getLevel();
			imgcell.setActor(new Image(bossTextures.get(foundindex)));
		} else {
			foundindex = mobnames.indexOf(activeMob.getName().toLowerCase());
			if (foundindex >= 0) {
				imgcell.setActor(new Image(mobtextures.get(foundindex)));
			} else {
				foundindex = randInt;
				imgcell.setActor(new Image(textures2.get(foundindex)));
			}
		}
		
		mobinfo.setText(activeMob.toString());	
		//System.out.println(mob.getBody());
	}
	
	private void handleFightInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    	if(fightTable.isVisible()) {
	    		endFight();
	    	}
	    }
		if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
	    	activeMob.setHp(0);
	    	updateHeroMonsterInfo();
	    }
		
	}
	
	private void endFight() {
		logTable.setVisible(false);
		fightInProgress = false;
		fightTable.setVisible(false);
		mobFwin.removeActor(mobFwin.findActor("lootbtn"));
		attackAndDefence.clear();
	}
	
	private void movementUpdate() {
		Location oldHeroLoc = hero.getLoc();
		
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
	    
	    Location newHeroLoc = hero.getLoc();
	    if(!oldHeroLoc.equals(newHeroLoc)) {
	    	statistics.put("steps", statistics.get("steps"));
	    	Monster mob = null;
	    	if(staticMonsters.containsKey(newHeroLoc)) {
	    		System.out.println("attacked");
	    		mob = staticMonsters.get(newHeroLoc);
	    		staticMonsters.remove(newHeroLoc);
	    	} else if(mapgen.getBossLoc().equals(newHeroLoc)) {
	    		mob = boss;
	    	} else {
	    		double ambushChance = ambSystem.generateAttackChance();
	    		System.out.println("ambush chance: " + ambushChance);
	    		if(rand.nextDouble() <= ambushChance) {
		    		mob = Monster.getMonsterWithModifier(
		    				ambSystem.monsterLevel(hero.getLoc()));
		    	}	    		
	    	}
	    	if(mob != null) {
	    		startTheFight(mob);
	    	}
	    }
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
	    		chooseEquipMethod(1 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(2 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		chooseEquipMethod(2 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(3 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		chooseEquipMethod(3 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(4 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		chooseEquipMethod(4 + 5*(invPageNr-1));
	    	}
	    	updateInvEquip();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
	    	if(equipwin.isVisible()) {
	    		hero.unequip(5 + 5*(equipPageNr-1));
	    	} else if (invwin.isVisible()) {
	    		chooseEquipMethod(5 + 5*(invPageNr-1));
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
	    	initialize();
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
	    	Boss boss = Boss.getBoss(2);
	    	if(boss == null) {
	    		System.out.println("no boss");
	    	} else {
	    		System.out.println(boss);
	    	}
	    	
//	    	if(logTable.isVisible()) {
//	    		logTable.setVisible(false);
//	    	} else {
//	    		logTable.setVisible(true);
//	    	}
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
	    	stage.setDebugAll(false);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
	    	stage.setDebugAll(true);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    		startTheFight(Monster.getMonster());
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
    		if(showMap) {
    			showMap = false;
    		} else {
    			showMap = true;
    		}
    		
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
	    	if(lightOscillate) {
	    		lightOscillate = false;
    		} else {
    			lightOscillate = true;
    		}
	    }
	    
	    camera.update();
	 }
	
	private void checkIfPaused() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
	    	if(paused && menu.isVisible()) {
	    		paused = false;
	    		menu.setVisible(false);
	    	} else if (!saves.isVisible()){
	    		paused = true;
	    		menu.setVisible(true);
	    	}
	    }
	}
	
	public void startTheFight(Monster mob) {
		genMob(mob);
		createActionButtons();
		createFightLog();
		hero.calculateStatsFromItems();
		findTableWithNameContaining(attackAndDefence, "placeholder");
		updateHeroMonsterInfo();
		fightInProgress = true;
		fightTable.setVisible(true);
	}
	
	public void sureAboutClosingDialog() {
		Dialog dialog = new Dialog("Close the game?", skin, "dialog") {
			protected void result (Object object) {
				if(object.equals(true)) {
					Gdx.app.exit();
				} else {
					this.hide();
				}
			}
		}.text("Are you sure?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
			.key(Keys.ESCAPE, false).show(stage);
	}

	
	public void createFightLog() {
		logTable.clear();
		logTable.setVisible(true);
		log = new TextButton("	Fight log: ", skin);
		ScrollPane logPane = new ScrollPane(log,skin);
		logPane.setFillParent(true);
		log.setFillParent(true);
		log.setTouchable(Touchable.disabled);
		log.getLabel().setAlignment(Align.topLeft);
		logTable.add(logPane).expand().bottom().left();
		logTable.setHeight(Gdx.graphics.getHeight());
		logTable.setWidth(256);
		logTable.setPosition(0, 0);
	}
	
	public static void addToLog(String newText) {
		if(log != null) {
			log.setText(log.getText() + "\n" +newText);
		}
	}
	
	public void chooseEquipMethod(int invItemNumber) {
		Item item = hero.getInventoryItemFromNumber(invItemNumber);
		if(item != null) {
			if(item.checkIfMelee(item)) {
				System.out.println("okay, it's melee, generating window");
				createChooseWeaponSlot(invItemNumber);
			} else if(item.checkIfShield(item)) {
				System.out.println("oaky, it's shield, equipping in weaponslot2");
				hero.equipShield((Shield) item); // equipped only in second weapon slot
			} else {
				System.out.println("okay, it's smth else, equipping in suitable slot");
				hero.equipFromInv(invItemNumber);
			}
		}	
	}
	
	public void createChooseWeaponSlot(int invItemNumber) {
		chooseSlot = new Table(skin);
		chooseSlot.setVisible(true);
		chooseSlot.setBackground("textfield");
		chooseSlot.setHeight(Gdx.graphics.getHeight()/8);
		chooseSlot.setWidth(Gdx.graphics.getWidth()/8);
		chooseSlot.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		chooseSlot.setName(""+invItemNumber);

		for(int i = 0; i<hero.weaponSlots.length; i++) {
			TextButton btn = new TextButton("WeaponSlot " + i, skin);
			btn.setName(""+i);
			btn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	Item item = hero.getInventoryItemFromNumber(Integer.parseInt(actor.getParent().getName()));
		        	hero.equipMelee((MeleeWeapon) item, Integer.parseInt(actor.getName()));
		        	updateInvEquip();
		        	chooseSlot.remove();
		        	pendingChooseTheSlotAction = false;
		        }
		    });
			chooseSlot.add(btn);
		}
		stage.addActor(chooseSlot);
		pendingChooseTheSlotAction = true;
	}
	
	public void updateHeroMonsterInfo() {
		heroinfo.setText("Health: " + hero.getHp() + "/" + hero.getMaxHp() + 
				"\n" + "Armor: " + hero.getArmor() + 
				" (reduces " + hero.getArmor()*0.1 + " dmg)" +
				"\n" + "Attack: " + hero.getAttackDamageStr());
		mobinfo.setText(activeMob.toString());
		int amount = hero.getPotionAmount();
		potionAmount.setText("" + amount);
		if(amount <= 0) {
			potionButton.setDisabled(true);
			potionButton.setChecked(true);
		} else {
			potionButton.setChecked(false);
		}
		if(FightSystem.isDead(hero)) {
			heroinfo.appendText("\n DEAD");
		}
		if(FightSystem.isDead(activeMob)) {
			mobinfo.appendText("\n DEAD");
			if(Boss.class.isInstance(activeMob)) {
				statistics.put("boss", statistics.get("boss") +1);
				mapgen.setBossLoc(new Location(-1,-1));
				endFight();
				showStatistics();
			} else {
				createLoot();
				System.out.println("killed monster's mod: " + activeMob.getMod().getName());
				statistics.put(activeMob.getMod().getName(), 
						statistics.get(activeMob.getMod().getName()) +1);
			}
			
		}
		
	}
	
	public boolean attackAndDefenceIsComplete() {
		Array<Cell> cells = attackAndDefence.getCells();
		for(Cell c : cells) {
			if(c.hasActor()) {
				if(c.getActor().getClass().getSimpleName().equals("Table")) {
					TextArea t = findTextAreaInTable((Table) c.getActor());
					if(t.getName() == null) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void createActionButtons() {
		int numOfAttacks = FightSystem.howManyAttacksToPick(hero);
		int numOfDefences = FightSystem.howManyDefencesToPick(hero);
		System.out.println("num of attacks: " + numOfAttacks + " num of defs: " + numOfDefences);
		if(numOfAttacks == 1 & numOfDefences == 1) {
			attackAndDefence.add(createButtonAndText(true));
			attackAndDefence.row();
			attackAndDefence.add(createButtonAndText(false));
			attackAndDefence.row();
			createStartTheFightButton();
		} else if(numOfAttacks == 2) {
			attackAndDefence.add(createButtonAndText(true));
			attackAndDefence.row();
			attackAndDefence.add(createOptionalButtonAndText());
		} else if (numOfAttacks == 1 & numOfDefences == 2) {
			attackAndDefence.add(createButtonAndText(true));
			attackAndDefence.row();
			attackAndDefence.add(createButtonAndText(false));
			attackAndDefence.row();
			attackAndDefence.add(createButtonAndText(false));
			createStartTheFightButton();
		}
	}
	
	public Table createButtonAndText(boolean isAtck) { 
		// attackAction TABLE consists of -> Button (text == Choose attack), TextArea (name == bodypart nr, text == bodypart fullname)
		Table table = new Table();
		String btnText;
		TextButton btn;
		Array<Cell> existingCells = attackAndDefence.getCells();
		int counter = 0;
		if(isAtck) {
			for(Cell c : existingCells) {
				if(c.hasActor()) {
					if(c.getActor().getName() != null & c.getActor().getName().contains("attackAction")) { // find how many TABLES of attackAction already exist
						counter++;
					}
				}
			}
			table.setName("attackAction" + counter);
			btn = new TextButton("Choose attack", skin);
			btn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	String parentName = actor.getParent().getName();
		        	int parentIndex = Integer.parseInt(parentName.substring(parentName.length()-1));
		            fillBodypartsTable(true, parentIndex);
		        }
		    });
			btnText = "Choose attack";
		} else {
			for(Cell c : existingCells) {
				if(c.hasActor()) {
					if(c.getActor().getName() != null & c.getActor().getName().contains("defenceAction")) {
						counter++;
					}
				}
			}
			table.setName("defenceAction" + counter);
			btn = new TextButton("Choose defence", skin);
			btn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	String parentName = actor.getParent().getName();
		        	int parentIndex = Integer.parseInt(parentName.substring(parentName.length()-1));
		            fillBodypartsTable(false, parentIndex);
		        }
		    });
		}
		
		table.add(btn);
		TextArea btnAlliedText = new TextArea("",skin);
		btnAlliedText.setDisabled(true);
		table.add(btnAlliedText);
		return table;
	}
	
	public Table createOptionalButtonAndText() {
		Table table = new Table();
		table.setName("optional");
		TextButton attackButton = new TextButton("Attack", skin);
		TextButton defenceButton = new TextButton("Defence", skin);
		attackButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Table t = (Table) actor.getParent().getParent();
				t.getCell(actor.getParent()).setActor(createButtonAndText(true));
	            createStartTheFightButton();
	        }
		});
		
		defenceButton.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Table t = (Table) actor.getParent().getParent();
				t.getCell(actor.getParent()).setActor(createButtonAndText(false));
	            createStartTheFightButton();
	        }
		});
		table.add(attackButton);
		TextArea text = new TextArea(" or ", skin);
		text.setDisabled(true);
		table.add(text);
		table.add(defenceButton);
		return table;
	}
	
	public void createLoot() {

		if(mobFwin.findActor("lootbtn") == null) {
			mobinfo.setText(activeMob.toString() + "\n" + "DEAD");
			mobFwin.row();
			TextButton lootbtn = new TextButton("LOOT BODY", skin);
			lootbtn.setName("lootbtn");
			lootbtn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	Item item = LootSystem.generateLoot(activeMob);
		        	if(item != null) {
		        		addToLog(item.getName() + " acquired!");
		        		hero.inventory.add(item);
		        		hero.setPotionAmount(hero.getPotionAmount() + LootSystem.dropPotion());
		        	} else {
		        		System.out.println("smth bad happened during loot generation");
		        	}
		        	updateInvEquip();
		            endFight();
		        }
		    });
			mobFwin.add(lootbtn);
		}
	}

	public ArrayList<Integer> getAllActionsOfType(String actionType) {
		ArrayList<Integer> allActions = new ArrayList<Integer>();
		Array<Cell> cells = attackAndDefence.getCells();
		for(Cell c : cells) {
			if(c.hasActor()) {
				if(c.getActor().getClass().getSimpleName().equals("Table")) {
					if(c.getActor().getName().contains(actionType)) {
						Table t = (Table) c.getActor();
						allActions.add(Integer.parseInt(findTextAreaInTable(t).getName()));
					}
				}
			}
		}
		
		return allActions;
	}
	
	public void createStartTheFightButton() {
		startTheFight = new TextButton("ATTACK!", skin);
		startTheFight.setName("startthefight");
		startTheFight.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	if(attackAndDefenceIsComplete() & !FightSystem.isDead(activeMob) & !FightSystem.isDead(hero)) {
	        		hero.setPickedAttacks(getAllActionsOfType("attackAction"));
	        		hero.setPickedDefs(getAllActionsOfType("defenceAction"));
	        		FightSystem.fight(hero, activeMob);
	        		updateHeroMonsterInfo();
	        		attackAndDefence.clear();
	        		createActionButtons();
	        	}
	            
	        }
	    });
		attackAndDefence.row();
		attackAndDefence.add(startTheFight);
	}
	
	public void updateInvEquip() {
		equipinfo.setText(hero.getAllEquiped(equipPageNr));
		invinfo.setText(hero.getInventory(invPageNr));
		int numOfPages = (int) Math.round(Math.ceil((double) hero.getInvSize() / 5));
		invPage.setText(invPageNr + "/" + numOfPages); 
		numOfPages = (int) Math.round(Math.ceil((double) hero.getSlotsArraySize() / 5));
		equipPage.setText(equipPageNr + "/" + numOfPages); 
		hero.calculateStatsFromItems();
	}
	
	public void fillBodypartsTable(boolean isAttacking, int serialNr) { //serialNr is number which indicated what button exactly triggered this method
		Body body;
		Bodypart picked;
		fightTable.setVisible(false);
		
		if(isAttacking) {
			body = activeMob.getBody();
			bodyPartsTable.setName("Attack"+serialNr);
		} else {
			bodyPartsTable.setName("Defence"+serialNr);
			body = hero.getBody();
		}
		
		for(Bodypart bp : body.getBodyParts()) {
			bodyPartsTable.add(new TextArea(bp.getName(), skin));
			
			TextButton btn = new TextButton("pick",skin);
			
			btn.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	Bodypart picked;
		        	int bodyPartIndex = (actor.getZIndex()-1) /2;
		        	String parentName = actor.getParent().getName();
	    			int parentIndex = Integer.parseInt(actor.getParent().getName().substring(parentName.length()-1));
	    			Table t;
	    			TextArea ta;
		       
		        	if(actor.getParent().getName().startsWith("Attack")) {
		    			picked = activeMob.getBody().getBodyParts().get(bodyPartIndex);		    			
		    			t = (Table) findActorInTableByName(attackAndDefence, "attackAction" + parentIndex);
		    			ta = findTextAreaInTable(t);
		    			ta.setText(picked.getName());
		    			ta.setName("" + bodyPartIndex);
		    		} else if (actor.getParent().getName().startsWith("Defence")) {
		    			picked = hero.getBody().getBodyParts().get(bodyPartIndex);
		    			t = (Table) findActorInTableByName(attackAndDefence, "defenceAction" + parentIndex);
		    			ta = findTextAreaInTable(t);
		    			ta.setText(picked.getName());
		    			ta.setName("" + bodyPartIndex);
		    		}
		        	

		        	bodyPartsTable.setVisible(false);
		        	fightTable.setVisible(true);
		        	bodyPartsTable.clear();
		        }
			});
			bodyPartsTable.add(btn);
			bodyPartsTable.row();
		}
		
		bodyPartsTable.setVisible(true);
		
	}
	
	public Actor findActorInTableByName(Table t, String name) {
		Array<Cell> cells = t.getCells();
		for(Cell c : cells) {
			if(c.hasActor()) {
				String actorName = c.getActor().getName();
				if(actorName.equals(name)) {
					return c.getActor();
				}
			}
		}
		return null;
	}
	
	public Table findTableWithNameContaining(Table t, String name) {
		for(Cell c : t.getCells()) {
			if(c.hasActor()) {
				if(c.getActor().getClass().getSimpleName().equals("Table")) {
					if(c.getActor().getName().contains(name)) {
						return (Table) c.getActor();
					}
				}
				
			}
		}
		return null;
	}
	
	
	public TextArea findTextAreaInTable(Table t) {
		for(Cell c : t.getCells()) {
			String actorName = c.getActor().getClass().getSimpleName();
			if(actorName.equals("TextArea")) {
				return (TextArea) c.getActor();
			}
		}
		return null;
	}
	
	private void initialize() {
		MapGenerator mg = new MapGenerator(MAP_WIDTH,MAP_HEIGHT, true, true);
		mg.generateMap();
		initialize(mg, new Hero(10,10,10,mg.getStartPos()));
		
	}
	
	private void initialize(MapGenerator mapgen, Hero hero) {
		this.mapgen = mapgen;
		this.hero = hero;
		boss = getBoss();
		map = mapgen.getMap();
		deadends = mapgen.getDeadends();
		endPos = mapgen.getEndPos();
		startPos = mapgen.getStartPos();
		ambSystem = new AmbushSystem(mapgen, hero);
	}
	
	
	private void saveOption(FileHandle file, String date) {
		String name = file.name();
		if(name != "" && date != "") { 
			Date d = new Date(Long.parseLong(date));
			TextArea text = new TextArea(name+ " " + d.toGMTString(), skin);
			text.setDisabled(true);
			TextButton save = new TextButton("Save", skin);
			save.setName(file.nameWithoutExtension());
			save.addListener(new ChangeListener() {
		        @Override
		        public void changed (ChangeEvent event, Actor actor) {
		        	saveGame(actor.getName());
		        	System.out.println("saving to file " + actor.getName()); 
		        	saves.setVisible(false);
		        	menu.setVisible(true);
		        }
		    });
			saveTable.add(text).minWidth(256);
			saveTable.add(save).row();
		} else {
			System.out.println("Came empty string");
		}
	}
	
	private void saveGame(String saveName) {
		System.out.println("saving to: " + saveName);
		SaveSystem.saveGame(this, saveName);
	}
	
	private void load() {
		rand = new java.util.Random();
		
		light = new Texture("light.png");
		darkness = new Texture("darkness.png");
		
		bossTextures.add(new Texture("minotaur.png"));
		bossTextures.add(new Texture("lich.png"));
		bossTextures.add(new Texture("knight.png"));
		
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

		textures2.add(new Texture("tile_texture_0.png"));
		textures2.add(new Texture("wall_texture.png"));
		textures2.add(new Texture("start.png"));
		textures2.add(new Texture("exit.png"));
		textures2.add(new Texture("stick.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		
		///////////////////////// UI ////////////////////////////////////////		
		skin = new Skin(Gdx.files.internal("uiskin.json"));		
		
		invinfo = new TextArea("Inventory contents", skin);
		invinfo.setPrefRows(13);
		invinfo.setDisabled(true);
		equipinfo = new TextArea("Contents", skin);
		equipinfo.setPrefRows(13);
		equipinfo.setDisabled(true);
		
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
		
		fightTable = new Table(skin);
		fightTable.setVisible(false);
		fightTable.debug();
		mobFwin = new Window("Monster statistics", skin);
		mobinfo = new TextArea("", skin);
		mobinfo.setDisabled(true);
		mobinfo.setPrefRows(3);
		mobFwin.add(mobinfo);
		
		heroFwin = new Window("Hero statistics", skin);
		heroinfo = new TextArea("", skin);
		heroinfo.setDisabled(true);
		heroFwin.add(heroinfo).fill().expand();
		heroFwin.row();
		
		fightTable.setBackground("textfield");
		fightTable.setHeight(Gdx.graphics.getHeight()/2);
		fightTable.setWidth(Gdx.graphics.getWidth()/2);
		fightTable.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		fightTable.add(new Image(textures2.get(4))).expandX();
		
		fightTable.add(new Image(textures2.get(3))).expandX();
		fightTable.row();
		fightTable.add(heroFwin).fill().expand();
		fightTable.add(mobFwin).fill().expand();
		
		attackAndDefence = new Table(skin);
		logTable = new Table(skin);
		
		fightTable.row();
		fightTable.add(attackAndDefence);
		Table potionTable = new Table();
		potionAmount = new Label("", skin);
		TextureRegionDrawable potionUp = new TextureRegionDrawable(new TextureRegion(new Texture("potionUp.png")));
		TextureRegionDrawable potionDown = new TextureRegionDrawable(new TextureRegion(new Texture("potionDown.png")));
		TextureRegionDrawable potionZero = new TextureRegionDrawable(new TextureRegion(new Texture("potionZero.png")));
		potionButton = new ImageButton(potionUp, potionDown, potionZero);
		potionButton.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	hero.restoreHp();
	        	updateHeroMonsterInfo();
	        }
	    });	
		potionTable.add(potionButton, potionAmount);
		fightTable.add(potionTable);
		fightTable.row();
		
		bodyPartsTable = new Table(skin);
		bodyPartsTable.setVisible(false);
		bodyPartsTable.setBackground("textfield");
		bodyPartsTable.setHeight(Gdx.graphics.getHeight()/2);
		bodyPartsTable.setWidth(Gdx.graphics.getWidth()/2);
		bodyPartsTable.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		
		
		///////////////////PAUSE MENU//////////////////////
		menu = new Window("Menu", skin);
		menu.setVisible(false);
		pauseMenuTable = new Table();
		
		saveButton = new TextButton("Save game", skin);
		exitButton = new TextButton("Exit game", skin);
		
		exitButton.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	GUIelements.sureAboutClosingDialog(stage, skin);
	        }
	    });	
		
		saveButton.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	saveTable.clear();
	        	FileHandle[] files = Gdx.files.local("../").list();
	        	for(FileHandle file: files) {
	        		if(file.extension().equals("ser")) {
	        			saveOption(file, ""+file.lastModified());
	        		}
	        	}
	        	menu.setVisible(false);
	        	saves.setVisible(true);
	        }
	    });	
		pauseMenuTable.add(saveButton).row();
		pauseMenuTable.add(exitButton);
		menu.add(pauseMenuTable);
		menu.setPosition(Gdx.graphics.getWidth()/2-menu.getWidth()/2, Gdx.graphics.getHeight()/2-menu.getHeight()/2);
		
		saves = new Window("Saving the game", skin);
		saves.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
		saves.setPosition(Gdx.graphics.getWidth()/2-saves.getWidth()/2, Gdx.graphics.getHeight()/2-saves.getHeight()/2);
		
		saves.setVisible(false);
		saveTable = new Table();
		Table saveToNewFile = new Table();
		
		saveTo = new TextArea("",skin);
		saveTo.setMessageText("type new save name");
		
		TextButton saveToButton = new TextButton("Save", skin);
		
		saveToButton.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        	String text = saveTo.getText();
	        	System.out.println(text);
	        	if(text.matches("^[a-zA-Z0-9]*$")) {
	        		saveGame(text);
	        	} else {
	        		System.out.println("Wrong save name");
	        	}
	        	saves.setVisible(false);
	        	menu.setVisible(true);
	        }
	    });	
		saveToNewFile.add(saveTo, saveToButton);
		saves.add(saveToNewFile).row();
		saves.add(saveTable).row();
		saves.add(GUIelements.getBackButton(skin));

		
		////////////////////////////////////////////////////////

		stage.addActor(fightTable);
		stage.addActor(uitable);
		stage.addActor(bodyPartsTable);
		stage.addActor(logTable);
		stage.addActor(menu);
		stage.addActor(saves);
		
		/////////////////////////////////////////////////////////////////////
		
		tile = new Rectangle();
		tile.width = 64;
		tile.height = 64;
		
		heroSprite = new Rectangle();
		heroSprite.width = 64;
		heroSprite.height = 64;
		
		////////////////////////////////SHADERS INIT//////////////////////////
		defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);		
		shader = new ShaderProgram(vertexShader, pixelShader);
		shader.begin();
		shader.setUniformi("u_lightmap", 1);
		shader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
				ambientColor.z, ambientIntensity);
		shader.end();
	}
	
	private void createStatistics() {
		statistics = new HashMap<String, Integer>();
		statistics.put("steps", 0);
		statistics.put("weak", 0);
		statistics.put("medium", 0);
		statistics.put("strong", 0);
		statistics.put("dangerous", 0);
		statistics.put("deadly", 0);
		statistics.put("boss", 0);
	}
	
	private void showStatistics() {
		String text = "Steps made: " + statistics.get("steps") +
				"\n Monsters killed " +
				"\n Weak: " + statistics.get("weak") +
				"\n Medium: " + statistics.get("medium") +
				"\n Strong: " + statistics.get("strong") +
				"\n Dangerous: " + statistics.get("dangerous") +
				"\n Deadly: " + statistics.get("deadly") +
				"\n " +
				"\n Bosses killed: " + statistics.get("boss");
		Dialog dialog = new Dialog("Statistics", skin, "dialog") {
			protected void result (Object object) {
				if(object.equals(true)) {
					this.hide();
				}
			}
		}.text(text).button("Ok", true).key(Keys.ENTER, true).show(stage);
	}
	
	private Boss getBoss() {
		int level;
		if(getMAP_WIDTH() < 15 && getMAP_HEIGHT() < 15) {
			level = 1;
		} else if(getMAP_WIDTH() < 30 && getMAP_HEIGHT() < 30) {
			level = 2;
		} else {
			level = 3;
		}
		return Boss.getBoss(level);
	}
	
	private void generateStaticMobs() {
		List<Location> arr = mapgen.getDeadends();
		Collections.shuffle(arr);
		Monster monster;
		arr = (List<Location>) arr.subList(0, arr.size()/2);
		staticMonsters = new HashMap<Location,Monster>();
		for(Location loc : arr) {
			//MapGenerator.getDistance(loc, mapgen.getStartPos())
			 monster = Monster.getMonsterWithModifier(ambSystem.monsterLevel(loc));
			staticMonsters.put(loc, monster);
			System.out.println(monster.getMod());
		}
		System.out.println("deadends size: " + mapgen.getDeadends().size() + " static: " + staticMonsters.size());
		System.out.println(staticMonsters);
		//System.out.println(staticMonsters);
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

	public Hero getHero() {
		return hero;
	}

	public int getMAP_WIDTH() {
		return MAP_WIDTH;
	}

	public int getMAP_HEIGHT() {
		return MAP_HEIGHT;
	}

	public ArrayList<Location> getDeadends() {
		return deadends;
	}

	public Matrix getMap() {
		return map;
	}

	public void setMap(Matrix map) {
		this.map = map;
	}

	public MapGenerator getMapgen() {
		return mapgen;
	}

	public void setMapgen(MapGenerator mapgen) {
		this.mapgen = mapgen;
	}

	public HashMap<String, Integer> getStatistics() {
		return statistics;
	}

	public void setStatistics(HashMap<String, Integer> statistics) {
		this.statistics = statistics;
	}

	public HashMap<Location, Monster> getStaticMonsters() {
		return staticMonsters;
	}

}
