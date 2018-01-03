package game;


public class Bullet {
	
	private Integer max; // O numero maximo de balas
	private Integer num; // numero de balas no momento
	
	public Bullet(int max) {
		this.max = max;
		num = 0;
	}
	
	public void charge(int bullets) {
		this.num += bullets;
		if(this.num > max)
			this.num = max;
	}
	
	public boolean shot() {
		if(num == 0) return false;
		num--;
		return true;
	}
	
	public boolean shot(int num) { 
		if(num - num < 0) return false;
		num -= num;
		return true;
	}
	
	/** 
	 * @return o numero maximo de balas no momento
	 */
	public Integer getNum() {
		return num;
	}
	
	/**
	 * @return o maximo de balas definidas no momento de instancia
	 */
	public Integer getMAX() {
		return max;
	}
	
	public Integer getDeficit() {
		return max - num;
	}
	
	
}
