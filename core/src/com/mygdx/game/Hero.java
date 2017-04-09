package com.mygdx.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hero extends Character {
	private Location loc;
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int EMPTY = 0;	
	
	List<Item> inventory = new ArrayList<Item>();
	List<Headgear[]> headSlot = new ArrayList<Headgear[]>();
	List<Bodyarmor[]> torsoSlot = new ArrayList<Bodyarmor[]>();
	List<Gloves[]> armsSlot = new ArrayList<Gloves[]>();
	List<MeleeWeapon[]> weaponSlot1 = new ArrayList<MeleeWeapon[]>();
	List<Weapon[]> weaponSlot2 = new ArrayList<Weapon[]>();
	List<Legarmor[]> legsSlot = new ArrayList<Legarmor[]>();
	List<Boots[]> toesSlot = new ArrayList<Boots[]>();
	List<?>[] slots = {headSlot,torsoSlot,armsSlot,weaponSlot1, weaponSlot2, legsSlot,toesSlot}; //strange syntax, but works :)
	List<?>[] weaponSlots = {weaponSlot1, weaponSlot2};
	
	// Schema
	// List<?> -> ArrayList<GearClass[]> -> GearClass[] -> GearClassObjectReference
	
	public Hero(int strength, int dexterity, int stamina, Location loc) {
		super(strength, dexterity, stamina, "humanoid", "Hero");
		setHp(getMaxHp());
		this.loc = loc;

		// Every slot is an array of certain type with length of 1, 
		// so that it is possible to add only one item to the slot
		inventory.addAll(Arrays.asList(new MeleeWeapon("SuperStick", true),new Shield("Bronze Shield", true)));
		
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
	
	public int getInvSize() {
		return inventory.size();
	}
	
	public int getSlotsArraySize() {
		return slots.length;
	}
	
	public String getAllEquiped(int page) {
		String complex = "Character has following equipped items: ";
		
		int i = 1;
		if(page == 0) {
			for(List<?> slot : slots) {
				complex += "\n" + i + ". " + slot.get(0).getClass().getSimpleName().replace("[]", "") + ": " + ((Item[]) slot.get(0))[0];
				i++;
			}
		} else {
			for(int a = -5 + 5 * page; a < 5 * page; a++) { //if it's first page, then gives slots nr 0,1,2,3,4 // if it's second page then 5,6,7,8,9 etc...
				if(a < slots.length) {
					List<?> slot = slots[a];
					complex += "\n" + i + ". " + slot.get(0).getClass().getSimpleName().replace("[]", "") + ": " + ((Item[]) slot.get(0))[0];
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
		int num = 1;
		if(inventory.isEmpty()) {
			out = "Inventory is empty";
		} else {
			if (page == 0) {
				for(Item item : inventory) {
					out += num + ". " + item.toString() + "\n";
					num++;
				}
			} else {
				for (int i = -5 + 5 *page; i < 5 * page; i++) {
					if (i < inventory.size()) {
						out += num + ". " + inventory.get(i).toString() + "\n";
						num++;
					} else {
						break;
					}
					
				}
			}
			
		}
		return out;
	}
	
	public void unequipSlot(List<?> slot) {
		if(!slot.isEmpty()) {
			Item equippedItem = ((Item[]) slot.get(0))[0];
			if(equippedItem != null) {
				inventory.add(equippedItem);
				((Item[]) slot.get(0))[0] = null;
			}
		}			
	}
	
	public boolean isSlotEmpty(List<?> slot) {
		return  ((Item[]) slot.get(0))[0] == null;
		
	}
	
	public void equip(Item item, List slot) {
		if(isSlotEmpty(slot)) {
			((Item[]) slot.get(0))[0] = item;
		}
	}
	
	public void fillWithMeleeArray(List slot) {
		slot.clear();
		MeleeWeapon[] array = new MeleeWeapon[1];
		slot.add(array);
	}
	
	public void fillWithShieldArray(List slot) {
		slot.clear();
		Shield[] array = new Shield[1];
		slot.add(array);
	}
	
	public Item getInventoryItemFromNumber(int itemNumber) {
		int num = itemNumber-1;
		return inventory.get(num);
	}
	
	public Item getEquipmentItemFromNumber(int itemNumber) {
		int num = itemNumber-1;
		List<?> slot;
		
		if(num <= slots.length) {
			slot = slots[num];
		} else {
			slot = null;
		}
		
		if(slot != null) {
			return (Item) ((Item[]) slot.get(0))[0];
		}
		return null;
	}
	
	public boolean isTwoHanded(MeleeWeapon weapon) {
		return weapon.isTwohanded();
	}
	
	public void equipMelee(MeleeWeapon item, int slotNr) {
		inventory.remove(item);
		if(weaponSlots.length > slotNr) {
			List<?> slot = weaponSlots[slotNr];
			if(item.isTwohanded()) { // if weapon is twohanded, then both hands should be empty to equip it
				if(isSlotEmpty(weaponSlot1) & isSlotEmpty(weaponSlot2)) {
					equip(item, weaponSlot1);
				} else {
					System.out.println("Unequip everything from your hands first!");
				}
			} else {
				if(slot.equals(weaponSlot1)) {
					if(!isSlotEmpty(slot)) {
						unequipSlot(slot);
					}
					equip(item, slot);
				} else if(slot.equals(weaponSlot2)) {
					System.out.println("isempty " + isSlotEmpty(weaponSlot1));
					if(!isSlotEmpty(weaponSlot1) && ((MeleeWeapon) getItemFromSlot(weaponSlot1)).isTwohanded()) {
						unequipSlot(weaponSlot1);
					}
					if(!isSlotEmpty(slot)) {
						unequipSlot(slot);
						fillWithMeleeArray(slot);
					}
					equip(item, slot);
				}
			}
		}
	}
	
	public void equipShield(Shield item) {
		inventory.remove(item);
		if(!isSlotEmpty(weaponSlot2)) {
			unequipSlot(weaponSlot2);
			fillWithShieldArray(weaponSlot2);
		}
		equip(item, weaponSlot2);
	}
	
	public Item getItemFromSlot(List slot) {
		return ((Item[]) slot.get(0))[0];
	}
	
	public void unequip(int itemNumber) {
		int num = itemNumber-1;
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
					String arrayname = (slot.get(0).getClass().getComponentType().getSimpleName()); 
					if (arrayname != null && arrayname.equals(item.getClass().getSimpleName())) { // checking if array in the slot of type Shield\MeleeWeapon\Headgear or smth else
						foundSlot = (ArrayList) slot;
						Item[] innerSlot = (Item[]) foundSlot.get(0); // won't it throw an Exception if array is empty??? i'm not sure

						System.out.println(weaponSlot2.getClass().getTypeName());
						
						if(innerSlot.getClass().getComponentType().isAssignableFrom(item.getClass())) {
							System.out.println("It's instance of " + innerSlot.getClass());
						} else {
							System.out.println("It isn't instance of " + innerSlot.getClass());
						}
						
						if(innerSlot[0] == null) { // if doesn't have item then just add it to the slot
							System.out.println("just equipping item in empty slot");
							innerSlot[0] = inventory.get(num);
							inventory.remove(num);
						} else {
							Item slotItem = innerSlot[0];
							innerSlot[0] =  inventory.get(num);
							inventory.remove(num);
							inventory.add(slotItem); // if there is an item in slot, then put it in inventory
							
						}
						return;
					}
				}
			}
		} else {
			return;
		}
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
	
	public int getArmor() {
		return 1; //get armor from equipped items pls
	}
	
	@Override
	public String toString() {
		return "Hero [strength=" + getStrength() + ", agility=" + getDexterity() + ", intelligence=" + getStamina()
				+ ", Health=" + getHp() + ", Evasion=" + getEvasion()
				+ ", Armor=" + getArmor() + ", AttackDamage=" + getAttackDamage() + "]";
	}
	
	@Override
	public void takeDmg(int dmg, int bodypart) {
		dmg = checkIfFatalStrike(dmg, bodypart);
		int actualDmg = dmg - getArmor();
		System.out.println(getName() + " has taken " + actualDmg + " DMG");
		if (actualDmg <= hp) {
			hp -= actualDmg;
		} else {
			System.out.println("Hero is dead");
			hp = 0;
		}
	}
}

