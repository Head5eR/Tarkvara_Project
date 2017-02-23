package ee.ttu.tarkvara.indiebricks.hero;

public class Hero {
	private int strength;
	private int agility;
	private int intelligence;
	
	public Hero(int strength, int agility, int intelligence) {
		this.strength = strength;
		this.agility = agility;
		this.intelligence = intelligence;
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
}
