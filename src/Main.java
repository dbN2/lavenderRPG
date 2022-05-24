/*
 * To do:
 * Implement savepoints
 * Implement areas
 * Implement bank
 * Polish things and fix errors
 * Add music
 * Get random seeds based on timestamp
 */


import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.io.File;
import javax.sound.sampled.*;


public class Main { 
	static Character chr;
	static Clip clip;
	static boolean isPlaying;
	static Scanner sc = new Scanner(System.in);
	static Random rad = new Random();
	static ArrayList<Item> shopItem = new ArrayList<>();
	static HashMap<String,Integer> shopItems = new HashMap<>();
	static HashMap<String,Integer> shopSpells = new HashMap<>();
	static ArrayList<File> files = new ArrayList<>();	
	static ArrayList<Entity> entities = new ArrayList<>();
	/*
	 * Slime :	 0
	 * Wasp  :	 1
	 * Chicken:  2
	 * Mourner:  3
	 * Hans:     4
	 * Lobster:  5
	 * Vampiress:6
	 * Dentist:  7
	 * Wind:     8
	 * Man:      9
	 */
	
	public static void main(String[] args)  {
		chr = new Character(4,4,4,4);
		//declare variables and sound files
		inititalizeFiles();
		//initialize shop and inventory
		chr.initializeItems();
		//initialize entities
		initializeEntities();
		//new game or continue
	
		System.out.println("1. New Game\n2. Continue");
		String choice = sc.next();
		switch(choice) {
		case "1":
			break;
		case "2":
			load(chr);
			System.out.println(chr.getName());
			break;
		default:
			System.out.println("Then I'll start a new game...");
			break;
		}
		
		//start story
		MyRunner myRunner = new MyRunner(chr);
		Thread myThread = new Thread(myRunner);
		myThread.start();
		
		if(chr.getProgress()<1) {
			checkpoint0(chr,entities);
		}
		if(chr.getProgress()<2) {
			checkpoint1(chr,entities);
		}
		if(chr.getProgress()<3) {
			checkpoint2(chr,entities);
		}
		
	}
	public static void checkpoint2(Character chr,ArrayList<Entity> e) {
		System.out.println("You feel tired...");
		sleep(1);
		System.out.println("You keep walking along the dusty old trail and eventually see a small town.");
		sleep(2);
		System.out.println("Strangers are a blur to you as you veer towards the building that looks like an inn.");
		sleep(3);
		System.out.println("\"It's 25 gold a night. But if you do a job for me, I'll let you stay for free.\"");
		sleep(2);
		System.out.println("Being the miser you are, you agree to help, putting your worries aside until tomorrow.");
		System.out.println("You climb under the fresh linen sheets and quickly doze off...");
		sleep(1);
		playSound(files.get(6));
		System.out.println("\"It wasn't always like this.\"");
		sleep(9);
		System.out.println("You wake up.\nSomething feels different.\nYou go downstairs... The manager looks."); 
		sleep(4);
		System.out.println("\"About that job. There's a vampire beneath the cemetary about a mile north of here.\"");
		sleep(2);
		System.out.println("\"You're gonna have to go kill it. You'll need this stake...\"\nHe hands you a jagged wooden stake.");
		sleep(2);
		System.out.println("\"Run away, and you'll find yourself in real trouble.");
		sleep(2);
		System.out.println("You step outside. The sun is glaring today, and the air feels dry.");
		sleep(2);
		System.out.println("A man looks over in your direction.");
		sleep(1);
		fight(e.get(9),chr);
		System.out.println("\"What's your problem!?\" He stumbles away. People start to look.");
		sleep(1);
		System.out.println("You keep walking north.");
		
		chr.setProgress(chr.getProgress()+1);
		Menu(chr);
	}
	public static void checkpoint1(Character chr,ArrayList<Entity> e) {
		Menu(chr);
		fight(e.get(1),chr);
		sleep(1);
		System.out.println("\nNo time for breaks... you hear demented squawking from the nearby haystack.");
		sleep(1);
		fight(e.get(2),chr);
		System.out.println("\nGreat job, time to save again because of save anxiety!");
		chr.setProgress(chr.getProgress()+1);
		Menu(chr);
	}
	public static void checkpoint0(Character chr,ArrayList<Entity> e) {
		System.out.println("Hey, welcome to the land of Land1024! You're going to have to input numbers and stuff to play- if you want to...");	
		//get name
		System.out.println("So, what is your name? I'm 437.");
		chr.setName(sc.next());
		int namecount = 1;
		while (chr.getName().matches(".*[0-9].*")) {		//if name has numbers in it
			if(namecount==3) {
				System.out.println("Why no numbers? Don't ask me, it's not like I make the rules around here...");
			}
			else if (namecount==9) {
				System.out.println("I have no free will.");
			}
			else {
				System.out.println("Hey, come on! Nobody's name has numbers in it, that's just silly... Symbols are okay though.");
			}
			chr.setName(sc.next());
			namecount++;
		}
		//get gender
		System.out.println("And are you female, or male? (1 / 2) Yes I know, male is supposed to come first,\n" 
				+ "and I'm definitely not asking to collect personal information and cater specific ads to you. \n");
	
		String choice = sc.next();
		switch(choice) {
		case "1":
			chr.setGen("female");
			break;
		case "2":
			chr.setGen("male");
			break;
		default:
			System.out.println("Sorry, this is a binary game. I'll just set it to female :)");
			break;
		}
		
		System.out.println("Great to meet you "+chr.getName()+ "! ");
		rollStats(chr);
		
		System.out.println("Well, now that the paperwork is in order, hehe excuse me! OOPS, I just summoned a slime!!! \n"
				+ "But dont worry, as a character, you inherently have access to more mechanics and opportunities\n"
				+ "to better yourself and gain an advantage over your enemies. Slimes don't have that.\n");
		sleep(1);
		
		fight(e.get(0),chr);
		sleep(1);
		
		System.out.println("You've gained a new spell... Ice Shards acquired!");
		chr.addSpell(2, "ice shards");
		sleep(1);
			
		System.out.println("Anyway great work mate, you're a real one! To celebrate, lets 'ave a go with the famous national quiz!");
		quiz1(chr,files);
		sleep(1);
		
		chr.setProgress(chr.getProgress()+1);
		
		System.out.println("You can take a break now, spend some money, some skill points if you aced the quiz, save, whatever, just forget about the wasp that's flying toward you at this moment.");
		Menu(chr);
	}
	
	
	public static void rollStats(Character chr) {
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
	
	//Fight functions //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void fight(Entity e, Character chr) {
		System.out.println("A wild " + e.getName() + " has appeared!");
		
		chr.setFightStatus(true);  //set fight status flag
		sleep(1);
		fightMenu(e,chr);
		
		//post-fight
		sleep(1);
		System.out.println("You've defeated "+e.getName()+"! Gained "+e.getXP()+" experience and "+e.getGold()+" gold.");
		playSound(files.get(2));
		sleep(1);
		chr.setFightStatus(false);  //set fight status flag
		chr.setGold(chr.getGold()+e.getGold());
		chr.setXP(chr.getXP()+e.getXP()); //increase experience
		while(chr.getXP()>=chr.getMaxXP()) {  //if max xp level up
			chr.levelUp();
		}
		
	}
	public static void spellTurn(Entity e,Character chr,ArrayList<File> files) {
		chr.castSpell(e, sc); 
		sleep(1);
		if(e.getHP()<=0) {
			playSound(files.get(1));
			return;
		}
		e.turn(chr,rad);
		if(chr.isDead()) {  //check if char dead
			System.out.println(chr.getHP());
			//death action
			death(chr);
			}
	}
	public static void fightMenu(Entity e,Character chr) {  //displays fight menu, handles input, takes action
		while(e.getHP()>0) {  //while enemy is alive, display menu after each turn
		System.out.println("1. Attack\n2. Spell\n3. Guard\n4. Item\n5. Run\n\nHP: "+String.format("%.2f",chr.getHP())+"\nMP: "+chr.getMP());
		int choice = sc.nextInt();
		switch(choice) {
		
		case 1:  //attack
			chr.Attack(e,rad);
			sleep(1);
			if(e.getHP()<=0) {				//check if enemy dead 
				playSound(files.get(1));	
				return;
			}
			e.turn(chr,rad);	//enemy turn
			if(chr.isDead()) {  //check if char dead
				//death action
				death(chr);
				}
			break;
			
		case 2:  //cast spell
			chr.castSpell(e, sc); 
			if (chr.getBack()) {	//if pressed back, return to menu
				chr.setBack(false);
				continue;
			}
			sleep(1);
			if(e.getHP()<=0) {			//check if enemy dead 
				playSound(files.get(1));
				return;
			}
			e.turn(chr,rad);
			if(chr.isDead()) {  //check if char dead
				System.out.println(chr.getHP());
				//death action
				death(chr);
				}
			break;
			
		case 3:  //guard
			e.guardAttack(chr, rad, chr.guard()); //enemy attacks with a reduced hit
			if(chr.isDead()) {  //check if char dead
				//death action
				death(chr);
				}
			break;
			
		case 4: //use item
			chr.useItem1(e,sc);
			if(chr.getBack()) {			//if pressed back, return to menu
				chr.setBack(false);
				continue;
			}
			sleep(1);
			if(e.getHP()<=0) {				//check if enemy dead 
				playSound(files.get(1));
				return;
			}
			e.turn(chr, rad);
			if(chr.isDead()) {  //check if char dead
				//death action
				death(chr);
				}
			break;
			
		case 5:  //run
			if(chr.run(e,rad)) {  //if roll is greater than run threshold
				System.out.println("You successfully run away."); 
				playSound(files.get(3));
				return;  //end fight
			}
			else {
				System.out.println("Run failed, you are simply too slow or possibly too low level.");
				e.turn(chr, rad);  //run failed
				if(chr.isDead()) {  //check if char dead
					death(chr);
				}
			}
			break;
			
		default:
			System.out.println("Invalid selection!");
			break;
		}
		}
	}

	
	public static void death(Character chr) {
		System.out.println("Oh no, you're dead!");
		sleep(2);
		String choice="";
		System.out.println("Continue? (Y/N)");
		choice = sc.next();
		while(choice.toLowerCase()!="y" && choice.toLowerCase()!="n") {		
			System.out.println("Invalid input");
			choice=sc.next();
		}
		if(choice=="Y"||choice=="y") {
			//start at save point, but for now exit
			//maybe startFrom(chr.getProgress())
			main(new String[1]);
		}
		else{
			System.exit(0);
		}

		}
	
