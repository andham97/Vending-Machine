package main.parts;

import main.Main;
import main.enums.Priority;
import main.parts.sensors.ColorSensorNXT;
import main.parts.sensors.Sensor;
import main.util.task.TaskMoneyAdded;

public class SensorController implements Runnable {
	private Sensor[] sensors = new Sensor[4];
	private final float[] INIT_VALUE;
	
	public SensorController(){
		for(int i = 0; i < this.sensors.length; i++){
			this.sensors[i] = new ColorSensorNXT("S" + (i + 1));
		}
		this.INIT_VALUE = new float[this.sensors.length];
		for(int i = 0; i < this.sensors.length; i++){
			for(int j = 0; j < 50; j++){
				this.sensors[i].update();
			}
			this.INIT_VALUE[i] = this.sensors[i].getValue();
		}
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			for(int i = 0; i < this.sensors.length; i++){
				this.sensors[i].update();
				if(this.sensors[i].getValue() != this.INIT_VALUE[i]){
					int data = 0;
					switch(i){
					case 0:
						data = 20;
						break;
					case 1:
						data = -15;
						break;
					case 2:
						data = 5;
						break;
					case 3:
						data = -9;
						break;
					}
					Main.queue.addTask(new TaskMoneyAdded(Priority.Low, data));
				}
			}
		}
	}
}
