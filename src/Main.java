/*
 * To do:
 * Implement save
 * Fix entity spells
 * Implement areas
 * Implement bank
 * Implement shop
 * Polish things and fix errors
 * Get random seeds based on timestamp
 */



import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.io.File;
import javax.sound.sampled.*;

public class Main implements java.io.Serializable{ 

	
	public static void main(String[] args)  {
		
		
		//declare variables and sound files
		Scanner sc = new Scanner(System.in);
		Random rad = new Random();
		Character chr = new Character(4,4,4,4); //initialize character with equal stats
		ArrayList<File> files = new ArrayList<>();		
		File yay = new File("files/166.wav"); //play when correct answer 
		File enemyDeath = new File("files/505.wav"); //enemy dies
		File victory = new File("files/117.wav");  //victory
		files.add(yay);   //0
		files.add(enemyDeath);//1
		files.add(victory);   //2

		
		//initialize shop and inventory
		HashMap<String,Integer> shopItems = new HashMap<>();
		shopItems.put("bronze scimitar",15); shopItems.put("flintlock",25); shopItems.put("small potion",5); shopItems.put("small mana potion",5); shopItems.put("bomb",5);
		chr.addItem("small potion", 1); chr.addItem("small mana potion", 1); chr.addItem("bomb", 0); 
		chr.mapItem(1,"small potion"); chr.mapItem(2, "small mana potion"); chr.mapItem(3, "bomb");
		//initialize entities
		Entity slime = new Entity(1,2,1,3,1,2);
		slime.setName("Green Slime");
		slime.setCast(true);
		slime.addSpell(1);
		slime.addSpell(2);
		Entity wasp = new Entity(1,4,0,5,2,3);
		wasp.setName("Giant Wasp");
		Entity chk = new Entity(2,2,2,8,3,6);
		chk.setName("Evil Chicken");
		chk.setCast(true);
		chk.addSpell(1);
		chk.addSpell(4);
		
		
		//start story
		MyRunner myRunner = new MyRunner(chr);
		Thread myThread = new Thread(myRunner);
		myThread.start();

		System.out.println("Hey, welcome to this space that was just created! You're going to have to input numbers and stuff to play- if you want to...");	
		//get name
		System.out.println("So, what is your name? I'm Leraine437.");
		String name = sc.next();
		int namecount = 1;
		while (name.matches(".*[0-9].*")) {		//if name has numbers in it
			if(namecount==3) {
				System.out.println("Why no numbers? Don't ask me, it's not like I make the rules around here...");
			}
			else if (namecount==9) {
				System.out.println("I have no free will.");
			}
			else {
				System.out.println("Hey, come on! Nobody's name has numbers in it, that's just silly... Symbols are okay though.");
			}
			name = sc.next();
			namecount++;
		}
		//get gender
		System.out.println("And are you female, or male? (1 / 2) Yes I know, male is supposed to come first,\n " 
				+ "and I'm definitely not asking to collect information and cater specific ads to you. \n"
				+ "By the way, please don't input anything but a number or you'll break the game.");
		int intChoice = sc.nextInt();
		switch(intChoice) {
		case 1:
			chr.setGen("female");
			break;
		case 2:
			chr.setGen("male");
			break;
		default:
			System.out.println("Sorry, this is a binary game. I'll just set it to female :)");
			break;
		}
		
		System.out.println("Great to meet you "+name+ "! " + chr.proSub() + " doesn't know I already know everything. I mean, :)");
		rollStats(sc,rad,chr);
		
		System.out.println("Well, now that the paperwork is in order, hehe excuse me! OOPS, I just summoned a slime!!! \n"
				+ "But dont worry, as a character, you inherently have access to more mechanics and opportunities\n"
				+ "to better yourself and gain an advantage over your enemies. Slimes don't have that.");
		
		fight(sc,rad,slime,chr,files);
		sleep();
		
		System.out.println("You've gained a new spell... Ice Shards acquired!");
		chr.addSpell(2, "ice shards");
		sleep();
		
		System.out.println("Anyway great work mate, you're a real one! To celebrate, lets 'ave a go with the famous national quiz!");
		quiz1(chr,sc,files);
		sleep();
		
		System.out.println("You can take a break now, spend some money, some skill points if you aced the quiz, save, whatever, just forget about the wasp that's flying toward you at this moment.");
		Menu(sc,chr);
		fight(sc,rad,wasp,chr,files);
		System.out.println("No time for breaks... you hear demented squawking from the nearby haystack.");
		fight(sc,rad,chk,chr,files);
		
	}
										
	
	public static void Menu(Scanner sc,Character chr) {
		int choice=0;
		while(choice !=5) {
		System.out.println("1. Shop\n2. Save\n3. Print Stats\n4. Spend SP\n5. Exit");
		choice = sc.nextInt();
		switch(choice) {
		case 1: //shop
			System.out.println("Welcome! What're ya sellin... or buyin?");
			shop();
			break;
		case 2: //save not functional yet
			break;
		case 3: //print stats
			chr.printStats();
			break;
		case 4: //spend SP
			chr.spendSP(sc);
			break;
		case 5: 
			return;
		}
		}
	}
	
