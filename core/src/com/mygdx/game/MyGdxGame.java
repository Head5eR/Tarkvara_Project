package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture tile0;
	ArrayList<Texture> textures = new ArrayList<Texture>();
	ArrayList<Texture> textures2 = new ArrayList<Texture>();
	Music backgroundMusic;
	private OrthographicCamera camera;
	private InputProcessor inputProc;
	private Rectangle tile;
	private Matrix map;
	private int winWidth = 800;
	private int winHeight = 800;
	private Location endPos;
	private Location startPos;
	private Hero hero;
	private Rectangle heroSprite;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		for(int i=0; i<15; i++) {
			String path = "tile texture_" + i + ".png";
			textures.add(new Texture(path));
		}
		
		textures2.add(new Texture("tile texture_0.png"));
		textures2.add(new Texture("wall texture.png"));
		textures2.add(new Texture("start.png"));
		textures2.add(new Texture("exit.png"));
		textures2.add(new Texture("stick.png"));
		
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\FireFox\\GameProjectJava\\core\\assets\\PINK_GUY_STFU.mp3"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 800);

		Monster monster = new Monster(707);
		System.out.println(monster);
		System.out.println(monster.getMod());
		
//		LabyrinthGenerator gen = new LabyrinthGenerator(12,10);
//		gen.generateMap();
//		map = gen.getMap();
	
		MapGenerator mapgen = new MapGenerator(20,20);
		mapgen.generateMap();
		map = mapgen.getMap();
		endPos = mapgen.getEndPos();
		startPos = mapgen.getStartPos();
		hero = new Hero(1,1,1,startPos);
		
//		backgroundMusic.setVolume(0.2f);
//		backgroundMusic.setLooping(true);
//		backgroundMusic.play();
		
		tile = new Rectangle();
		tile.width = 64;
		tile.height = 64;
		
		heroSprite = new Rectangle();
		heroSprite.width = 64;
		heroSprite.height = 64;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
//		System.out.println(camera.position.x + " " + camera.position.y);
		
		handleInput();
		movementUpdate();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tile.setX(0);
		tile.setY(winHeight);

		for(int y=0; y<map.getWidth(); y++) {
			tile.setX(0);
			tile.setY(tile.y-tile.getHeight());
			
			for(int x=0; x<map.getLength(); x++) {
//				// it's for old map generator version 
//				int value = map.getCell(x, y)-128;
//				batch.draw(textures.get(value), tile.x, tile.y);
				int value = map.getCell(x, y);
				batch.draw(textures2.get(value), tile.x, tile.y);
				if(startPos.getX() == x && startPos.getY() == y) {
					batch.draw(textures2.get(2), tile.x, tile.y);
				}
				if(endPos.getX() == x && endPos.getY() == y) {
					batch.draw(textures2.get(3), tile.x, tile.y);
				}
				
				if(hero.getLoc().getX() == x && hero.getLoc().getY() == y) {
					heroSprite.x = tile.x;
					heroSprite.y = tile.y;
					batch.draw(textures2.get(4), heroSprite.x, heroSprite.y);
				}
				tile.setX(tile.x + tile.getWidth());
			}
		}
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}
	
	private void movementUpdate() {
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			System.out.println("key w is pressed");
	    	hero.move(map,2);
	    	camera.position.set(heroSprite.x, heroSprite.y, 0);
			camera.update();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
	    	System.out.println("key a is pressed");
	    	hero.move(map,1);
	    	camera.position.set(heroSprite.x, heroSprite.y, 0);
			camera.update();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
	    	hero.move(map,8);
	    	camera.position.set(heroSprite.x, heroSprite.y, 0);
			camera.update();
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
	    	hero.move(map,4);
	    	camera.position.set(heroSprite.x, heroSprite.y, 0);
			camera.update();
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

	    if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
	    	if(camera.zoom < 1.30f) {
	    		camera.zoom += 0.02;
	    	}
	    }
	    if (Gdx.input.isKeyPressed(Input.Keys.X)) {
	    	if(camera.zoom > 0.60) {
	    		camera.zoom -= 0.02;
	    	}
	    }
	 }

}
