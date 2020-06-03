package application;

import java.time.Duration;
import java.util.TimerTask;



public class Chrono extends TimerTask {
	private Duration temps;
	private int nbMinutes = 0;
	private static boolean on = false;
	private String sauvegarde;
	private TimerNumeric timer;



	public void setOn() {
		Chrono.on = true;

	}
	public void setOff() {
		Chrono.on = false;
	}
	public int getTemps() {
		return Integer.parseInt(sauvegarde)+nbMinutes;
	}
	public int getMinutes() {
		return nbMinutes;
	}
	public boolean isOn() {
		return on;
	}
	public String toString() {
		return "minutes "+ temps.toString()+sauvegarde ;
	}
	public void  addTimer(TimerNumeric timer) {
		this.timer =timer;
	}
	public TimerNumeric getTimer() {
		return this.timer;
	}

	@Override
	public void run() {
		if(!on) cancel();
		nbMinutes++;
		timer.setText(nbMinutes+"");
		System.out.println(nbMinutes);
		
		
	}

	
}
