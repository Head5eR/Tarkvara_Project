package com.mygdx.game;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
	private Window options;
	private TextField mapWidthTextField;
	private TextField mapHeightTextField;
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
	private boolean test = true;
	
	// here comes the light magic
	// used shader light making tutorial - www.alcove-games.com/opengl-es-2-tutorials/lightmap-shader-fire-effect-glsl/
	//	
	
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
	
	private boolean lightOscillate = false;
	//used to make the light flicker
		public float zAngle;
		public static final float zSpeed = 15.0f;
		public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;
	
	
	public MyGdxGame (final GameLauncher game) {
		this.game = game;
		rand = new java.util.Random();
		
		light = new Texture("light.png");
		darkness = new Texture("darkness.png");
		
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
		fightTable.row();
		
		bodyPartsTable = new Table(skin);
		bodyPartsTable.setVisible(false);
		bodyPartsTable.setBackground("textfield");
		bodyPartsTable.setHeight(Gdx.graphics.getHeight()/2);
		bodyPartsTable.setWidth(Gdx.graphics.getWidth()/2);
		bodyPartsTable.setPosition(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
		
		stage.addActor(fightTable);
		stage.addActor(uitable);
		stage.addActor(bodyPartsTable);
		stage.addActor(logTable);

		/////////////////////////////////////////////////////////////////////
		
		initialize();
		
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

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(!pendingChooseTheSlotAction) {
			if(!fightInProgress) {
				movementUpdate();
				handleInput();
			} else {
				handleFightInput();
			}
		}
		
		//camera.position.set(heroSprite.x, heroSprite.y, 0);
		
		zAngle += delta * zSpeed;
		while(zAngle > PI2)
			zAngle -= PI2;

		tile.setX(0);
		tile.setY(winHeight);
		
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
							} else {
								game.batch.draw(textures2.get(value), tile.x, tile.y);
							}
						} else {
							game.batch.draw(textures2.get(value), tile.x, tile.y);
						}
						
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
		stage.getViewport().update(width, height);

		shader.begin();
		shader.setUniformf("resolution", width, height);
		shader.end();
	}
	
	private void genMob() {
		activeMob = Monster.getMonsterWithModifier(ambSystem.monsterLevel());
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
	    		endFight();
	    	}
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
	    	double ambushChance = ambSystem.generateAttackChance();
	    	System.out.println(ambushChance);
	    	if(rand.nextDouble() <= ambushChance) {
	    		//startTheFight();
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
	    	if(logTable.isVisible()) {
	    		logTable.setVisible(false);
	    	} else {
	    		logTable.setVisible(true);
	    	}
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
	    	stage.setDebugAll(false);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
	    	stage.setDebugAll(true);
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
	    		startTheFight();
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
	
	public void startTheFight() {
		genMob();
		createActionButtons();
		createFightLog();
		hero.calculateStatsFromItems();
		findTableWithNameContaining(attackAndDefence, "placeholder");
		updateHeroMonsterInfo();
		fightInProgress = true;
		fightTable.setVisible(true);
	}
	
	public void dialogTest() { // will look into it later
		Dialog dialog = new Dialog("Some Dialog", skin, "dialog") {
			protected void result (Object object) {
				System.out.println("Chosen: " + object);
			}
		}.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
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
		logTable.setWidth(215);
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
		heroinfo.setText("Health: " + hero.getHp() + "/" + hero.getMaxHp() + "\n" + "Armor: " + hero.getArmor() + "\n" + "Attack: " + hero.getAttackDamageStr());
		mobinfo.setText(activeMob.toString());
		if(FightSystem.isDead(hero)) {
			heroinfo.appendText("\n DEAD");
		}
		if(FightSystem.isDead(activeMob)) {
			mobinfo.appendText("\n DEAD");
			createLoot();
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
		mapgen = new MapGenerator(MAP_WIDTH,MAP_HEIGHT, true, true);
		mapgen.generateMap();
		map = mapgen.getMap();
		deadends = mapgen.getDeadends();
		endPos = mapgen.getEndPos();
		startPos = mapgen.getStartPos();
		hero = new Hero(10,10,10,startPos);
		
		ambSystem = new AmbushSystem(mapgen, hero);
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
