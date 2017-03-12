package robertsInvVer;

import java.awt.Color;
import java.awt.Graphics;

public class ItemSlot {
	public static final int SLOTSIZE = 50;
	
	private int x, y;
	private ItemStack itemStack;
	
	public ItemSlot(int x, int y, ItemStack itemStack) {
		this.x = x;
		this.y = y;
		this.itemStack = itemStack;
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x, y, SLOTSIZE, SLOTSIZE);
		
		g.setColor(Color.BLACK);
		g.drawRect(x, y, SLOTSIZE, SLOTSIZE);
	}
	
	public ItemStack getitemStack() {
		return itemStack;
	}
	
	public boolean addItem(Item item, int amount) {
		if(itemStack != null) {
			if(item.getItemType()==itemStack.getItem().getItemType()) {
				this.itemStack.setAmount(this.itemStack.getAmount() + amount);
				return true;
			}else {
				return false;
			}
		}else {
			this.itemStack = new ItemStack(item, amount);
			return true;
		}
	}
}
