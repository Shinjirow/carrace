package simplerace.e;
import simplerace.*;

/**
 * AIController
 */

public class AIController implements Controller, Constants {

    /**
     * デバッグ用変数
     */
    private boolean DEBUG = false;

    /**
     * 自身のセンサ情報が束縛されるフィールド.
     */
    private SensorModel inputs;

    /**
     * 狙うべき旗の位置と自身の角度が代入されるフィールド.
     */
    private double targetAngle;

    /**
     * こいつが狙うべき旗の位置が束縛されるフィールド.
     */
    private Vector2d targetFlag;

    /**
     * バックでbackwardleftとかを押しっぱなしにして収束する旋回速度
     * ここを最低速度としてあらゆる行動を行う
     */
    private final double lowestTurnSpeed = -2.564335;

    /**
     * 当たり判定の距離を対角線で割った値
     * getDistanceシリーズは対角線で割った値が求められるので作った
     */
    private final double collideDetection = 20.0 / Calculator.DIAGONAL;

    /**
     * コマンドが入る配列
     * 自車を上から見下ろした時の8方位+ニュートラル
     */
    int[][] commands = {{forwardleft,  forward,  forwardright},
                        {left,         neutral,  right},
                        {backwardleft, backward, backwardright}};

    /**
     * 統計情報を取る
     */
    private Analyst analyst;

    public void reset(){
        this.analyst = new Analyst();
    }

    /**
     * 色々更新する
     * @param inputs : センサ情報
     */
    private void update(SensorModel inputs){
        this.inputs = inputs;
        DataCenter.getSingleton().update(this);
        this.targetFlag = DataCenter.getSingleton().operation(this);
        this.targetAngle = Calculator.getAngleBetweenCarAndWaypoint(this, this.targetFlag);
    }

    /**
     * isAbleToBrake 今からブレーキして目標速度まで落とすと止まり切れるか返す
     *
     * 　　n      ♪キボウノハナー
     *　 _H
     *　巛 ｸ　 ノﾚzz　　　　　　　　俺は止まんねえからよ...
     *　 F｜　幺 ﾐwｯﾐ
     *　｜｜　ヽﾚvvｲ             お前らが止まんねえ限り、
     *　｜ ￣⌒＼二ヽ＿
     *　 ￣￣Ｙ　ミ　 /|       俺はその先にいるぞ！！！！！
     *　　　 ｜　 |　｜|
     *　　　 /　　|　｜|
     *　　　/　　 |　 L|           だからよ...
     *　　　＼＿_/|＿/(ヽ
     *　　　 ｜　　 ｜/ぐ)
     *　　　 ｜　 ﾊ ∧＼≫        止まるんじゃねえぞ...
     *　　　 ｜　/ Ｖ∧
     *　　　 ｜ ｜　Ｖ｜
     * @return true 止まり切れる : false 止まり切れない(ブレーキしなさい)
     */
    private boolean isAbleToBrake(){
        double finPoint = (Calculator.getDistanceBetweenCarAndWaypoint(this, this.targetFlag) - this.collideDetection) * Calculator.DIAGONAL;
        double speed = this.inputs.getSpeed();
        double targetSpeed = 0;
        if(Calculator.areTheyEqual(this.targetFlag, DataCenter.getSingleton().getFirstFlag()))
            targetSpeed = this.lowestTurnSpeed - 2.0;
        
        double distance = 0;
        if(targetSpeed < speed) return true;

        while(true){
            if(speed >= targetSpeed) break;
            distance -= speed;
            speed += 0.425;
        }

        return (distance > finPoint) ? false : true;
    }

    /**
     * isTooClose
     * 目的旗が自身の最小旋回半径より内側にいるかをチェックする
     * @return true 内側にいる場合 : false 外側にいる場合
     */
    private boolean isTooClose(){
        if(this.inputs.getSpeed() < - 3.5) return false;
        double mergin = 12.5 + 20.0;
        
        return (Calculator.getDistanceBetweenCarAndWaypoint(this, this.targetFlag)*Calculator.DIAGONAL < mergin && Math.abs(this.targetAngle) < Math.PI*0.8) ? true : false;
    }

    /**
     * reverseLR
     * コマンドの左右を反転させる
     * @param cmd コマンド
     * @return コマンドを左右反転させた値
     */
    private int reverseLR(int cmd){
        for(int i = 0;i < commands.length;i++){
            for(int j = 0;j < commands[i].length;j++){
                if(j == 1) continue;
                if(cmd == commands[i][j]) return commands[i][commands[i].length - j - 1];
            }
        }
        return cmd;
    }

    /**
     * reverseFW
     * コマンドの前後を反転させる
     * @param cmd コマンド
     * @return コマンドを前後反転させた値
     */
    private int reverseFW(int cmd){
        for(int i = 0;i < commands.length;i++){
            for(int j = 0;j < commands[i].length;j++){
                if(i == 1) continue;
                if(cmd == commands[i][j]) return commands[commands.length - 1 - i][j];
            }
        }
        return cmd;
    }

    /**
     * ControllerをImplementsすると必要になる
     * 実際に操縦を行うメソッド
     *
     * @param inputs センサ情報
     * @return 操縦コマンド
     */
    public int control (SensorModel inputs) {

        //this.turnStartProcess(inputs);

        int command = neutral;

        this.update(inputs);

        if(this.targetAngle > 0){
            command = backwardleft;
            if(this.targetAngle > 3.0) command = backward;
        }else{
            command = backwardright;
            if(this.targetAngle < -3.0) command = backward;
        }

        if(!this.isAbleToBrake()) command = this.reverseFW(command);
        if(this.isTooClose()) command = this.reverseLR(command);

        //this.turnEndProcess();

        if(DEBUG) this.doSlowly(1000);

        return command;
    }

    /**
     * doSlowly
     * とりあえずvalミリ秒sleepさせる
     * 時間が欲しいときにねじ込んでください
     * @param val : sleepさせる時間 [ms]
     */
    private void doSlowly(long val){
        try{
            Thread.sleep(val);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        return;
    }

    /*-------------------------Analyze------------------------------*/

    /**
     * ターン開始時に行う操作
     * @param inputs センサ情報
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

    /*---------------------------getter-----------------------------*/

    /**
     * 自身のセンサ情報を返す.
     * @return 自身のセンサ情報
     */
    public SensorModel getSensor() {
        return this.inputs;
    }

    /**
     * 自身から1つめのWaypointへの角度を返す.
     * @return 自身から1つ目のWaypointへの角度
     * -π <= returnValue <= π
     */
    protected double getTargetAngle(){
        return this.targetAngle;
    }

    /*--------------------------------------------------------------*/

    /**
     * 自身の文字列を応答する
     * @return 自身の文字列
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("AIController");
        sb.append(this.hashCode());
        sb.append(" : ");
        return sb.toString();
    }

    /**
     * 自身を表すハッシュ値を応答する
     * @return 自身のハッシュ値
     */
    @Override
    public int hashCode(){

        return super.hashCode();
    }
}

