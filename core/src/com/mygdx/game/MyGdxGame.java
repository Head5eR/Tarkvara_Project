package com.mygdx.game;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame implements Screen {
	private final GameLauncher game;
	private ArrayList<Texture> textures2 = new ArrayList<Texture>();
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
	
	public MyGdxGame (final GameLauncher game) {
		this.game = game;

		textures2.add(new Texture("tile texture_0.png"));
		textures2.add(new Texture("wall texture.png"));
		textures2.add(new Texture("start.png"));
		textures2.add(new Texture("exit.png"));
		textures2.add(new Texture("stick.png"));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 800);

		mapgen = new MapGenerator(10,14, true, true);
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
	}
	
	private void movementUpdate() {
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
	    	hero.move(map,2);
	    	System.out.println(new Monster());
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
	    	hero.move(map,1);
	    	System.out.println(new Monster());
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
	    	hero.move(map,8);
	    	System.out.println(new Monster());
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
	    	hero.move(map,4);
	    	System.out.println(new Monster());
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
	    	System.out.println(hero.getAllEquiped());
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
	    	System.out.println(hero.getInventory());
	    }
	    if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
	    	 mapgen = new MapGenerator(10,14, true, true);
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
