
public class MyRunner extends Thread implements Runnable{
	Character chr;
	public MyRunner(Character chr){
		this.chr = chr;
	}
	@Override
	public void run() {
		long lastTimeCheck = System.currentTimeMillis();
		while(true) {
		if(System.currentTimeMillis() - lastTimeCheck >= 12000 && !chr.getFightStatus()) {  //character heals for 1 hp every 15s out of combat
			if(chr.getHP()<chr.getMaxHP()) {
				chr.setHP(chr.getHP()+1);
				System.out.println("You recover a little bit of health.");
			}
			lastTimeCheck = System.currentTimeMillis();
		}
		}
	}

	public  void start() {
		run();

	}
}
