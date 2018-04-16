package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    private int status = 0;

    private double targetAngle;

    private Driver aDriver = new Driver();

    public void reset() {}

    public int control (SensorModel inputs) {

        this.targetAngle = inputs.getAngleToNextWaypoint();

        Vector2d myPos = inputs.getPosition();
        Vector2d target1 = inputs.getNextWaypointPosition();
        Vector2d target2 = inputs.getNextNextWaypointPosition();
        double targetDistance = inputs.getDistanceToNextWaypoint();
        double currentSpeed = inputs.getSpeed();

		int command=neutral;
		if(this.status == 0){
            command = this.aDriver.adjust(inputs);
        }else if(this.status == 1){
            command = this.aDriver.accelerate(inputs);
        }else if(this.status == 2){
            command = this.aDriver.decelerate(inputs);
        }else if(this.status == 3){
            command = this.aDriver.turn(inputs);
        }else if(this.status == 4){
            command = this.aDriver.adjust(inputs);
        }
//		System.out.print(command);
        return command;
    }
}

class Driver extends Object {

    public Driver(){
        return;
    }

    public int accelerate(SensorModel inputs){
        return 0;
    }

    public int decelerate(SensorModel inputs){
        return 0;
    }

    public int turn(SensorModel inputs){
        return 0;
    }

    public int adjust(SensorModel inputs){
        return 0;
    }
}
