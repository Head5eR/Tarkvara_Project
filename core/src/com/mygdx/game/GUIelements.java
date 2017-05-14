package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

// Going to use this subclass for typical graphical elements 
// which I don't want to rewrite everytime

public abstract class GUIelements {
	public static void sureAboutClosingDialog(Stage stage, Skin skin) {
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
	
	public static TextButton getBackButton(Skin skin) {
		TextButton btn = new TextButton("Back", skin);
		btn.addListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	        		 actor.getParent().setVisible(false);
	        }
	    });
		return btn;
	}
}
