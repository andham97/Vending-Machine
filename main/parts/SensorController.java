package main.parts;

import main.Main;
import main.enums.Priority;
import main.parts.sensors.ColorSensorNXT;
import main.parts.sensors.Sensor;
import main.util.task.TaskSensorTriggered;

public class SensorController implements Runnable {
	private Sensor[] sensors = new Sensor[4];
	private final float CONTROL_VALUE = 0.3f;
	
	public SensorController(){
		for(int i = 0; i < sensors.length; i++){
			sensors[i] = new ColorSensorNXT("S" + (i + 1));
		}
	}
	
	public void start(){
		new Thread(this).start();
	}
	
	public void run(){
		while(Main.isRunning){
			for(int i = 0; i < this.sensors.length; i++){
				this.sensors[i].update();
				if(this.sensors[i].getValue() == this.CONTROL_VALUE){
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
					Main.queue.addTask(new TaskSensorTriggered(Priority.Low, data));
				}
			}
		}
	}
}
