package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    static int FORWARD = 0;
    static int BRAKING = 1;
    static int TURNING = 2;
    int status=FORWARD;

    int turnValue=0;

    double targetAngle;

    public void show(int n){
        System.out.println(n);
    }

    public int verySlowTurnRight(){
        //System.out.println("summoned");
        if(turnValue == 0){
            return forwardright;
        }
        turnValue++;
        if(turnValue >= 50) turnValue=0;
        return right;
    }

    public int verySlowTurnLeft(){
        //System.out.println("summoned!!");
        if(turnValue == 0){
            return forwardleft;
        }
        turnValue++;
        if(turnValue >= 50) turnValue=0;
        return left;
    }

    public int slowTurnRight(){
        //System.out.println("summoned!");
        if(turnValue == 0){
            return forwardright;
        }
        turnValue++;
        if(turnValue >= 3) turnValue=0;
        return right;
    }

    public int slowTurnLeft(){
        //System.out.println("summoned!!!!");
        if(turnValue == 0){
            return forwardleft;
        }
        turnValue++;
        if(turnValue >= 3) turnValue=0;
        return left;
    }

    public void reset() {}

    public int control (SensorModel inputs) {

        //return verySlowTurnLeft();

        /*
        int command;


        if(a==0) command=forwardright;
        else command=forward;

        a++;
        if(a>=3) a=0;

        //show(command);
        //System.out.println("getSpeed(): " + inputs.getSpeed());
        //System.out.println("getPosition().x: " + inputs.getPosition().x);
        */


        Vector2d myPos = inputs.getPosition();
        Vector2d target1 = inputs.getNextWaypointPosition();
        Vector2d target2 = inputs.getNextNextWaypointPosition();
        double targetDistance = inputs.getDistanceToNextWaypoint();
        double currentSpeed = inputs.getSpeed();

		int command=neutral;
		double targetAngle = inputs.getAngleToNextWaypoint();
		//System.out.println(targetAngle);

		if(status==FORWARD){
		    if(targetAngle < 0.05 && targetAngle > -0.05){
		        command = forward;
		        if(targetDistance < 0.3 && currentSpeed >= 4.5){
		            command = neutral;
		            if(targetDistance < 0.18 && currentSpeed >= 2.36){
		                command = backward;
                    }
                }
            }
            if(targetAngle > 0){
                if(targetAngle > 0.3)
                    return slowTurnLeft();

                if(targetAngle > 0.6)
                    return verySlowTurnLeft();

                command=forwardleft;
                if(targetDistance < 0.3 && currentSpeed >= 4.5){
                    command=left;
                    if(targetDistance < 0.18 && currentSpeed >= 2.36){
                        command = backwardleft;
                    }
                }
            } else {
                if(targetAngle < -0.3)
                    return slowTurnRight();

                if(targetAngle < -0.6)
                    return verySlowTurnRight();

                command=forwardright;
                if(targetDistance < 0.3 && currentSpeed >= 4.5){
                    command=right;
                    if(targetDistance < 0.18 && currentSpeed >= 2.36){
                        command = backwardright;
                    }
                }
            }

        }


//		System.out.print(command);
        return command;


    }
}
