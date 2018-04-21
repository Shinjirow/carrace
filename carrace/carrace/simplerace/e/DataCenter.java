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

    /**
     * オペレーションを行う.
     * @return そのAIが狙うべき旗の情報
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