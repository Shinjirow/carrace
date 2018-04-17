package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    private double targetAngle;

    private double targetDistance;

    private Vector2d nextWaypoint;

    private double brakingPoint;

    public void reset(){}

    public int control (SensorModel inputs) {
        int command = neutral;

        this.targetAngle = inputs.getAngleToNextWaypoint();
        this.targetDistance = inputs.getDistanceToNextWaypoint();
        if(!eq(this.nextWaypoint, inputs.getNextWaypointPosition())){
            //System.out.println("summon");
            this.nextWaypoint = inputs.getNextWaypointPosition();
            this.brakingPoint = inputs.getDistanceToNextWaypoint() * 0.20;
        }

        //System.out.println(brakingPoint);

        //System.out.println(this.targetDistance);


        if(this.targetAngle > 0){
            command = backwardleft;

            if(this.targetDistance < this.brakingPoint){
                command = forwardleft;
            }
        }else{
            command = backwardright;

            if(this.targetDistance < this.brakingPoint){
                command = forwardright;
            }
        }

        return command;
    }

    public boolean eq(Vector2d a, Vector2d b){
        if(a == null || b == null) return false;

        if(a == b) return true;
        if(a.equals(b)) return true;

        if(a.x == b.x && a.y == b.y) return true;

        return false;
    }
}
