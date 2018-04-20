package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {



    int f = 0;

    public void reset(){}

    public int control (SensorModel inputs) {

        int command = backwardleft;

        System.out.println(inputs.getDistanceToNextWaypoint()*Math.sqrt(32000.0D));


        /**
        if(inputs.getAngleToNextWaypoint() > 0){
            command = backwardleft;
        }else{
            command = backwardright;
        }
         */

        f++;

        return command;
    }
}