	public static void rollStats(Scanner sc, Random rad, Character chr) {
		System.out.println("Would you like to roll for different stats? (Y/N) ");
		char charChoice = sc.next().charAt(0);
		
		while (charChoice == 'y' || charChoice == 'Y') {
			int points = 16;
			int[] stats = new  int[4];
			for( int i=0; i<3;i++) {
			stats[i] = rad.nextInt(3+1)+2;
			points-=stats[i];			
			}
			stats[3] = points;
			chr.setStr(stats[0]); chr.setAgi(stats[1]); chr.setInt(stats[2]); chr.setSta(stats[3]); chr.setHP(4+chr.getSta()); chr.setMP(5+chr.getInt());
			chr.printStats();
			System.out.println("Roll again? (Y/N) ");
			charChoice = sc.next().charAt(0);
		}
	}
	public static void fight(Scanner sc,Random rad, Entity ent, Character chr,ArrayList<File> files) {
		System.out.println("A wild " + ent.getName() + " has appeared!");
		
		chr.setFightStatus(true);  //set fight status flag
		sleep();
		fightMenu(sc,rad,ent,chr,files);
		
		//post-fight
		playSound(files.get(1));    
		sleep();
		System.out.println("You've defeated "+ent.getName()+"! Gained "+ent.getXP()+" experience.");
		playSound(files.get(2));
		chr.setFightStatus(false);  //set fight status flag
		chr.setXP(chr.getXP()+ent.getXP()); //increase experience
		if(chr.getXP()>=chr.getMaxXP()) {  //if max xp level up
			chr.levelUp();
		}
		
	}
	public static void fightMenu(Scanner sc,Random rad,Entity e,Character chr,ArrayList<File> files) {  //displays fight menu, handles input, takes action
		while(e.getHP()>0) {  //while enemy is alive, display menu after each turn
		System.out.println("1. Attack\n2. Spell\n3. Guard\n4. Item\n5. Run\n\nHP: "+String.format("%.2f",chr.getHP())+"\nMP: "+chr.getMP());
		int choice = sc.nextInt();
		switch(choice) {
		
		case 1:  //attack
			chr.Attack(e,rad);
			sleep();
			if(e.getHP()<=0) {
				return;
			}
			e.turn(chr,rad);
			if(chr.isDead()) {  //check if char dead
				//death action
				death(sc,chr);
				}
			break;
			
		case 2:  //cast spell
			chr.castSpell(e, sc); 
			sleep();
			if(e.getHP()<=0) {
				return;
			}
			e.turn(chr,rad);
			if(chr.isDead()) {  //check if char dead
				System.out.println(chr.getHP());
				//death action
				death(sc,chr);
				}
			break;
			
		case 3:  //guard
			e.guardAttack(chr, rad, chr.guard()); //enemy attacks with a reduced hit
			if(chr.isDead()) {  //check if char dead
				//death action
				death(sc,chr);
				}
			break;
		case 4: //use item
			chr.useItem(sc,e,rad,files);
			sleep();
			if(e.getHP()<=0) {
				return;
			}
			e.turn(chr, rad);
			if(chr.isDead()) {  //check if char dead
				//death action
				death(sc,chr);
				}
			break;
		case 5:  //run
			if(chr.run(e,rad)) {  //if roll is greater than run threshold
				System.out.println("You successfully run away."); 
				return;  //end fight
			}
			else {
				System.out.println("Run failed, get fkt");
				e.turn(chr, rad);  //run failed

				if(chr.isDead()) {  //check if char dead
					death(sc,chr);
				}
			}
			break;
			
		default:
			System.out.println("Invalid selection!");
			break;
		}
		
		}
	}
	
