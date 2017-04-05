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
	private Body body;
	
	List<Item> inventory = new ArrayList<Item>();
	List<Headgear[]> headSlot = new ArrayList<Headgear[]>();
	List<Bodyarmor[]> torsoSlot = new ArrayList<Bodyarmor[]>();
	List<Item[]> armsSlot = new ArrayList<Item[]>();
	List<MeleeWeapon[]> weaponSlot1 = new ArrayList<MeleeWeapon[]>();
	List<Weapon[]> weaponSlot2 = new ArrayList<Weapon[]>();
	List<Legarmor[]> legsSlot = new ArrayList<Legarmor[]>();
	List<Boots[]> toesSlot = new ArrayList<Boots[]>();
	List<?>[] slots = {headSlot,torsoSlot,armsSlot,weaponSlot1, weaponSlot2, legsSlot,toesSlot}; //strange syntax, but works :)
	
	// Schema
	// List<?> -> ArrayList<GearClass[]> -> GearClass[] -> GearClassObject
	
	public Hero(int strength, int agility, int intelligence, Location loc) {
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
		this.loc = loc;
		body = new Body("humanoid");
		
		// Every slot is an array of certain type with length of 1, 
		// so that it is possible to add only one item to the slot
		Headgear[] hs = {new Headgear("Helmet", true)};
			headSlot.add(hs);
		Bodyarmor[] ts = {new Bodyarmor("Chainmail", true)};
			torsoSlot.add(ts);
		Gloves[] as = {new Gloves("Gauntlets", true)};
			armsSlot.add(as);
		MeleeWeapon[] ws1 = {new MeleeWeapon("Stick", true)};
			weaponSlot1.add(ws1);
		Shield[] ws2 = {new Shield("Wooden Shield", true)};
			weaponSlot2.add(ws2);
		Legarmor[] ls = {new Legarmor("Pants", true)};
			legsSlot.add(ls);
		Boots[] toes = {new Boots("High boots", true)};
			toesSlot.add(toes);
	}
	
	public String getAllEquiped(int page) {
		String complex = "Character has following equipped items: ";
		
		int i = 1;
		if(page == 0) {
			for(List<?> slot : slots) {
				complex += "\n" + i + ". " + slot.get(0).getClass().getSimpleName() + ": " + ((Item[]) slot.get(0))[0];
				i++;
			}
		} else {
			for(int a = -5 + 5 * page; a < 5 * page; a++) { //if it's first page, then gives slots nr 0,1,2,3,4 // if it's second page then 5,6,7,8,9 etc...
				if(a < slots.length) {
					List<?> slot = slots[a];
					complex += "\n" + i + ". " + slot.get(0).getClass().getSimpleName() + ": " + ((Item[]) slot.get(0))[0];
					i++;
				} else {
					break;
				}
				
			}
		}
		
		
		return complex;
	}
	
	public String getInventory(int page) {
		String out = "";
		if(inventory.isEmpty()) {
			out = "Inventory is empty";
		} else {
			if (page == 0) {
				for(Item item : inventory) {
					out += item.toString() + "\n";
				}
			} else {
				for (int i = -5 + 5 *page; i < 5 * page; i++) {
					if (i < inventory.size()) {
						System.out.println(i);
						out += inventory.get(i).toString() + "\n";
					} else {
						break;
					}
					
				}
			}
			
		}
		return out;
	}
	
	public void unequip(int itemNumber) {
		int num = itemNumber-1;
		System.out.println("slot number: " + num);
		List<?> slot;
		
		if(num <= slots.length) {
			slot = slots[num];
		} else {
			slot = null;
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
		return (int) Math.round(agility*0.5);
	}
	
	public int getAttackDamage() {
		return (int) Math.round(strength*agility*0.2);
	}
	
	public int getWrath() {
		return (int) Math.round(strength*intelligence*0.02);
	}
	
	public int getInvSize() {
		return inventory.size();
	}
	
	public int getSlotsArraySize() {
		return slots.length;
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
