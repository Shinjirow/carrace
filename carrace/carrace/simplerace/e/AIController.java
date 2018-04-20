package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    public void reset(){}

    public int control (SensorModel inputs) {

        int command = neutral;

        if(inputs.getAngleToNextWaypoint() > 0){
            command = forwardleft;
        }else{
            command = forwardright;
        }

        return command;
    }
}
