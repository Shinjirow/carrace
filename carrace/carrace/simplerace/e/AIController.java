package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    private double targetAngle;

    boolean flag = false;

    Vector2d startpos;

    public void reset(){}

    public int control (SensorModel inputs) {

        System.out.println("Speed = " + inputs.getSpeed());

        int command=backward;

        if(!flag) if(inputs.getSpeed() < -10) {flag = true;startpos = inputs.getPosition();}

        if(flag) if(inputs.getSpeed() < 0)command = forward;else command = neutral;

        System.out.println("position = " + inputs.getPosition());

        return command;
    }
}
