package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {



    int f = 0;

    public void reset(){}

    private boolean isTooClose(SensorModel inputs){
        double mergin = 12.5 + 20.0;
        //System.err.println(inputs.getDistanceToNextWaypoint()*Math.sqrt(320000.0D) < mergin && Math.abs(inputs.getAngleToNextWaypoint()) < Math.PI*0.6);
        
        return (inputs.getDistanceToNextWaypoint()*Math.sqrt(320000.0D) < mergin && Math.abs(inputs.getAngleToNextWaypoint()) < Math.PI*0.8) ? true : false;
    }

    /**
     * reverseLR
     * コマンドの左右を反転させる
     */
    private int reverseLR(int cmd){
        int command = cmd;
        if(cmd == forwardleft)        command = forwardright;
        else if(cmd == forwardright)  command = forwardleft;
        else if(cmd == backwardright) command = backwardleft;
        else if(cmd == backwardleft)  command = backwardright;
        else if(cmd == left)          command = right;
        else if(cmd == right)         command = left;
        return command;
    }

    public int control (SensorModel inputs) {

        int command = backwardleft;

        //System.out.println(inputs.getDistanceToNextWaypoint()*Math.sqrt(32000.0D));


        
        if(inputs.getAngleToNextWaypoint() > 0){
            command = backwardleft;
        }else{
            command = backwardright;
        }
         
        if(this.isTooClose(inputs)) command = reverseLR(command);

        f++;

        return command;
    }
}
