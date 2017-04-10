package com.mygdx.game;

import java.util.Random;

public class LootSystem {
	
	public void generateLoot(Monster monster) { //insert monster difficulty
		String difficulty = monster.getMod().getName();
		
		Random random = new Random();
		
		float x = random.nextFloat();
		
		if(difficulty.equals("weak")) {  //generate loot according to the monster's difficulty			
				//return item level 1
			
		}else if(difficulty.equals("medium")){
			if(x <= 0.7) {  //return item level 1-2
				//return lvl1 item
			}else {
				//return lvl2 item
			}
		}else if(difficulty.equals("strong")){  //return item level 1-2 (more chances to get item level 2)
			if(x <= 0.5) {  //return item level 1-2
				//return lvl1 item
			}else {
				//return lvl2 item
			}
			
		}else if(difficulty.equals("dangerous")){  //return item level 1-3
			if(x <= 0.2) {
				//return lvl1 item
			}else if(x > 0.2 & x <= 0.6) {
				//return lvl2 item
			}else {
				//return lvl3 item
			}
		}else if(difficulty.equals("deadly")){  //return item level 1-3 (high chances to get item lvl2, medium chances to get item lvl3)
			if(x <= 0.05) {
				//return lvl1 item
			}else if(x > 0.05 & x <= 0.65) {
				//return lvl2 item
			}else {
				//return lvl3 item
			}			
		}
	}
	
}

