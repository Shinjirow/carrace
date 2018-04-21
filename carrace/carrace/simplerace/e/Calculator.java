package simplerace.e;
import simplerace.*;

/**
 * カーレースにおける様々な計算をクラスメソッドとして提供するクラスである.
 */
public class Calculator extends Object{

    public static final double DIAGONAL = Math.sqrt(320000.0D);

    public Calculator(){
    }

    /**
     * 二つのポジション間の角度を求める
     * @param from : Vector2d
     * @param to : Vector2d
     * @return 角度
     */
    public static double getAngleToTwoPositions(Vector2d from, Vector2d to) {
        double xDiff = to.x - from.x;
        double yDiff = to.y - from.y;

        return Math.atan2(yDiff, xDiff);
    }

    /**
     * AIControllerとあるVector2dとの角度を求める
     * @param from :  AIController
     * @param waypoint : Vector2d
     * @return 角度
     */
    public static double getAngleBetweenCarAndWaypoint(AIController from, Vector2d waypoint){
        Vector2d position = from.getSensor().getPosition();
        double angle = Calculator.getAngleToTwoPositions(position, waypoint);
        angle = from.getSensor().getOrientation() - angle;

        return Utils.correctAngle(angle);
    }

    /**
     * AIControllerとVector2dとの距離を求める
     * @return 距離を対角線で割った値
     */
    public static double getDistanceBetweenCarAndWaypoint(AIController from, Vector2d waypoint){
        return from.getSensor().getPosition().dist(waypoint) / Calculator.DIAGONAL;
    }

    /**
     * Vector2dクラスの同値比較を行うメソッド
     * @param a 一つ目のVector2dインスタンス
     * @param b 二つ目
     * @return 同値ならtrue、違うならfalse
     */
    public static boolean areTheyEqual(Vector2d a, Vector2d b){
        if(a == null || b == null) return false;

        if(a == b) return true;
        if(a.equals(b)) return true;

        if(a.x == b.x && a.y == b.y) return true;

        return false;
    }
}