	//Menu functions //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void save(Character chr) {
		SaveData data = new SaveData();
		data.name = chr.getName();
		data.gender = chr.getGen();
		data.gold = chr.getGold();
		data.strength = chr.getStr();
		data.agility = chr.getAgi();
		data.intelligence = chr.getInt();
		data.stamina = chr.getSta();
		data.spellMap = chr.spellMap;
		data.maxHP = chr.getMaxHP();
		data.maxMP = chr.getMaxMP();
		data.level = chr.getLevel();
		data.inventory = chr.inventory;
		data.equips = chr.equips;
		data.maxStr = chr.getMaxStr();
		data.maxAgi = chr.getMaxAgi();
		data.maxInt = chr.getMaxInt();
		data.maxSta = chr.getMaxSta();
		data.progress = chr.getProgress();
		
		try {
			ResourceManager.save(data, "1.save");
			System.out.println("Data saved.\n");
		}
		catch (Exception e) {
			System.out.println("Couldn't save: "+ e.getMessage());
		}
	}
	public static void load(Character chr) {
		try {
			SaveData data = (SaveData) ResourceManager.load("1.save");
			chr.setName(data.name);
			chr.setGold(data.gold);
			chr.setGen(data.gender);
			chr.setAgi(data.agility);
			chr.setStr(data.strength);
			chr.setInt(data.intelligence);
			chr.setSta(data.stamina);
			chr.setMaxAgi(data.maxAgi);
			chr.setMaxStr(data.maxStr);
			chr.setMaxInt(data.maxInt);
			chr.setMaxSta(data.maxSta);
			chr.spellMap = data.spellMap;
			chr.inventory = data.inventory;
			chr.setMaxHP(data.maxHP);
			chr.setMaxMP(data.maxMP);
			chr.setProgress(data.progress);
			System.out.println("Data loaded.\n");
		}
		catch( Exception e) {
			System.out.println("Couldn't load save data... "+ e.getMessage());
		}
	}
										
	
	public static void Menu(Character chr) {
		
		int choice=0;
		while(choice !=5) {
			stopClip();
		System.out.println("1. Shop\n2. Save\n3. Print Stats\n4. Spend SP\n5. Exit\n6. Go Fishing");
		choice = sc.nextInt();
		switch(choice) {
		case 1: //shop
			shop(chr);
			break;
		case 2: //save not functional yet
			save(chr);
			break;
		case 3: //print stats
			chr.printStats();
			break;
		case 4: //spend SP
			chr.spendSP(sc);
			break;
		case 5: 
			return;
		case 6:
			fishMenu(chr);
			break;
		}
		}
	}
	
	public static void shop(Character chr) {
		int choice = 0;
		while(choice!=7) {
			System.out.println("Welcome! What're ya buyin?");
			printShop(chr);
			choice = sc.nextInt();
			if(choice==7) {return;}
			Item item = chr.getShopItem(choice);
			if(item.getName()=="bronze scimitar" || item.getName() =="flintlock") {
				if(chr.getGold()<item.getCost()) {
					System.out.println("Not enough gold.");
					continue;
				}
				else if(item.getQuantity()==1) {
					System.out.println("You already have one!");
					continue;
				}
				else {
					buyItem(chr,item);
				}
			}
			else {
				if(chr.getGold()<item.getCost()) {
					System.out.println("Not enough gold.");
					continue;
				}
				else {
						buyItem(chr,item);
				}
			}
		}
	}
	
	public static void buyItem(Character chr,Item item) {
		System.out.println("The shopkeeper tosses you a "+item.getName());
		chr.incQuantity(item);
		chr.setGold(chr.getGold() - item.getCost());
	}
	
	public static void printShop(Character chr) {
		int i=0;
		for(i=1;i<7;i++) {	//print items
			Item item = chr.getShopItem(i);
			System.out.print(i+". "+item.getName() +": "+ item.getCost()+"\t");
			if(i%2==0) {
				System.out.println();
			}
		}
		System.out.println(7+".Back");
			if(shopItem.size()>7) {
			for(i=7;i<shopItem.size();i++) {Item item = chr.getShopItem(i);
			System.out.print(i+1+". "+item.getName() +": "+ item.getCost()+"\t");
			if(i%2==0) {
				System.out.println();
			}
			i++;
			}
			}
		for(HashMap.Entry<String,Integer> set: shopSpells.entrySet()) {			//print spells
			System.out.print(i+". "+set.getKey() +": "+ set.getValue()+"\t\t");
			if(i%2==0) {
				System.out.println();
			}
			i++;
			}
	}
	
	 
	   public static void fishMenu(Character chr) {
		   playLoop(files.get(5));
		   isPlaying=true;
		   System.out.println("\nYou walk down the empty space until you see foliage beginning to render in.\n"
		   		+ "Down the path, a narrow, worn trail leads past the forest into a small open area.\n"
		   		+ "The sun smiles down on you, and you retrieve your fishing rod and bait.\n");
		   int choice = 0;
		   while(choice!=3){
		   		System.out.println("\n1. Cast\t(Bait: "+chr.getItem(5).getQuantity() +")"+"\n2. Sit\n3. Leave");
		   		choice = sc.nextInt();
		   		switch(choice) {
		   		case 1: 
		   			if(chr.inventory.get(5).getQuantity()<1) {
		   				System.out.println("No bait left.");
		   				return;
		   			}
		   			fish(chr);
		   		break;
		   		case 2: System.out.println("You sit on the warm grassy field, feeling a little bit better.");
		   		if(chr.getHP()<chr.getMaxHP()) {
		   		chr.setHP(chr.getHP()+1);
		   		}
		   		break;
		   		case 3: 
		   			System.out.println("You mosey on back, past the forest...");
		   			break;
		   		default:
		   		}
	   }
	   }
	   public static void fish(Character chr) {
		   ArrayList<String> fishes = new ArrayList<>();				//list of possible fish
		   fishes.add("rainbow trout"); fishes.add("smallmouth bass"); 
		   fishes.add("largemouth bass"); fishes.add("pike");
		   fishes.add("sunfish"); fishes.add("dungeness crab");
		   fishes.add("blue crab"); fishes.add("lobster");
		   
		   System.out.println("You cast your line...");
		   playSound(files.get(4));
		   long lastTimeCheck = System.currentTimeMillis();
		   int rand = rad.nextInt(12000)+6000;						//takes 6 to 18 seconds to catch a fish
		   while(true) {
			   if (System.currentTimeMillis()- lastTimeCheck > rand) {
				   String fish = fishes.get(rad.nextInt(fishes.size()));
				   System.out.println("You reel in a "+fish+". ");
				   Item fsh = chr.inventory.get(3);
				   fsh.setQuantity(fsh.getQuantity()+1);
				   Item bait = chr.inventory.get(5);
				   bait.setQuantity(bait.getQuantity()-1);
				   lastTimeCheck = System.currentTimeMillis();
				   return;
			   }
			   if (System.currentTimeMillis()- lastTimeCheck > 17250 ) {  //rare event
				   System.out.println("You reel in a shark. You certainly shouldn't swim here!");
				   Item shrk = chr.inventory.get(4);
				   shrk.setQuantity(shrk.getQuantity()+1);
				   lastTimeCheck = System.currentTimeMillis();
				   return;
			   }
		   }
	   }
	
	public static void quiz1(Character chr,ArrayList<File> files ) {
		int points=0;
		System.out.println();
		System.out.println("Q1. What does agility do?\n1. Hit harder\n2. Hit more consistently\n3. Heal faster\n4. Run away faster");
		sleep(1);
		int choice = sc.nextInt();
		if(choice == 2) {
			playSound(files.get(0));
			System.out.println("Congrats! +1");
			points++;
		}
		else {
			System.out.println("Thats not quite right. Agility allows you to hit more consistently, granting more dps.");
		}
		sleep(1);
		System.out.println();
		System.out.println("Q2. Bob is a construction worker and manager at a steel factory. His friend Hank delivers steel pipes to him everyday at 25mph when the distance between them is 5 miles. \n"
				+ "If Bob were the main character, he would have 10 strength. By the way, let's say max damage is calculated by 0.5+strength.\n"
				+ "If Bob attacks an imaginary being every minute, and one day Bob was sent into the body of Hank and decided to take over his job, how much damage would\n"
				+ "Bob do during the trip to \"Bob\" if we assume he hits max damage each time?\n"
				+ "1. 77\n2. 73\n3. 0\n4. 126");
		sleep(1);
		choice = sc.nextInt();
		if(choice == 4) {
			playSound(files.get(0));
			System.out.println("Congrats! +1");
			points++;
		}
		else {
			System.out.println("Thats not quite right. You probably picked 0 or some other idiot answer, anyway Bob attacks at a max hit of 10.5 12 times during the trip.");
		}
		sleep(1);
		System.out.println();
		System.out.println("Q3. How many outer electrons are there in the element Cesium?\n1. 1 \n2. 2 \n3. 4\n4. 6");
		sleep(1);
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
		sleep(1);
		if(points==3) {
			System.out.println("You are smart. Therefore I reward you with an extra skill point. You can spend it later.");
			chr.setSP(1);
		}
		else if(points==2) {
			System.out.println("You have done okay. but take- well, take my respect. But you don't get anything.");
		}
		else if(points==1){
			System.out.println("At least you didn't get all of them wrong.");
		}
		else {
			System.out.println("At least you're not... in jail.");
		}
	}
	public static void initializeEntities() {
		Entity slime = new Entity(1,2,999,3,1,2,2);
		entities.add(slime);
		slime.setName("Green Slime");
		Entity wasp = new Entity(1,4,0,5,2,3,4);
		entities.add(wasp);
		wasp.setName("Giant Wasp");
		Entity chk = new Entity(2,2,3,9,3,9,9);
		chk.setName("Evil Chicken");
		entities.add(chk);
		chk.setCast(true);
		chk.addSpell(1);
		chk.addSpell(4);
		Entity mrn = new Entity(2,1,3,8,3,5,6);
		entities.add(mrn);
		mrn.setName("Mourner");
		Entity hans = new Entity(1,1,10,6,5,4,15);
		hans.setName("Hans");
		hans.setCast(true);
		entities.add(hans);
		Entity lob = new Entity(2,4,1,7,3,6,6);
		lob.setName("Unfriendly Lobster");
		entities.add(lob);
		Entity vam = new Entity(3,1,4,12,5,12,12);
		vam.setName("Vampiress");
		entities.add(vam);
		Entity den = new Entity(5,1,10,25,10,250,250);
		den.setName("Dentist's Room");
		entities.add(den);
		Entity wind = new Entity(1,20,0,9999,1,999,999);
		wind.setName("Wind");
		entities.add(wind);
		Entity man = new Entity(2,2,2,10,4,8,7);
		man.setName("Teenager");
		man.setCast(true);
		man.addSpell(6);
		man.addSpell(7);
		entities.add(man);
	}
	
	public static void inititalizeFiles(){
		File yay = new File("files/166.wav"); //play when2 correct answer 
		File enemyDeath = new File("files/505.wav"); //enemy dies
		File victory = new File("files/117.wav");  //victory
		File run = new File("files/3359.wav");  //successful run
		File fish = new File("files/3254.wav");
		File ell = new File("files/ellinia.wav");
		File sleep = new File("files/sleep.wav");
		files.add(yay);   	  //0
		files.add(enemyDeath);//1
		files.add(victory);   //2
		files.add(run);       //3
		files.add(fish);      //4
		files.add(ell);       //5
		files.add(sleep);     //6

	}
	
	public static void sleep(int i) {	//pause for 1 second
		try {
			Thread.sleep(i*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Sound functions //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	   static void playSound(File sound) {
	    	try{
	    		Clip clip = AudioSystem.getClip();
	    		clip.open(AudioSystem.getAudioInputStream(sound));
	    		FloatControl gainControl = 
	    			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    		gainControl.setValue(-13.0f);
	    	    clip.start();
	    	    Thread.sleep(100);
	    }
	    	catch(Exception e) {
	    	}
	    }
	   
	   static void stopClip() {
		  if(isPlaying) {
			  clip.stop();
		  }
	   }
	   static void playLoop(File sound) {
	    	 
	 		try {
	 			clip= AudioSystem.getClip();
	 			clip.open(AudioSystem.getAudioInputStream(sound));
				clip.loop(10);
				FloatControl gainControl = 
	    			    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    		gainControl.setValue(-18.0f);
	 		} catch (Exception e) {
	 		}
	    }
	  

}

