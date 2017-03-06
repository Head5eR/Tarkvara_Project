package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameLauncher extends Game{
	public BitmapFont font;
	public SpriteBatch batch;
	
	
	@Override
	public void create() {
		batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MyGdxGame(this));
	}
	
	public void render() {
        super.render(); //important!
    }

    public void dispose() {
    	super.dispose();
        //batch.dispose();
        font.dispose();
        
    }
    
    public void resize(int width, int height) {
    	super.resize(width, height);
    }

}
