package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

    private double targetAngle;

    private int stats = 0;

    long fl=0;

    public void reset(){}

    public int control (SensorModel inputs) {

        System.out.println("f = "+ fl + ", Speed = " + inputs.getSpeed());

        int command=neutral;

        if(stats==0){
            command=forward;
            if(inputs.getSpeed()>10.0) this.stats=1;
        }else if(stats == 1){
            command=backward;
            if(inputs.getSpeed()<0.0) this.stats=2;
        }

        fl++;

        return command;
    }
}
