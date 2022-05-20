import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.File;
import javax.sound.sampled.*;

public class Character implements java.io.Serializable{
private int strength, agility, intelligence, stamina, maxStr,maxAgi,maxInt,maxSta; //core stats
private int level;  
private int gold;
private int str_bonus;  //str bonus if equipped weapons
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

HashMap<Integer,String> spellMap = new HashMap<>();	//maps spells to the buttons that use them
HashMap<Integer,String> itemMap = new HashMap<>();	//maps items to the buttons that use them
LinkedHashMap<String,Integer> inventory = new LinkedHashMap<>();	//map of items and their quantity
ArrayList<String> validItems = new ArrayList<>();	//items that have at least 1 quantity
Random rad;
String name;
//sound files
File enemyHit = new File("files/472.wav");  //enemy takes damage
File heal = new File("files/138.wav"); //play when heal
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
		gold = 0;
		HP = 5+stamina;
		maxHP = HP;
		MP = 5+intelligence;
		maxMP = MP;
		SP = 0;
		addSpell(1,"heal");
}

//Mutators
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
public double getMaxXP() {
	return maxXP;
}
public String getArea() {
	return area;
}
public boolean getFightStatus() {
	return inFight;
}
public void getSpells(){    //print spells
	for(HashMap.Entry<Integer,String> set: spellMap.entrySet()) {
		System.out.print(set.getKey() +". "+ set.getValue()+"\t");
	}
}
public void getItems() { //print items in inventory
	validItems.clear();
	int i=1;
	for(HashMap.Entry<String,Integer> set: inventory.entrySet()) {
		if(set.getValue()>0) {
		System.out.print(i+". "+set.getKey() +": "+ set.getValue()+"\t\t");
		validItems.add(set.getKey());
		if(i%2==0) {
			System.out.println();
		}
		}
		i++;
	}
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
public void setSP(int sp) {
	SP = sp;
}
public void setXP(int xp) {
	exp = xp;
}
public void setMaxXP(double xp) {
	maxXP = xp;
}
public void setFightStatus(boolean b) {
	inFight = b;
}
public void setArea(String area) {
	this.area=area;
}
public void addSpell(Integer i, String spell) {
	spellMap.put(i,spell);
}
public void addItem(String item, Integer i) {
	inventory.put(item, i);
}
public void mapItem(Integer i, String item) {
	itemMap.put(i, item);
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
		playSound(enemyHit);
		System.out.println("You slash "+e.getName() + " for "+String.format("%.2f",hit)+" damage.");
		System.out.println("HP is "+e.getHP());
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
			default:
				break;
			}	
		}
		
		else {
			System.out.println("Invalid input or you don't have this spell!");
		}
		
	}
	
	public void useItem(Scanner sc,Entity e,Random rad,ArrayList<File> files) {  //item menu and functionalities
		getItems();
		int choice = sc.nextInt();
		if(!itemMap.containsKey(choice)) { 
			System.out.println("Invalid input.");
			return;
		}
		String item = itemMap.get(choice);  
		if(!validItems.contains(item)) {  //items with 0 count are invisible, but if char tries to access them should get an error
			System.out.println("Invalid input, perhaps on purpose...");
			return;
		}
		
		switch(item) {
		case "small potion":
			if(HP+5>(maxHP)) {
				setHP(maxHP);
			}
			else {
				setHP(HP+5);
			}
			inventory.put("small potion",inventory.get("small potion")-1) ;
			System.out.println("You drink from the vial of potion. You heal 5 HP");
			break;
		case "small mana potion":
			if(MP+5>(maxMP)) {
				setMP(maxMP);
			}
			else {
				setMP(MP+5);
			}
			inventory.put("small mana potion",inventory.get("small mana potion")-1) ;
			System.out.println("You drink from the vial of mana. You recover 5 MP");
			break;
		case "bomb":
			double damage = rad.nextInt(8);
			e.setHP(e.getHP()-damage);
			inventory.put("bomb",inventory.get("bomb")-1) ;
			System.out.println("You toss a bomb at "+e.getName()+". It does ummm "+damage+" damage");
			playSound(bomb);
			break;
			
		}
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
		double health = 1.0+(0.5*intelligence);
		setHP(HP+ health);
		if(HP>maxHP) {
			HP=maxHP;
		}
		castMP(4);
		playSound(heal);
		System.out.println("You heal for "+health + " HP.");
	}
	
	public void ice1(Entity e) {
		if(MP<4) {
			System.out.println("Not enough MP.");
			return;
		}
		double damage = 1.5+(0.5*intelligence);
		castMP(4);
		e.setHP(e.getHP() - damage);
		playSound(water);
		System.out.println("You propel ice shards at "+e.getName()+", hitting for "+ String.format("%.2f",damage)+" damage.");
	}
	public void lightning(Entity e) {
		if(MP<5) {
			System.out.println("Not enough MP.");
			return;
		}
		double  damage = 2.0+(0.52*intelligence);
		castMP(5);
		e.setHP(e.getHP() - damage);
		playSound(lightning);
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
		castMP(6);
		playSound(healmore);
		System.out.println("You heal for "+health+" HP.");
		
	}
	
	public boolean isDead() {  //check if char is dead
		return HP<=0;
	}
	
	public void castMP(int mp) {
		MP-=mp;
		if(MP<0) {
			MP = 0;
		}
	}
	
	//post-fight
	public void levelUp() {  //level up
		clearDebuffs();
		System.out.println("Congrats! ... You've leveled up!");
		System.out.println("Level: "+ level+"-> "+ ++level);
		System.out.println("Stamina: "+ stamina+"-> "+ ++stamina);
		System.out.println("Health and Mana restored!");
		maxSta++;
		HP = stamina + 5;
		maxHP++;
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
		System.out.println("Stats: \nStrength: "+strength+"\nAgility: "+agility+"\nIntelligence: "+intelligence+"\nStamina: "+stamina+"\nHP: "+String.format("%.2f",HP)+"\nMP: "+String.format("%.2f",MP));
	}
	
	 static void playSound(File sound) {
	    	
	    	try{
	    		Clip clip = AudioSystem.getClip();
	    		clip.open(AudioSystem.getAudioInputStream(sound));
	    		FloatControl gainControl = 
	    			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    		gainControl.setValue(-10.0f);
	    	    clip.start();
	    	    Thread.sleep(100);
	    }
	    	catch(Exception e) {
	    	}
	    }
}

