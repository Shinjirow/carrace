package simplerace.e;
import simplerace.*;

/**
 * カーレースにおける様々な計算をクラスメソッドとして提供するクラスである.
 */
public class Calculator extends Object{

    /**
     * DIAGONAL
     * 対角線の値 割ったりかけたりして使うといいと思う
     */
    public static final double DIAGONAL = Math.sqrt(320000.0D);

    /**
     * バックでbackwardleftとかを押しっぱなしにして収束する旋回速度
     * ここを最低速度としてあらゆる行動を行う
     */
    private static final double lowestTurnSpeed = -2.564335;

    /**
     * コンストラクタ
     * 現在はインスタンスを作る必要がないので中身は空
     */
    public Calculator(){
    }

    /**
     * getAngleToTwoPositions
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
     * getAngleBetweenCarAndWaypoint
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
     * getDistanceBetweenCarAndWaypoint
     * AIControllerとVector2dとの距離を求める
     * @return 距離を対角線で割った値
     */
    public static double getDistanceBetweenCarAndWaypoint(AIController from, Vector2d waypoint){
        return from.getSensor().getPosition().dist(waypoint) / Calculator.DIAGONAL;
    }

    /**
     * areTheyEqual
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

    /**
     * simlate
     * ここからそこまで何フレームでいけるの？を計算してくれるメソッド
     * 現在は距離のみで判定する
     */
    public static int simulate(AIController from, Vector2d waypoint){
        int estimate = 0;
        double distance = Calculator.getDistanceBetweenCarAndWaypoint(from, waypoint) * Calculator.DIAGONAL - 20.0;
        distance += 2.0 / Math.abs(Calculator.getAngleBetweenCarAndWaypoint(from, waypoint));
        double p1 = 0.0, p2 = distance;
        double currentSpeed = from.getSensor().getSpeed();
        double bottomSpeed = areTheyEqual(waypoint, DataCenter.getSingleton().getFirstFlag()) ? Calculator.lowestTurnSpeed : 0.0;

        for(;;estimate+=2){
            if(p1 > p2) break;
            p1 += -currentSpeed;
            p2 -= -bottomSpeed;
            currentSpeed += -0.2;
            bottomSpeed += -0.4;
            //System.err.println(p1 + ", " + p2);
        }

        return estimate;
    }
}