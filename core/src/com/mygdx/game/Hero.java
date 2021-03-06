package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;

public class Hero extends Character {
	private Location loc;
	final int MOVELEFT = 1;
	final int MOVEUP = 2;
	final int MOVERIGHT = 4;
	final int MOVEDOWN = 8;
	final int EMPTY = 0;
	
	private int extraStr = 0;
	private int extraStam = 0;
	private int extraDex = 0;
	private int extraWrath = 0;
	private int extraArmor = 0;
	private int extraMinAttack = 0;
	private int extraMaxAttack = 0;
	
	private int potionAmount = 1;
	
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
	
	public Hero(int strength, int dexterity, int stamina, Location loc, int hp, 
			List<?>[] slots, List<?>[] weaponSlots, List<Item> inventory, int potionAmount) {
		super(strength, dexterity, stamina, "humanoid", "Hero");
		this.loc = loc;
		this.slots = slots;
		this.weaponSlots = weaponSlots;
		weaponSlot1 = (List<MeleeWeapon[]>) weaponSlots[0];
		weaponSlot2 = (List<Weapon[]>) weaponSlots[1];
		this.inventory = inventory;
		this.hp = hp;
		this.potionAmount = potionAmount;
		calculateStatsFromItems();
		
	}
	
	public Hero(int strength, int dexterity, int stamina, Location loc) {
		super(strength, dexterity, stamina, "humanoid", "Hero");
		this.loc = loc;

		// Every slot is an array of certain type with length of 1, 
		// so that it is possible to add only one item to the slot
		inventory.addAll(Arrays.asList(MeleeWeapon.getMeleeWeaponRandRarity("sword", "2"),Shield.getShieldRandRarity("1")));
		
		Headgear[] hs = {Headgear.getHeadgearRandRarity("1")};
			headSlot.add(hs);
		Bodyarmor[] ts = {Bodyarmor.getBodyarmorRandRarity("1")};
			torsoSlot.add(ts);
		Gloves[] as = {Gloves.getGlovesRandRarity("1")};
			armsSlot.add(as);
		MeleeWeapon[] ws1 = {MeleeWeapon.getMeleeWeaponRandRarity("axe", "1")};
			weaponSlot1.add(ws1);
		Shield[] ws2 = {Shield.getShieldRandRarity("1")};
			weaponSlot2.add(ws2);
		Legarmor[] ls = {Legarmor.getLegarmorRandRarity("1")};
			legsSlot.add(ls);
		Boots[] toes = {Boots.getBootsRandRarity("1")};
			toesSlot.add(toes);
		calculateStatsFromItems();
		setHp(getMaxHp());
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

	public ArrayList<String> getEquippedItems() {
		ArrayList<String> items = new ArrayList<String>();
		String itemName = "";
		for(List<?> slot : slots) {
			String slotItem;
			if(((Item[]) slot.get(0))[0] != null) {
				slotItem = ((Item[]) slot.get(0))[0].toString();
			} else {
				slotItem = "empty";
			}
			itemName = slot.get(0).getClass().getSimpleName().replace("[]", "") 
					+ ": " + slotItem;
			items.add(itemName);
		}
		return items;
	}
	
	public ArrayList<String> getInventoryItems(int startingItem, int lastItem) {
		ArrayList<String> items = new ArrayList<String>();
		int last;
		if(lastItem > inventory.size()) {
			last = inventory.size();
		} else {
			last = lastItem;
		}
		
		for(int i=startingItem; i<last; i++) {
			items.add(inventory.get(i).toString());
		}
		return items;
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
		if(itemNumber < inventory.size()) {
			return inventory.get(itemNumber);
		} 
		return null;
	}
	
	public Item getEquipmentItemFromNumber(int itemNumber) {
		List<?> slot;
		
		if(itemNumber < slots.length) {
			slot = slots[itemNumber];
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
		if(weaponSlots.length > slotNr) {
			List<?> slot = weaponSlots[slotNr];
			if(item.isTwohanded()) { // if weapon is twohanded, then both hands should be empty to equip it
				if(isSlotEmpty(weaponSlot1) && isSlotEmpty(weaponSlot2)) {
					equip(item, weaponSlot1);
					inventory.remove(item);
				} else {
					System.out.println("Unequip everything from your hands first!");
				}
			} else {
				if(slot.equals(weaponSlot1)) {
					if(!isSlotEmpty(slot)) {
						unequipSlot(slot);
					}
					equip(item, slot);
					inventory.remove(item);
				} else if(slot.equals(weaponSlot2)) {
					System.out.println("isempty " + isSlotEmpty(weaponSlot1));
					if(!isSlotEmpty(weaponSlot1) && ((MeleeWeapon) getItemFromSlot(weaponSlot1)).isTwohanded()) {
						unequipSlot(weaponSlot1);
					}
					if(!isSlotEmpty(slot)) {
						unequipSlot(slot);
					}
					fillWithMeleeArray(slot);
					equip(item, slot);
					inventory.remove(item);
				}
			}
		}
		
	}
	
	public void equipShield(Shield item) {
		
		if(!isSlotEmpty(weaponSlot2)) {
			unequipSlot(weaponSlot2);
		}
		if(!isSlotEmpty(weaponSlot1)) {
			MeleeWeapon weapon = (MeleeWeapon) getItemFromSlot(weaponSlot1);
			if(weapon.isTwohanded()) {
				unequipSlot(weaponSlot1);
			}
		}
		fillWithShieldArray(weaponSlot2);
		equip(item, weaponSlot2);
		inventory.remove(item);
	}
	
	public Item getItemFromSlot(List slot) {
		return ((Item[]) slot.get(0))[0];
	}
	
	public void unequip(int itemNumber) {
		List<?> slot;
		
		if(itemNumber < slots.length) {
			slot = slots[itemNumber];
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
	
	public void equipFromInv(int itemNumber) { //is used only for armor items :D look MyGdxGame.chooseEquipMethod() for clarifications
		if(inventory.size() >= itemNumber) { // if larger than inventory size then requested item doesn't exist
			Item item = (inventory.get(itemNumber));
			ArrayList<?> foundSlot;
			
			if(item.isEquipable()) {
				for (List<?> slot : slots) {
					String arrayname = (slot.get(0).getClass().getComponentType().getSimpleName()); 
					if (arrayname != null && arrayname.equals(item.getClass().getSimpleName())) { // checking if array in the slot of type Shield\MeleeWeapon\Headgear or smth else
						foundSlot = (ArrayList) slot;
						Item[] innerSlot = (Item[]) foundSlot.get(0); // won't it throw an Exception if array is empty??? i'm not sure

						if(innerSlot[0] == null) { // if doesn't have item then just add it to the slot
							innerSlot[0] = inventory.get(itemNumber);
							inventory.remove(itemNumber);
						} else {
							Item slotItem = innerSlot[0];
							innerSlot[0] =  inventory.get(itemNumber);
							inventory.remove(itemNumber);
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
		return 0 + extraArmor;
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
		int actualDmg = (int) Math.round(dmg - getArmor()*0.1);
		//System.out.println(getName() + " has taken " + actualDmg + " DMG");
		if (actualDmg > 0  && actualDmg < hp) {
			hp -= actualDmg;
		} else if (actualDmg > hp){
			System.out.println("Hero is dead");
			hp = 0;
		}
	}
	
	public void calculateStatsFromItems() { // sry for indian code, Martin, pls don't kill me :c
		extraStr = 0;
		extraStam = 0;
		extraDex = 0;
		extraWrath = 0;
		extraArmor = 0;
		extraMinAttack = 0;
		extraMaxAttack = 0;
		for(List<?> slot : slots) {
			if(!isSlotEmpty(slot)) {
				Item item = getItemFromSlot(slot);
				if(item.checkIfWeapon(item)) {
					Weapon wep = (Weapon) item; 
					extraStr += wep.getStrength();
					extraStam += wep.getStamina();
					extraDex += wep.getDexterity();
					extraWrath += wep.getWrath();
					extraMinAttack += wep.getMinDamage();
					extraMaxAttack += wep.getMaxDamage();
					if(wep.checkIfShield(item)) {
						Shield shl = (Shield) wep;
						extraArmor += shl.getArmor();
					}
				} else if (Item.isArmorItem(item)) {
					Armor arm = (Armor) item; 
					extraStr += arm.getStrength();
					extraStam += arm.getStamina();
					extraDex += arm.getDexterity();
					extraWrath += arm.getWrath();
					extraArmor += arm.getArmor();
				}
			}
 		}
		if(getHp() > getMaxHp()) {
			hp = getMaxHp();
		}
	}
	
	public int getPotionAmount() {
		return potionAmount;
	}		
	
	public void setPotionAmount(int potionAmount) {
		this.potionAmount = potionAmount;
	}

	public void restoreHp() {
		if(potionAmount > 0 &&  getHp() < getMaxHp()) {
			potionAmount -= 1;
			setHp(getHp() + (int) Math.round(0.3 * getMaxHp()));
			if(getHp() > getMaxHp()) {
				setHp(getMaxHp());
			}
		}
	}
	
	public int getHeroStrength() {
		return super.getStrength();
	}
	
	public int getHeroDexterity() {
		return super.getDexterity();
	}
	
	public int getHeroStamina() {
		return super.getStamina();
	}
	
	public int getHeroWrath() {
		return super.getWrath();
	}
	
	@Override
	public int getStrength() {
		return getHeroStrength() + extraStr;
	}
	@Override
	public int getDexterity() {
		return getHeroDexterity() + extraDex;
	}
	@Override
	public int getStamina() {
		return getHeroStamina() + extraStam;
	}
	@Override
	public int getWrath() {
		return getHeroWrath() + extraWrath;
	}
	
	@Override
	public int getMaxHp() {
		return getStamina()*10;
	}
	
	@Override
	public int getHp() {
		return hp;
	}
	
	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	@Override
	public int getMinAttackDamage() {
		return (int) Math.round(getMaxAttackDamage() * 0.7) + extraMinAttack;
	}
	
	@Override
	public int getMaxAttackDamage() {
		return (int) Math.round(getStrength()*3) + extraMaxAttack;
	}
	
	@Override
	public int getAttackDamage() {
		return MathUtils.random(getMinAttackDamage(), getMaxAttackDamage());
	}
	
	@Override
	public String getAttackDamageStr() {
		return this.getMinAttackDamage() + "-" + this.getMaxAttackDamage();
	}
}

