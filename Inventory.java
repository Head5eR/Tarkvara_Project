package tarkvaraproject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;

public class Inventory {
	private int x, y;
	private int width, height;
	
	private int numCols = 6;
	private int numRows = 4;
	
	
	private CopyOnWriteArrayList<ItemSlot> itemSlots;
	
	public Inventory(int x, int y) {
		this.x = x;
		this.y = y;
		itemSlots = new CopyOnWriteArrayList<ItemSlot>();
		
		for (int i = 0; i < numCols; i++) {
			for (int j = 0; j < numRows; j++) {
				if (j == (numRows-1)) {
					y += 35;
				}				
				itemSlots.add(new ItemSlot(x + (i * (ItemSlot.SLOTSIZE + 10)), y + (j*(ItemSlot.SLOTSIZE)), null));
			}
		}
		
		width = numCols * (ItemSlot.SLOTSIZE + 10);
		height = numRows * (ItemSlot.SLOTSIZE + 10) + 35;
	}
	
	public void tick() {
		for(ItemSlot is: itemSlots) {
			is.tick();
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x - 17, y - 17, width + 30, height + 30);		
		g.setColor(Color.BLACK);
		g.drawRect(x - 17, y - 17, width + 30, height + 30);
		
		for (ItemSlot is: itemSlots) {
			is.render(g);
		}
	}
}

//public Inventory inventory;  (add this into the main class)
//inventory = new Inventory(80, 40);
//inventory.tick(); (check)
//inventory.render(g); (graphics)