package simplerace.e;
import simplerace.*;

public class Calculator extends Object{
    public Calculator(){

    }

    public static double getAngleToTwoPositions(Vector2d from, Vector2d to) {
        double xDiff = to.x - from.x;
        double yDiff = to.y - from.y;
        return Math.atan2(yDiff, xDiff);
    }


    public static double getAngleBetweenCarAndWaypoint(AIController from, Vector2d Waypoint){
        Vector2d position = from.getSensor().getPosition();
        double xDiff = Waypoint.x - position.x;
        double yDiff = Waypoint.y - position.y;
        double angle = Math.atan2(yDiff, xDiff);
        angle = from.getSensor().getOrientation() - angle;
        return Calculator.correctAngle(angle);

    }

    public static double correctAngle(double angle) {
        while(angle < -3.141592653589793D) {
            angle += 6.283185307179586D;
        }

        while(angle > 3.141592653589793D) {
            angle -= 6.283185307179586D;
        }

        return angle;
    }
}