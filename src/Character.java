import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.File;
import javax.sound.sampled.*;

public class Character implements java.io.Serializable{
private static final long serialVersionUID = 2L;
private String name;
private int strength, agility, intelligence, stamina, maxStr,maxAgi,maxInt,maxSta; //core stats
private int progress;
private int level;  
private int gold;
private int str_bonus;  //str bonus if equipped weapons
private boolean back;
private double HP;
private double maxHP;
private double MP;
private double maxMP;
private int SP;			//skillpoints
private int exp;
private double maxXP;   
private boolean inFight;   //character fight status
private String gender;
private String area;	//current area, not implemented

ArrayList<Item> equips = new ArrayList<>();
ArrayList<Item> shopItem = new ArrayList<>();
HashMap<Integer,String> spellMap = new HashMap<>();	//maps spells to the buttons that use them
/*spells 
 * 1. heal
 * 2. ice
 * 3. poison
 * 4. healmore
 */
ArrayList<Item> inventory = new ArrayList<>();
//items
Item spot = new Item(1);
Item smpot  = new Item(2);
Item bmb = new Item(3);
Item fsh = new Item(4);
Item shrk = new Item(5);
Item bait = new Item(6);


Random rad;
Scanner sc;
//sound files
File enemyHit = new File("files/472.wav");  //enemy takes damage
File heal = new File("files/138.wav"); //play when heal,	
File healmore = new File("files/121.wav"); //healmore
File water = new File("files/212.wav");   //aqua blast
File lightning = new File("files/217.wav"); //lightning 
File lava = new File("files/134.wav"); //lava 
File bomb = new File("fiels/133.wav"); //bomb
File ultraheal = new File("files/120.wav");	//ultraheal




public Character() {
		
}

public Character(int str, int agi,int intel, int sta) {  
		exp = 0;
		level = 1;
		this.strength = str;
		maxStr= str;
		this.agility = agi;
		maxAgi = agi;
		this.intelligence = intel;
		maxInt = intel;
		this.stamina = sta;
		maxSta = sta;
		maxXP = 4;
		str_bonus = 1;
		gold = 25;
		HP = 5+stamina;
		maxHP = HP;
		MP = 5+intelligence;
		maxMP = MP;
		SP = 0;
		addSpell(1,"heal");
		addSpell(7,"back");
}

//Mutators
public String getName() {
	return name;
}
public int getStr() {
	return strength;
}
public int getAgi() {
	return agility;
}
public int getInt() {
	return intelligence;
}
public int getSta() {
	return stamina;
}
public int getMaxStr() {
	return maxStr;
}
public int getMaxAgi() {
	return maxAgi;
}
public int getMaxInt() {
	return maxInt;
}
public int getMaxSta() {
	return maxSta;
}
public String getGen() {
	return gender;
}
public double getHP() {
	return HP;
}
public double getMaxHP() {
	return maxHP;
}
public double getMP() {
	return MP;
}
public double getMaxMP() {
	return maxMP;
}
public int getSP() {
	return SP;
}
public int getXP() {
	return exp;
}
public int getLevel() {
	return level;
}
public double getMaxXP() {
	return maxXP;
}
public int getGold() {
	return gold;
}
public boolean getBack() {
	return back;
}
public String getArea() {
	return area;
}
public int getProgress() {
	return progress;
}
public boolean getFightStatus() {
	return inFight;
}
public Item getItem(int i) {
	return inventory.get(i);
}
public void getSpells(){    //print spells
	for(HashMap.Entry<Integer,String> set: spellMap.entrySet()) {
		System.out.print(set.getKey() +". "+ set.getValue()+"\t");
	}
}

public void getItems() {
	for(int i=0;i<inventory.size();i++) {
		Item item = inventory.get(i);
		System.out.print(item.getID()+"." + item.getName() + ": " + item.getQuantity() +"\t");
		if(i%2==1) {
			System.out.println();
		}
	}
	System.out.println("7. Back");
}


//setters
public void setName(String name) {
	this.name = name;
}
public void setStr(int str) {
	strength = str;
}
public void setAgi(int agi) {
	agility = agi;
}
public void setInt(int intel) {
	intelligence = intel;
}
public void setSta(int sta) {
	stamina = sta;
}
public void setMaxStr(int a) {
	maxStr = a;
}
public void setMaxAgi(int a) {
	maxAgi = a;
}
public void setMaxInt(int a) {
	maxInt = a;
}
public void setMaxSta(int a) {
	maxSta = a;
}
public void setGen(String gen) {
	gender = gen;
}
public void setHP(double hp) {
	HP = hp;
}
public void setMaxHP(double hp) {
	maxHP = hp;
}
public void setMP(double mp) {
	MP = mp;
}
public void setMaxMP(double mp) {
	maxMP = mp;
}
public void setSP(int sp) {
	SP = sp;
}
public void setGold(int g) {
	gold = g;
}
public void setLevel(int l) {
	level = l;
}
public void setXP(int xp) {
	exp = xp;
}
public void setMaxXP(double xp) {
	maxXP = xp;
}
public void setBack(boolean b) {
	back= b;
}
public void setFightStatus(boolean b) {
	inFight = b;
}
public void setProgress(int p) {
	progress = p;
}
public void setArea(String area) {
	this.area=area;
}
public void addSpell(Integer i, String spell) {
	spellMap.put(i,spell);
}
public void addInventory(Item item) {
inventory.add(item);
}
public void addEquip(Item item) {
	equips.add(item);
}
public void removeEquip(Item item) {
	equips.remove(item);
}
public void getEquips() {
	int i=0;
	for(Item item: equips) {
		System.out.println(item + "\t");
		if(i%2==1) {
			System.out.println();
		}
		i++;
	}
}
//fight functions



