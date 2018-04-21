package simplerace.e;
import simplerace.*;

/**
 * AIController
 * Todo
 * それぞれAIControllerに次旗、次次旗にかかる時間を計算させる
 * その計算結果をデータセンタに送る
 * データセンタがそれを見て、どちらの旗に行くべきかオペレーションする
 * 行くべき旗がわかるので、そちらに進む
 */

public class AIController implements Controller, Constants {

    private SensorModel inputs;

    private double targetAngle;

    private Vector2d targetFlag;

    private Analyst analyst;

    private boolean added = false;

    public void reset(){
        this.analyst = new Analyst();
    }

    private void setField(SensorModel inputs){
        this.inputs = inputs;

        this.targetFlag = DataCenter.getSingleton().operation(this);

        this.targetAngle = Calculator.getAngleBetweenCarAndWaypoint(this, this.targetFlag);

    }

    /**
     * ControllerをImplementsすると必要になる
     * 実際に操縦を行うメソッド
     *
     * @param inputs センサ情報
     * @return 操縦コマンド
     */
    public int control (SensorModel inputs) {

        if(!added) {
            DataCenter.getSingleton().register(this);
            this.added = true;
        }

        int command = neutral;

        this.setField(inputs);

        if(this.targetAngle > 0){
            command = backwardleft;

            if(this.targetAngle > 3.0) command = backward;
        }else{
            command = backwardright;

            if(this.targetAngle < -3.0) command = backward;
        }

        //System.err.println(inputs.getClass());

        //DataCenter.getSingleton().println();

        return command;
    }

    /**
     * Vector2dクラスの同値比較を行うメソッド
     * @param a 一つ目のVector2dインスタンス
     * @param b 二つ目
     * @return 同値ならtrue、違うならfalse
     */
    public boolean eq(Vector2d a, Vector2d b){
        if(a == null || b == null) return false;

        if(a == b) return true;
        if(a.equals(b)) return true;

        if(a.x == b.x && a.y == b.y) return true;

        return false;
    }

    /**
     * ターン開始時に行う操作
     * @author takaesumizuki
     */
    private void turnStartProcess(SensorModel inputs) {
        this.analyst.update(inputs); /* 統計情報をとる */

        return;
    }

    /**
     * ターン終了時に行う操作
     * @author takaesumizuki
     */
    private void turnEndProcess() {
        // this.analyst.printResult(); /* ターンごとの結果を表示したくないならコメントアウトしてください */
        if (this.analyst.isLastTurn()) {
            // this.analyst.printResult(); /* ゲームごと(ラウンドごと)の結果を表示したくないならコメントアウトしてください */
            if (this.analyst.isFinalRound()) {
                this.analyst.finalRoundProcess();/* 最終結果を表示したくないならコメントアウトしてください*/
            }
        }

        return;
    }

    /**
     * 自身のセンサ情報を返す.
     * @return
     */
    public SensorModel getSensor() {
        return this.inputs;
    }

    /**
     * 自身から1つめのWaypointへの角度を返す.
     * @return
     */
    protected double getTargetAngle(){
        return this.targetAngle;
    }

    /**
     * 自身の文字列を応答する
     * @return 自身の文字列
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("AIController");
        sb.append(this.hashCode());
        return sb.toString();
    }

    /**
     * 自身を表すハッシュ値を応答する
     * @return
     */
    public int hashCode(){

        return super.hashCode();
    }
}

