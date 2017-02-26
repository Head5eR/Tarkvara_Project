package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	private Rectangle tile;
	private Matrix map;
	private final int WIN_WIDTH = 800;
	private final int WIN_HEIGHT = 800;
	
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
		
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\FireFox\\GameProjectJava\\core\\assets\\PINK_GUY_STFU.mp3"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIN_WIDTH, WIN_HEIGHT);

		Monster monster = new Monster(707);
		System.out.println(monster);
		System.out.println(monster.getMod());
		
//		LabyrinthGenerator gen = new LabyrinthGenerator(12,10);
//		gen.generateMap();
//		map = gen.getMap();
	
		MapGenerator mapgen = new MapGenerator(20, 20);
		mapgen.generateMap();
		map = mapgen.getMap();
		
//		backgroundMusic.setVolume(0.2f);
//		backgroundMusic.setLooping(true);
//		backgroundMusic.play();
		
		tile = new Rectangle();
		tile.width = 64;
		tile.height = 64;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		handleInput();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tile.setX(20);
		tile.setY(WIN_HEIGHT-20-64);
		
		
		
		for(int y=0; y<map.getWidth(); y++) {
			tile.setX(20);
			tile.setY(tile.y-tile.getHeight());
			
			for(int x=0; x<map.getLength(); x++) {
				// it's for old map generator version 
				//int value = map.getCell(x, y)-128;
				//batch.draw(textures.get(value), tile.x, tile.y);
				int value = map.getCell(x, y);
				batch.draw(textures2.get(value), tile.x, tile.y);
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
	        camera.zoom += 0.02;
	    }
	    if (Gdx.input.isKeyPressed(Input.Keys.X)) {
	        camera.zoom -= 0.02;
	    }
	 }

}
