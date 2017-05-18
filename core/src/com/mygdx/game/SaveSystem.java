package com.mygdx.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SaveSystem {
	
	public static boolean saveGame(MyGdxGame game, String name) {
		Map<String, Object> data = new HashMap<String, Object>();
		Hero hero = game.getHero();
		data.put("mapWidth", game.getMAP_WIDTH());
		data.put("mapHeight", game.getMAP_HEIGHT());
		data.put("deadends", game.getDeadends());
		data.put("map", game.getMap());
		data.put("startLocs", game.getMapgen().getStartLocs());
		data.put("startPos", game.getMapgen().getStartPos());
		data.put("endPos", game.getMapgen().getEndPos());
		data.put("stat", game.getStatistics());
		data.put("bossLoc", game.getMapgen().getBossLoc());
		data.put("static", game.getStaticMonsters());
		
		// hero attributes
		data.put("hp", hero.getHp());
		data.put("inv", hero.inventory);
		data.put("slots", hero.slots);
		data.put("loc", hero.getLoc());
		data.put("weaponSlots", hero.weaponSlots);
		data.put("str", hero.getHeroStrength());
		data.put("dex", hero.getHeroDexterity());
		data.put("stam", hero.getHeroStamina());
		data.put("wrath", hero.getHeroWrath());
		data.put("potionAmount", hero.getPotionAmount());
		
		if(name.length() > 0 && name.matches("^[a-zA-Z0-9]*$")) {
			try {
		         FileOutputStream fileOut =
		         new FileOutputStream(name+ ".ser");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(data);
		         out.close();
		         fileOut.close();
		         System.out.println("Serialized data is saved in " + name + ".ser");
		         return true;
		      }catch(IOException i) {
		         i.printStackTrace();
		         return false;
		      }
		} else {
			return false;
		}
	}
	
	public static ArrayList<Object> loadGame(String path) {
		try {
	         FileInputStream fileIn = new FileInputStream(path);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         HashMap data = (HashMap) in.readObject();
	         in.close();
	         fileIn.close();
	         ArrayList<Object> heroMapgenStat = new ArrayList<Object>();
	         Hero hero = new Hero((Integer) data.get("str"),(Integer) data.get("dex"), 
	        		 (Integer) data.get("stam"), (Location) data.get("loc"), (Integer) data.get("hp"),
	        		 (List<?>[]) data.get("slots"), (List<?>[]) data.get("weaponSlots"), 
	        		 (List<Item>) data.get("inv"), (Integer) data.get("potionAmount"));
	         MapGenerator mg = new MapGenerator((Integer) data.get("mapWidth"), 
	        		 (Integer) data.get("mapHeight"), (Matrix) data.get("map"), (ArrayList<Location>) data.get("deadends"), 
	        		 (List<Location>) data.get("startLocs"), (Location) data.get("startPos"), (Location) data.get("endPos"),
	        		 (Location) data.get("bossLoc"));
	         heroMapgenStat.add(hero);
	         heroMapgenStat.add(mg);
	         heroMapgenStat.add(data.get("stat"));
	         heroMapgenStat.add(data.get("static"));
	         return heroMapgenStat;
	      }catch(IOException i) {
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c) {
	         System.out.println("HashMap class not found");
	         c.printStackTrace();
	         return null;
	      }
		
	}
}
