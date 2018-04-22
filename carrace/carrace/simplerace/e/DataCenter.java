package simplerace.e;
import simplerace.*;

import java.util.List;
import java.util.ArrayList;

/**
 * 2台のAIコントローラの情報を取りもつクラスである.
 * AIコントローラにgetterをいくつか整備することで、それらから必要な情報を得ている.
 * それらの情報を元に、2台のコントローラに、次に狙うべき旗を支持する.
 */
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
     * 次の旗の位置が束縛されるフィールド.
     */
    private Vector2d nextWaypoint;

    /**
     * 次の次の旗の位置が束縛されるフィールド.
     */
    private Vector2d nextNextWaypoint;

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
        if(DataCenter.singleton == null)
            DataCenter.singleton = new DataCenter();

        return DataCenter.singleton;
    }

    /**
     * AIControllerのセンサ情報を元にアップデートする
     * @param anAIController : アップデート要求をしてきたAIController
     */
    public void update(AIController anAIController){
        if(this.controllers.indexOf(anAIController) == -1)
            this.controllers.add(anAIController);
        
        this.nextWaypoint = anAIController.getSensor().getNextWaypointPosition();
        this.nextNextWaypoint = anAIController.getSensor().getNextNextWaypointPosition();

        return;
    }

    /**
     * operation
     * 受け取ったAIControllerに対してオペレーションを行う.
     * @param anAIController : AIController
     * @return そのAIが狙うべき旗の情報
     */
    public Vector2d operation(AIController anAIController){

        double min = 500000000.0;
        int index = -1;
        double distance;
        for(int i = 0;i < this.controllers.size();i++){
            distance = this.controllers.get(i).getSensor().getDistanceToNextWaypoint();
            distance += 2.0 / Math.abs(Calculator.getAngleBetweenCarAndWaypoint(this.controllers.get(i), this.nextWaypoint));
            if(distance < min){
                min = distance;
                index = i;
            }
        }
        int frame = Calculator.simulate(anAIController, this.nextWaypoint);
        System.err.println(anAIController.toString() + ": " + frame);

        if(anAIController != this.controllers.get(index))
            return this.nextNextWaypoint;

        return this.nextWaypoint;
    }

    /**
     * getFirstFlag
     * NextWaypointの情報を返す
     * @return NextWaypoint : Vector2d
     */
    public Vector2d getFirstFlag(){
        return this.nextWaypoint;
    }

    /**
     * getFirstFlag
     * NextNextWaypointの情報を返す
     * @return NextNextWaypoint : Vector2d
     */
    public Vector2d getSecondFlag(){
        return this.nextNextWaypoint;
    }
}