	public double getMax() { //gets maximum possible hit
		double max_hit = Math.round(1 + strength*((str_bonus+64.0)/256.0));
		return max_hit;
	}
	
	public double getHit(Random rad ) {  //gets hit between 0 and max
		double max = getMax();
		double db = rad.nextDouble();
		double hit = Math.round(db*(Math.pow(1.015, agility))*max);
		if(hit>max) {hit = max;}
		return hit;
	}
	
	public void Attack(Entity e,Random rad) {
		double hit = getHit(rad);
		e.setHP(e.getHP() - hit);
		Main.playSound(enemyHit);
		System.out.println("You slash "+e.getName() + " for "+String.format("%.2f",hit)+" damage.");
	}
	public void castSpell(Entity e, Scanner sc) {  
		getSpells(); //display spells
		int choice = sc.nextInt();
		if(spellMap.containsKey(choice)) {  //if selected spell is valid
			String spell = spellMap.get(choice);
			switch(spell) {
			case "heal":
				heal();
				break;
			case "ice shards":
				ice1(e);
				break;
			case "back":
				setBack(true);
				return;
			default:
				break;
			}	
		}
		
		else {
			System.out.println("Invalid input or you don't have this spell!");
		}
		
	}
	private void useHeal(Item item) {
		if(HP+item.getHeal()>(maxHP)) {
			setHP(maxHP);
		}
		else {
			setHP(HP+item.getHeal());
		}
		item.setQuantity(item.getQuantity()-1); ;
		System.out.println("You drink from the "+item.getName()+". You heal "+item.getHeal()+ " HP");
	}
	private void useMHeal(Item item) {
		if(MP+item.getHeal()>(maxMP)) {
			setMP(maxMP);
		}
		else {
			setMP(MP+item.getMHeal());
		}
		item.setQuantity(item.getQuantity()-1); ;
		System.out.println("You drink from the "+item.getName()+". You recover "+item.getMHeal()+" MP");
	}
	
	private void useDamage(Item item,Entity e) {
		double damage = rad.nextDouble()*item.getDamage();
		e.setHP(e.getHP()-damage);
		item.setQuantity(item.getQuantity()-1);
		System.out.println("You toss a "+item.getName()+" at "+e.getName()+". It does ummm "+damage+" damage");
		Main.playSound(bomb);
	}
	private void useError() {
		System.out.println("Can't use this item.");
	}
	
