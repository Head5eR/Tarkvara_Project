package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
		//textures.add()
		for(int i=0; i<15; i++) {
			String path = "tile texture_" + i + ".png";
			textures.add(new Texture(path));
		}
		
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\FireFox\\GameProjectJava\\core\\assets\\PINK_GUY_STFU.mp3"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIN_WIDTH, WIN_HEIGHT);
		
		LabyrinthGenerator gen = new LabyrinthGenerator(10,10);
		gen.generateMap();
		map = gen.getMap();
		
		//backgroundMusic.setVolume(0.2f);
		//backgroundMusic.setLooping(true);
		//backgroundMusic.play();
		
		tile = new Rectangle();
		tile.width = 64;
		tile.height = 64;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tile.setX(20);
		tile.setY(WIN_HEIGHT-20-64);
		
		
		
		for(int i=0; i<map.getWidth(); i++) {
			tile.setX(20);
			tile.setY(tile.y-tile.getHeight());
			for(int j=0; j<map.getHeight(); j++) {
				int value = map.getCell(j, i)-128;
				batch.draw(textures.get(value), tile.x, tile.y);
				tile.setX(tile.x + tile.getWidth());
			}
		}
		
		//batch.draw(tile0, tile.x, tile.y);
		//batch.draw(img, 0, 0);
		//batch.draw(tile0, 700, 400);
		
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
