package simplerace.e;
import simplerace.*;

import java.util.List;
import java.util.ArrayList;

public class DataCenter extends Object{

    /**
     * 自分自身を束縛する唯一のフィールドである.
     */
    private static DataCenter singleton = null;

    /**
     * コントローラを束縛するフィールドである.
     */
    private List<AIController> controllers;

    /**
     * データセンタのコンストラクタである.
     * シングルトンデザインパターンを採用するため、private修飾子をつけている.
     */
    private DataCenter(){
        this.controllers = new ArrayList<>();

        return;
    }

    /**
     * データセンタのインスタンスを返す.
     * @return 自身のインスタンス
     */
    public static DataCenter getSingleton() {
        if(DataCenter.singleton == null) DataCenter.singleton = new DataCenter();

        return DataCenter.singleton;
    }

    /**
     * AIControllerを登録する
     * @param aController
     */
    public void register(AIController aController){
        controllers.add(aController);

        return;
    }

    public void println(){
        for (AIController aController: controllers) {
            System.out.println(aController.toString() + ", Angle = " + aController.getTargetAngle());
        }
    }

    /**
     * オペレーションを行う.
     * @return
     */
    public Vector2d operation(AIController anAIController){

        double min = 500000000.0;
        int index = -1;

        for(int i = 0;i < this.controllers.size();i++){
            if(this.controllers.get(i).getSensor().getDistanceToNextWaypoint() < min){
                min = this.controllers.get(i).getSensor().getDistanceToNextWaypoint();
                index = i;
            }
        }

        if(anAIController != this.controllers.get(index)) return anAIController.getSensor().getNextNextWaypointPosition();

        return anAIController.getSensor().getNextWaypointPosition();
    }
}