	public void useItem1(Entity e,Scanner sc) {
		getItems();
		int choice = sc.nextInt();
		switch(choice) {
		case 1:
			if(spot.getQuantity()<1) {
				System.out.println("No "+spot.getName()+"s left.");
				setBack(true);
				break;
			}
			useHeal(spot);
			break;
		case 2:
			if(smpot.getQuantity()<1) {
				System.out.println("No "+smpot.getName()+"s left.");
				setBack(true);
				break;
			}
			useMHeal(smpot);
			break;
		case 3:
			if(bmb.getQuantity()<1) {
				System.out.println("No "+bmb.getName()+"s left.");
				setBack(true);
				break;
			}
			useDamage(bmb,e);
			break;
		case 4:
			if(fsh.getQuantity()<1) {
				System.out.println("No "+fsh.getName()+"s left.");
				setBack(true);
				break;
			}
			useHeal(fsh);
			break;
		case 5:
			if(shrk.getQuantity()<1) {
				System.out.println("No "+shrk.getName()+"s left.");
				setBack(true);
				break;
			}
			useHeal(shrk);
			break;
		case 6:
			useError();
			setBack(true);
			break;
		case 7:
			setBack(true);
			break;
		default:
			break;
		}
	}
	public void incQuantity(Item item) {
		item.setQuantity(item.getQuantity()+1);
	}
	public void decQuantity(Item item) {
		item.setQuantity(item.getQuantity()-1);
	}
	
	public Item getShopItem(int i) {
		return shopItem.get(i-1);
	}
	
	public void initializeItems() {
		addInventory(spot);
		spot.setQuantity(1);
		spot.setName("small potion");
		spot.setHeal(5);
		spot.setCost(5);
		shopItem.add(spot);
		addInventory(smpot);
		smpot.setQuantity(1);
		smpot.setCost(5);
		smpot.setName("small mana potion");
		smpot.setMHeal(5);
		shopItem.add(smpot);
		addInventory(bmb);
		bmb.setCost(10);
		bmb.setQuantity(1);
		bmb.setName("bomb");
		bmb.setDamage(8);
		shopItem.add(bmb);
		addInventory(fsh);
		fsh.setQuantity(0);
		fsh.setName("fish");
		addInventory(shrk);
		shrk.setQuantity(0);
		shrk.setName("shark");
		addInventory(bait);
		bait.setCost(3);
		bait.setQuantity(5);
		bait.setName("bait");
		shopItem.add(bait);
		Item sci = new Item();
		sci.setName("bronze scimitar");
		sci.setCost(25);
		sci.setQuantity(0);
		shopItem.add(sci);
		Item gun = new Item();
		gun.setName("flintlock");
		gun.setCost(40);
		gun.setQuantity(0);
		shopItem.add(gun);
		
	}
	
	
	public double guard() { //returns damage multiplier for guard
		double multiplier = .5+(.05*stamina);
		return multiplier;
	}
	
	public boolean run(Entity e,Random rad) {  //returns if run is successful
		double runChance = (level/e.getLevel()-0.4)*100;
		double threshold = 100 - runChance;
		int rand = rad.nextInt(100);
		return rand>threshold;
	}
	
	//spells
	
	public void heal() {  
		if(MP<4) {
			System.out.println("Not enough MP.");
			return;
		}
		double health = 1.5+(0.5*intelligence);
		setHP(HP+ health);
		if(HP>maxHP) {
			HP=maxHP;
		}
		useMP(4);
		Main.playSound(heal);
		System.out.println("You heal for "+health + " HP.");
	}
	
