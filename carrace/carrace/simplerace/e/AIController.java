package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    private double targetAngle;
    
    public void reset(){}

    public int control (SensorModel inputs) {

        System.out.println("Speed = " + inputs.getSpeed());

        int command=backwardleft;

        return command;
    }
}
