package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

public class Hero {
	private int strength;
	private int agility;
	private int intelligence;
	private Location loc;
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int EMPTY = 0;
	
	List<Item> inventory = new ArrayList<Item>();
	List<Item[]> headSlot = new ArrayList<Item[]>();
	List<Bodyarmor[]> torsoSlot = new ArrayList<Bodyarmor[]>();
	List<Gloves[]> armsSlot = new ArrayList<Gloves[]>();
	List<Legarmor[]> legsSlot = new ArrayList<Legarmor[]>();
	List<Boots[]> toesSlot = new ArrayList<Boots[]>();
	List<?>[] slots = {headSlot,torsoSlot,armsSlot,legsSlot,toesSlot}; //strange syntax, but works :)
	
	// Schema
	// List<?> -> ArrayList<GearClass[]> -> GearClass[] -> GearClassObject
	
	public Hero(int strength, int agility, int intelligence, Location loc) {
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.loc = loc;
		
		// Every slot is an array of certain type with length of 1, 
		// so that it is possible to add only one item to the slot
		Headgear[] hs = {new Headgear("Helmet", true)};
			headSlot.add(hs);
		Bodyarmor[] ts = {new Bodyarmor("Chainmail", true)};
			torsoSlot.add(ts);
		Gloves[] as = {new Gloves("Gauntlets", true)};
			armsSlot.add(as);
		Legarmor[] ls = {new Legarmor("Pants", true)};
			legsSlot.add(ls);
		Boots[] toes = {new Boots("High boots", true)};
			toesSlot.add(toes);
	}
	
	public String getAllEquiped() {
		return "Character has following equipped items: \n 1.Head: " + headSlot.get(0)[0] + "\n 2.Torso: " 
				+ torsoSlot.get(0)[0] + " \n 3.Arms: " + armsSlot.get(0)[0] + " \n 4.Legs: " + legsSlot.get(0)[0] 
				+ "\n 5.Toes: " + toesSlot.get(0)[0];
	}
	
	public String getInventory() {
		String out = "";
		if(inventory.isEmpty()) {
			out = "Inventory is empty";
		} else {
			for(Item item : inventory) {
				out += item.toString() + "\n";
			}
		}
		return out;
	}
	
	public void unequip(int itemNumber) {
		int num = itemNumber-1;
		System.out.println("slot number: " + num);
		// BEWARE, INDIAN CODE INCOMING!
		List<?> slot = null;
		switch(num) {
			case 0: { slot = headSlot; break;}
			case 1: { slot = torsoSlot; break;}
			case 2: { slot = armsSlot; break;}
			case 3: { slot = legsSlot; break;}
			case 4: { slot = toesSlot; break;}
			
		}
		if(slot != null) {
			Item item = (Item) ((Item[]) slot.get(0))[0];
			System.out.println("unequipping " + item);
			if (item != null) {
				inventory.add(item);
				((Item[]) slot.get(0))[0] = null;
			}
		}
		
	}
	
	public void equipFromInv(int itemNumber) {
		int num = itemNumber-1;
		if(inventory.size() >= itemNumber) { // if larger than inventory size then requested item doesn't exist
			Item item = (inventory.get(num));
			ArrayList<?> foundSlot;
			
			if(item.isEquipable()) {
				for (List<?> slot : slots) {
					String arrayname = (slot.get(0).getClass().getSimpleName().split("\\["))[0];
					if (arrayname != null && arrayname.equals(item.getClass().getSimpleName())) {
						foundSlot = (ArrayList) slot;
						Item[] innerSlot = (Item[]) foundSlot.get(0);
						System.out.println(foundSlot.get(0));
						
						if(innerSlot[0] == null) { // if doesn't have item then just add it to the slot
							System.out.println("just equipping slot");
							innerSlot[0] = inventory.get(num);
							inventory.remove(num);
						} else {
							inventory.add(innerSlot[0]); // if there is an item in slot, then put it in inventory
							innerSlot[0] =  inventory.get(num);
						}
						return;
					}
				}
			}
		} else {
			return;
		}
	}
	
	public int getHealth() {
		return strength * 18;
	}
	
	public int getMana() {
		return intelligence * 13;
	}
	
	public float getEvasion() {
		return (float) (agility*intelligence*0.1);
	}
	
	public int getArmor() {
		return (int) (agility*0.5);
	}
	
	public int getAttackDamage() {
		return (int) (strength*agility*0.2);
	}
	
	public int getWrath() {
		return (int) (strength*intelligence*0.02);
	}

	@Override
	public String toString() {
		return "Hero [strength=" + strength + ", agility=" + agility + ", intelligence=" + intelligence
				+ ", getHealth()=" + getHealth() + ", getMana()=" + getMana() + ", getEvasion()=" + getEvasion()
				+ ", getArmor()=" + getArmor() + ", getAttackDamage()=" + getAttackDamage() + ", getWrath()="
				+ getWrath() + "]";
	}
	
	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	public void move(Matrix map, int direction) {
		switch(direction){
			case MOVELEFT: { if((loc.getX() > 0) && (map.getCell(loc.getX()-1, loc.getY()) == EMPTY)) {loc = loc.goLeft(1); } break;} 
			case MOVEUP: { if(loc.getY() > 0 && (map.getCell(loc.getX(), loc.getY()-1) == EMPTY)) {loc = loc.goUp(1); } break;} 
			case MOVERIGHT: { if(map.getLength()-1 > loc.getX() && (map.getCell(loc.getX()+1, loc.getY()) == EMPTY)) {loc = loc.goRight(1); } break;}
			case MOVEDOWN: { if(map.getWidth()-1 > loc.getY() && (map.getCell(loc.getX(), loc.getY()+1) == EMPTY)) {loc = loc.goDown(1); } break;} 
		}
			
	}
}