	public void ice1(Entity e) {
		if(MP<4) {
			System.out.println("Not enough MP.");
			return;
		}
		double damage = 1.5+(0.5*intelligence);
		useMP(4);
		e.setHP(e.getHP() - damage);
		Main.playSound(water);
		System.out.println("You propel ice shards at "+e.getName()+", hitting for "+ String.format("%.2f",damage)+" damage.");
	}
	public void lightning(Entity e) {
		if(MP<5) {
			System.out.println("Not enough MP.");
			return;
		}
		double damage = 2.0+(0.52*intelligence);
		useMP(5);
		e.setHP(e.getHP() - damage);
		Main.playSound(lightning);
		System.out.println();
	}
	public void poison(Entity e) {
		if(MP<4) {
			System.out.println("Not enough MP.");
			return;
		}
		double damage = 4.0+(0.52*intelligence);
		useMP(4);
		e.setHP(e.getHP() - damage);
		Main.playSound(lightning);
		System.out.println();
	}
	public void healmore() {
		if(MP<6) {
			System.out.println("Not enough MP.");
			return;
		}
		double health = 2.5+(0.65*intelligence);
		setHP(HP + health);
		if(HP>maxHP) {
			HP = maxHP;
		}
		useMP(6);
		Main.playSound(healmore);
		System.out.println("You heal for "+health+" HP.");
		
	}
	
	public boolean isDead() {  //check if char is dead
		return HP<=0;
	}
	
	public void useMP(int mp) {
		MP-=mp;
		if(MP<0) {
			MP = 0;
		}
	}

	//post-fight
	public void levelUp() {  //level up
		clearDebuffs();
		System.out.println("Congrats! ... You've leveled up!");
		Main.sleep(1);
		System.out.println("Level: "+ level+"-> "+ ++level);
		System.out.println("Stamina: "+ stamina+"-> "+ ++stamina);
		System.out.println("Health and Mana restored!");
		maxSta++;
		HP = stamina + 5;
		maxHP++;
		MP = intelligence + 5;
		maxXP= Math.pow(maxXP, 1.2); //increase max xp
		SP++;
		exp = 0;  //reset current xp
		if(level%5==0) {  //extra skill point every 5 levels
			SP++;
		}
		
	}
	public void spendSP(Scanner sc) {
		int choice = 0;
		while(choice!=5) {
			if (SP==0) {
				System.out.println("Out of SP. Level up to earn some more!");
				return;
			}
		System.out.println("Allocate a point into...?\n1. Strength\n2. Agility\n3. Intelligence\n4. Stamina\n5. Nevermind");
		choice = sc.nextInt();
		switch(choice) {
		case 1:
			strength++;
			maxStr++;
			SP--;
			System.out.println("Strength increased by 1 -> " + maxStr);
			break;
		case 2:
			agility++;
			SP--;
			maxAgi++;
			System.out.println("Agility increased by 1 -> " + maxAgi);
			break;
		case 3:
			intelligence++;
			maxInt++;
			SP--;
			System.out.println("Intelligence increased by 1 -> " + maxInt);
			break;
		case 4:
			stamina++;
			maxSta++;
			HP++;
			maxHP++;
			SP--;
			System.out.println("Stamina increased by 1 -> " + maxSta);
			break;
		case 5:
			return;
		default:
		}
		}
	}
	
	public void clearDebuffs() {
		if(strength==maxStr && agility==maxAgi && intelligence==maxInt && stamina==maxSta) {
			return;
		}
		strength = maxStr;
		agility = maxAgi;
		intelligence = maxInt;
		stamina = maxSta;
	}

	//Miscellanious functions
	
	public String proSub() {  //returns subjective pronoun based on character's gender
		if (gender == "male") {return "He";}
		else  {return "She";}
				
		}
	
	public String proObj() {   //returns objective pronoun based on character's gender
		if (gender == "male") {return "him";}
		else {return "her";}
	}
	
	public void printStats() {  //prints character stats
		System.out.println("Stats: Gold: "+gold+"\nStrength: "+strength+"\nAgility: "+agility+"\nIntelligence: "+intelligence+"\nStamina: "+stamina+"\nHP: "+String.format("%.2f",HP)+"\nMP: "+String.format("%.2f",MP));
	}
	
}

