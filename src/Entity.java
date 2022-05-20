import java.util.Random;
import java.io.File;
import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Entity {
private int strength, agility,intelligence, stamina,level,XP,spellcount;
private String name;
private double HP;
private double maxHP;
private double MP;
private double maxMP;
private boolean canCast;

HashMap<Integer,String> spells = new HashMap<>();
int[] spellNumbers1 = new int[4];  //holds numbers that are keys to the spell
ArrayList<Integer> spellNumbers = new ArrayList<>();
Random rad;
File playerHit = new File("files/513.wav"); //play takes damage

public Entity() {
}

public Entity(int str, int agi,int intel, int sta,int lev, int xp) {
	MP = intel + 4;
	maxMP = MP;
	strength = str;
	agility = agi;
	intelligence = intel;
	stamina = sta;
	level = lev;
	HP=2+0.7*sta;
	maxHP = HP;
	XP = xp;
}

//mutators
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
public String getName() {
	return name;
}
public double getHP() {
	return HP;
}
public int getLevel() {
	return level;
}
public int getXP() {
	return XP;
}
//public int[] getSpellNumbers() {
//	return spellNumbers;
//}
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
public void setName(String name) {
	this.name = name;
}
public void setHP(double hp) {
	HP = hp;
}
public void setXP(int xp) {
	XP= xp;
}
public void setCast(boolean c) {
	canCast = c;
}
public void addSpell(int sn) {
	spellNumbers.add(sn);
}

//fight functions
public void turn(Character chr, Random rad) {  //enemy turn
	if (!canCast) {    //cannot cast spells, just attack
		Attack(chr,rad);
	}
	else {  //can cast spells, choose between casting and attacking
		
		if(MP<4) {             //OOM
			Attack(chr,rad);
		}
		
		else {				// not OOM
			
			if ((HP/maxHP)<=.50 && spellNumbers.contains(1)) {   //heal if less than 50% health and can cast heal
			heal();
			}
			else if ((HP/maxHP)<=.50 && !spellNumbers.contains(1)) {
				int i = rad.nextInt(2);
				if (i==0) {      
					Attack(chr,rad);
				}
				if (i==1){
					castSpell(chr,rad);
				}
			}
			if ((HP/maxHP)>.50){   //attack or cast offensive spell
				int i = rad.nextInt(2);
				if (i==0) {      
					Attack(chr,rad);
				}
				if (i==1){
					castSpell(chr,rad);
				}
			}
		}
	}
}

public void Attack(Character chr,Random rad) {
	double hit = getHit(rad);
	double HP = chr.getHP() - hit;
	chr.setHP(HP);
	playSound(playerHit);
	System.out.println(name+" pummels you for "+String.format("%.2f",hit)+" damage.");
}

public void guardAttack(Character chr, Random rad, double mult) {
	double hit = getHit(rad)*(1-mult);
	double HP = chr.getHP() - hit;
	chr.setHP(HP);
	playSound(playerHit);
	System.out.println("You prepare to block: "+name+" pummels you for "+String.format("%.2f",hit)+" damage.");
}

public double getMax() {
	double max_hit = Math.round(0.5 + strength);
	return max_hit;
}

public double getHit(Random rad ) {
	double max = getMax();
	double db = rad.nextDouble();
	double hit = Math.round(db*(Math.pow(1.01, agility))*max);
	if(hit>max) {hit = max;}
	return hit;
}

//spells
public void getSpellNumber(Random rad) {
	
}
public void castSpell(Character chr,Random rad) {
	
	int choice = rad.nextInt(spellNumbers.size());  //gets index in spell numbers
	int spellNumber = spellNumbers.get(choice);  //refers to spell number present in entity's spells
	switch(spellNumber) { 
	case 1: //heal
		heal();
		break;
	case 2: //ice
		ice1(chr);
		break;
	case 3: //lightning
		lightning(chr);
		break;
	case 4: //hypnotize
		hypnotize(chr);
		break;
	case 5: //healmore
//		healmore();
		break;
		
	default:
		System.out.println("Error, spell not found."); //this should not be printed out, as default should never be accessed.
		break;
	}
	
}

public void castMP(int mp) {
	MP-=mp;
	if(MP<0) {
		MP = 0;
	}
}
public void heal() {
	double health = 1.0 + 0.5*intelligence;
	HP = HP+health;
	if(HP>maxHP) {
		HP = maxHP;
	castMP(4);
	System.out.println(name+ " casts heal and recovers "+health+" HP.");
	}
}

public void ice1(Character chr) {
	double damage = 1.5+(0.5*intelligence);
	chr.setHP(chr.getHP() - damage);
	System.out.println(name+" propels ice shards at you, hitting for "+ String.format("%.2f",damage)+" damage.");
	castMP(4);
}

public void hypnotize(Character chr) {
	
	chr.setAgi(chr.getAgi()-1);
	chr.setStr(chr.getStr()-1);
	System.out.println(name+" pulls out a pocketwatch and lets it dangle back and forth. You feel a littttle tired.");
	castMP(4);
}
public void lightning(Character chr) {
	double damage = 2.0+(0.52*intelligence);
	chr.setHP(chr.getHP() - damage);
	System.out.println(name+" casts a lightning bolt upon you, striking for "+ String.format("%.2f",damage)+" damage.");
	castMP(4);
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
