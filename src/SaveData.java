import java.util.ArrayList;
import java.util.HashMap;

//This file contains save data to be loaded
 
public class SaveData implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
		
	public String name, gender;
	public int strength,agility,intelligence,stamina,maxStr,maxAgi,maxInt,maxSta,level,xp,maxXP,strBonus,gold,SP;
	public int progress;
	double HP,MP,maxHP,maxMP;
	public HashMap<Integer,String> spellMap;
	public ArrayList<Item> inventory, equips;
}	
