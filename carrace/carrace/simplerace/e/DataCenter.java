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
    private List<Vector2d> waypoints;

    /**
     * データセンタのコンストラクタである.
     * シングルトンデザインパターンを採用するため、private修飾子をつけている.
     */
    private DataCenter(){
        this.controllers = new ArrayList<>();
        this.waypoints = new ArrayList<>(2);
        this.waypoints.add(null); this.waypoints.add(null);
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
     * AIControllerからのセンサ情報を元にアップデートする
     * @param anAIController : アップデート要求をしてきたAIController
     */
    public void update(AIController anAIController){
        if(this.controllers.indexOf(anAIController) == -1)
            this.controllers.add(anAIController);

        this.waypoints.set(0, anAIController.getSensor().getNextWaypointPosition());
        this.waypoints.set(1, anAIController.getSensor().getNextNextWaypointPosition());
        
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
        int mini = 500000000;
        int index = -1;
        double distance;
        for(int i = 0;i < this.controllers.size();i++){
            
            distance = this.controllers.get(i).getSensor().getDistanceToNextWaypoint();
            distance += 2.0 / Math.abs(Calculator.getAngleBetweenCarAndWaypoint(this.controllers.get(i), this.waypoints.get(0)));
            if(distance < min){
                min = distance;
                index = i;
            }
        }

        return (anAIController != this.controllers.get(index)) ? this.getSecondFlag() : this.getFirstFlag();
    }

    /**
     * getFirstFlag
     * NextWaypointの情報を返す
     * @return NextWaypoint : Vector2d
     */
    public Vector2d getFirstFlag(){
        return this.waypoints.get(0);
    }

    /**
     * getFirstFlag
     * NextNextWaypointの情報を返す
     * @return NextNextWaypoint : Vector2d
     */
    public Vector2d getSecondFlag(){
        return this.waypoints.get(1);
    }
}