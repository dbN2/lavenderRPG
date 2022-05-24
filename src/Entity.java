import java.util.Random;
import java.io.File;
import javax.sound.sampled.*;
import java.util.ArrayList;


public class Entity {
private int strength, agility,intelligence, stamina,level,XP,gold;
private String name;
private double HP;
private double maxHP;
private double MP;
private double maxMP;
private boolean canCast;

ArrayList<Integer> spellNumbers = new ArrayList<>(); //holds numbers of spells that map to which spell is cast
Random rad;
File playerHit = new File("files/513.wav"); //play takes damage
File guard = new File("files/718.wav");     //guardAttack
File hypno = new File("files/201.wav");     //hypnotize
File healmore = new File("files/121.wav"); //healmore

public Entity() {
}

public Entity(int str, int agi,int intel, int sta,int lev, int xp,int g) {
	MP = intel + 6;
	maxMP = MP;
	strength = str;
	agility = agi;
	intelligence = intel;
	stamina = sta;
	level = lev;
	HP=2+0.7*sta;
	maxHP = HP;
	XP = xp;
	gold = g;
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
public int getGold() {
	return gold;
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
public void setGold(int g) {
	gold = g;
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
			else if ((HP/maxHP)>.50){   //attack or cast offensive spell
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
	Main.playSound(playerHit);
	System.out.println(name+" pummels you for "+String.format("%.2f",hit)+" damage.");
}

public void guardAttack(Character chr, Random rad, double mult) {
	double hit = getHit(rad)*(1-mult);
	double HP = chr.getHP() - hit;
	chr.setHP(HP);
	Main.playSound(guard);
	System.out.println("You prepare to block: "+name+" pummels you for "+String.format("%.2f",hit)+" damage.");
}

public double getMax() {	//get maximum hit
	double max_hit = Math.round(0.5 + strength*(.75));
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
		healmore();
		break;
	case 6: //stare
		stare(chr);
		break;
	case 7: //insult
		insult(chr);
		break;
		
	default:
		System.out.println("Error, spell not found."); //this should not be printed out, as default should never be accessed.
		break;
	}
	
}
public void useMP(int mp) {
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
	}
	useMP(4);
	System.out.println(name+ " casts heal and recovers "+health+" HP.");
}

public void ice1(Character chr) {
	double damage = 1.5+(0.5*intelligence);
	chr.setHP(chr.getHP() - damage);
	System.out.println(name+" propels ice shards at you, hitting for "+ String.format("%.2f",damage)+" damage.");
	useMP(4);
}

public void hypnotize(Character chr) {
	
	chr.setAgi(chr.getAgi()-1);
	chr.setStr(chr.getStr()-1);
	System.out.println(name+" pulls out a pocketwatch and lets it dangle back and forth. You feel a littttle tired.");
	Main.playSound(hypno);
	useMP(4);
}
public void lightning(Character chr) {
	double damage = 2.0+(0.52*intelligence);
	chr.setHP(chr.getHP() - damage);
	System.out.println(name+" casts a lightning bolt upon you, striking for "+ String.format("%.2f",damage)+" damage.");
	useMP(4);
}
public void healmore() {
	double health = 2.5+(0.65*intelligence);
	setHP(HP + health);
	if(HP>maxHP) {
		HP = maxHP;
	}
	useMP(8);
	Main.playSound(healmore);
	System.out.println(name+" heals for "+health+" HP.");
	
}
public void insult(Character chr) {
	double damage = 2.0-(0.2*chr.getStr());
	chr.setHP(chr.getHP() - damage);
	System.out.println(name+" insults one of your insecurities, striking for "+ String.format("%.2f",damage)+" damage.");
	useMP(4);
}

public void stare(Character chr) {
	double damage = 1.5-(0.2*chr.getStr());
	chr.setHP(chr.getHP() - damage);
	System.out.println(name+" stares at you with contempt, striking for "+ String.format("%.2f",damage)+" damage.");
	useMP(4);
}
public void plead() {
	System.out.println("Please don't do this... I've got children.");
}

}
