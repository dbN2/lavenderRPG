
public class Item implements java.io.Serializable{
	private String name;
	private int ID;
	private int quantity;
	private int cost;
	private double heal;
	private double mheal;
	private double damage;
	public Item() {
		
	}
	public Item(int id) {
		ID = id;
	}
	public int getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public int getQuantity() {
		return quantity;
	}
	public int getCost() {
		return cost;
	}
	public double getHeal() {
		return heal;
	}
	public double getMHeal() {
		return mheal;
	}
	public double getDamage() {
		return damage;
	}
	
	public void setName(String n) {
		name = n;
	}
	public void setQuantity(int q) {
		quantity = q;
	}
	public void setCost(int c) {
		cost = c;
	}
	public void setHeal(double h) {
		heal = h;
	}
	public void setMHeal(double m) {
		mheal = m;
	}
	public void setDamage(double d) {
		damage = d;
	}
	


	
}