	public static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void death(Scanner sc,Character chr) {
		System.out.println("Oh no, you're dead!");
		sleep();
		System.out.println("Continue? (Y/N)");
		char choice = sc.next().charAt(0);
		if(choice=='Y'||choice=='y') {
			//start at save point, but for now exit
			System.exit(0);
		}
		else {
			System.exit(0);
		}
	}
	
	
	public static void shop() {
		
	}

	
	public static void quiz1(Character chr,Scanner sc,ArrayList<File> files ) {
		int points=0;
		System.out.println();
		System.out.println("Q1. What does agility do?\n1. Hit harder\n2. Hit more consistently\n3. Heal faster\n4. Run away faster");
		sleep();
		int choice = sc.nextInt();
		if(choice == 2) {
			playSound(files.get(0));
			System.out.println("Congrats! +1");
			points++;
		}
		else {
			System.out.println("Thats not quite right. Agility allows you to hit more consistently, granting more dps.");
		}
		sleep();
		System.out.println();
		System.out.println("Q2. Bob is a construction worker and manager at a steel factory. His friend Hank delivers steel pipes to him everyday at 25mph when the distance between them is 5 miles. \n"
				+ "If Bob were the main character, he would have 10 strength. By the way, let's say max damage is calculated by 0.5+strength.\n"
				+ "If Bob attacks an imaginary being every minute, and one day Bob was sent into the body of Hank and decided to take over his job, how much damage would\n"
				+ "Bob do during the trip to \"Bob\" if we assume he hits max damage each time?\n"
				+ "1. 77\n2. 73\n3. 0\n4. 126");
		sleep();
		choice = sc.nextInt();
		if(choice == 4) {
			playSound(files.get(0));
			System.out.println("Congrats! +1");
			points++;
		}
		else {
			System.out.println("Thats not quite right. You probably picked 0 or some other idiot answer, anyway Bob attacks at a max hit of 10.5 12 times during the trip.");
		}
		sleep();
		System.out.println();
		System.out.println("Q3. How many outer electrons are there in the element Cesium?\n1. 1 \n2. 2 \n3. 4\n4. 6");
		sleep();
		choice = sc.nextInt();
		if(choice == 1) {
			playSound(files.get(0));
			System.out.println("Congrats! +1");
			points++;
		}
		else {
			System.out.println("Thats not quite right. It's just a simple google search...");
		}
		System.out.println();
		System.out.println("Lets see how you did...");
		sleep();
		if(points==3) {
			System.out.println("You are smart. Therefore I reward you with an extra skill point. You can spend it later.");
			chr.setSP(1);
		}
		else if(points==2) {
			System.out.println("You have done okay. I don't know who you are, but take ummm well you can have like respect.");
		}
		else {
			System.out.println("Idiot! LOSER!");
		}
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

