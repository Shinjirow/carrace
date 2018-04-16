package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    private boolean DEBUG = false;

    private int status = 0;

    private double targetAngle;

    private Driver aDriver = new Driver(this);

    public void reset() {}

    public void transition(){
        this.status++;
        if(this.status == 4) this.status = 0;
        return;
    }

    public int control (SensorModel inputs) {
        if(DEBUG) System.out.println(status);

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
        }
//		System.out.print(command);
        return command;
    }
}

class Driver extends Object implements Constants {

    private boolean DEBUG = false;

    private Vector2d currentTarget = null;

    private AIController aiController;

    public Driver(AIController aiC){
        this.aiController=aiC;
        return;
    }

    public void setTarget(Vector2d target){
        this.currentTarget = target;
        return;
    }

    //---controls----

    public int accelerate(SensorModel inputs){
        //if(DEBUG) System.out.println("distance = "+inputs.getDistanceToNextWaypoint());

        if(inputs.getDistanceToNextWaypoint()<0.25) aiController.transition();

        int cmd=forward;
        if(inputs.getAngleToNextWaypoint()< -0.1){
            cmd=forwardright;
        }else if(inputs.getAngleToNextWaypoint() > 0.1){
            cmd=forwardleft;
        }
        return cmd;
    }

    public int decelerate(SensorModel inputs){
        if(inputs.getSpeed() < 2.3) aiController.transition();

        int cmd=backward;
        return cmd;
    }

    public int turn(SensorModel inputs){
        if(this.currentTarget==null) this.setTarget(inputs.getNextWaypointPosition());
        if(inputs.getNextWaypointPosition() != this.currentTarget) {
            aiController.transition();
            this.setTarget(inputs.getNextWaypointPosition());
        }

        int cmd=neutral;
        if(inputs.getAngleToNextWaypoint()<0.0){
            cmd=forwardright;
        }else{
            cmd=forwardleft;
        }
        return cmd;
    }

    public int adjust(SensorModel inputs){
        if(inputs.getAngleToNextWaypoint() < 0.1 && inputs.getAngleToNextWaypoint() > -0.1) aiController.transition();

        if(DEBUG) System.out.println("angle = " + inputs.getAngleToNextWaypoint() + ", distance = " + inputs.getDistanceToNextWaypoint());

        int cmd=neutral;
        if(inputs.getAngleToNextWaypoint()<0.0){
            if(inputs.getSpeed() > 1.8)
                cmd=right;
            else
                cmd=forwardright;
        }else{
            if(inputs.getSpeed() > 1.8)
                cmd=left;
            else
                cmd=forwardleft;
        }

        if(inputs.getAngleToNextWaypoint() > 1.3 && inputs.getDistanceToNextWaypoint() < 0.07) cmd = forward;
        if(inputs.getAngleToNextWaypoint() < -1.3 && inputs.getDistanceToNextWaypoint() < 0.07) cmd = forward;

        return cmd;
    }
}