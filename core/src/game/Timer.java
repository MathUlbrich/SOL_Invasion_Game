package game;

public class Timer {
	
	private float time;
	private float elapsed;
	
	/*
	 * Metodo usado para esperar determinado tempo. Depois que o 
	 * periodo for concluido eh retornado verdadeiro.
	 */
	
	public boolean Wait(float dt) {
		elapsed += dt;
		if(time == 0) time = 5f;
		return elapsed <= time ? false : true;
	}
	
	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}
	
}
