package simplerace.e;
import simplerace.*;

/**
 * AIControler
 */

public class AIController implements Controller, Constants {

    /**
     * デバッグ用変数
     * メインプログラムで1フレームごとに500msの空白を与えるので、
     * 考える時間が稼げる
     */
    private final boolean DEBUG = false;

    /**
     * コーナーに進入する際の目標速度が代入されるフィールド.
     */
    private double targetSpeed;

    /**
     * 自分、1旗、2旗のangle
     * 0 <= angle <= pi
     */
    private double angle;

    /**
     * angleの2乗
     * 0 <= angle <= pi*pi
     */
    private double anglePow;

    /**
     * 仮設旗を束縛するフィールド.
     */
    private Vector2d fakeWaypoint;

    /**
     * 仮設旗との角度が代入されるフィールド.
     */
    private double fakeAngle;

    /**
     * コーナーに進入するための減速地点が代入されるフィールド.
     */
    private double brakingPoint;

    /**
     * 高江洲が作った統計を取る用の変数を束縛するフィールド.
     */
    private Analyst analyst;

    /**
     * Math.PIの2乗 angleの2乗に比例させたいが大きくなりすぎるために作ったはず
     */
    private final double pipi = Math.PI * Math.PI;

    /**
     * バックでbackwardleftとかを押しっぱなしにして収束する旋回速度
     * ここを最低速度としてあらゆる行動を行う
     */
    private final double defTurnSpeed = -2.564335;

    /**
     * 当たり判定の距離を対角線で割った値
     * getDistanceシリーズは対角線で割った値が求められるので作った
     */
    private final double collideDetection = 20.0/Math.sqrt(320000.0D);

    /**
     * コンストラクタ.
     * 使われないらしい
     */
    public AIController(){
        this.analyst = new Analyst();

        return;
    }

    /**
     * Controllerをimplementする際に必要なメソッド.
     * 統計処理を行うAnalystをここでnewする
     */
    public void reset(){
        this.analyst = new Analyst();
    }


    /**
     * そのターンでの行動を決めるのに最低限必要なフィールドを埋める
     * @param inputs センサ情報
     */
    private void setField(SensorModel inputs){
        this.angle = this.getRadians(inputs);
        this.anglePow = this.angle*this.angle;
        this.targetSpeed = this.defTurnSpeed * (anglePow/2.0 + 1.0);

        return;
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
     * @param inputs センサ情報
     * @return true 止まり切れる : false 止まり切れない(ブレーキしなさい)
     */
    private boolean isAbleToBrake(SensorModel inputs){
        double finPoint = (inputs.getDistanceToNextWaypoint() - this.collideDetection) * Math.sqrt(320000.0D);
        double speed = inputs.getSpeed();
        double distance = 0;

        if(this.targetSpeed < speed) return true;

        while(true){
            if(speed >= this.targetSpeed) break;
            distance -= speed;
            speed += 0.425;
        }

        if(distance > finPoint) return false;

        return true;
    }

    /**
     * 自身、一つ目の旗、二つ目の旗の角度を求める
     *
     * 4/19 偽旗で角度を出すように変更
     * @param inputs センサ情報
     * @return -PI ~ PIの値の範囲で角度
     */
    private double getRadians(SensorModel inputs){
        Vector2d A = inputs.getPosition();
        Vector2d B = this.fakeWaypoint;
        Vector2d C = inputs.getNextNextWaypointPosition();
        Vector2d BA = new Vector2d(A.x - B.x, A.y - B.y);
        Vector2d BC = new Vector2d(C.x - B.x, C.y - B.y);

        double cosB = (BA.x*BC.x+BC.y*BA.y)/((Math.sqrt(Math.pow(BA.x,2.0)+Math.pow(BA.y,2.0)))*(Math.sqrt(Math.pow(BC.x,2.0)+Math.pow(BC.y,2.0))));

        double rad = Math.acos(cosB);

        return (rad > Math.PI) ? (Math.PI - rad) : rad;
    }


    /**
     * 偽の旗を生成する
     * @param inputs センサ情報
     * @return 偽の旗
     */
    private Vector2d createFakeWaypoint(SensorModel inputs){
        Vector2d T1 = inputs.getNextWaypointPosition();
        Vector2d T2 = inputs.getNextNextWaypointPosition();

        double T1T2Distance = Math.sqrt((T2.x - T1.x) * (T2.x - T1.x) + (T2.y - T1.y) * (T2.y - T1.y));
        double radian = Math.atan2(T2.y - T1.y,T2.x - T1.x);
        double distance = inputs.getDistanceToNextWaypoint()*Math.sqrt(320000.0D);

        Vector2d fakePoint = new Vector2d(T1.x - (Math.cos(radian) * distance*distance/143.75 * 0.47) + (Math.cos(radian)*20.0),
                                           T1.y - (Math.sin(radian) * distance*distance/143.75 * 0.47) + (Math.sin(radian)*20.0));

        return fakePoint;
    }

    /**
     * 偽旗との角度を求める
     * @param inputs センサ情報
     * @return 角度
     */
    private double getAngleToFakeWaypoint(SensorModel inputs){
        Vector2d nextWp = this.fakeWaypoint;
        Vector2d position = inputs.getPosition();
        double angle = Math.atan2(nextWp.y - position.y, nextWp.x - position.x);
        angle = inputs.getOrientation() - angle;

        while(angle < -3.141592653589793D) angle += 6.283185307179586D;
        while(angle > 3.141592653589793D) angle -= 6.283185307179586D;

        return angle;
    }

    /**
     * ControllerをImplementsすると必要になる
     * 実際に操縦を行うメソッド
     * 4/17 配布プログラムをそのままバックするようにしただけ(average15)
     *      高速で旗に突っ込むのを改善するためにブレーキ開始地点を設定(average17)
     *
     * 4/18 減速しすぎるため、一定速度まで落ちたら惰性で旗取り
     *      それに合わせてブレーキングポイントを手前に移動(average17.9~18.5)
     *
     *      直進性能を上げた(average18.1~19.0), 10000試行で18.8
     *
     *      必要な旋回角に応じて減速距離が変わるようにした 18.8
     *
     * 4/19 最低限減速に必要な距離を与えた 19.0
     *
     *      減速方式を変えた isAbleToBrakeで判定するように 19.3
     *      isAbleToBrakeのシミュレート精度の改善 19.45
     *
     *      仮想的に旗を設置し、そちらを追うことで回り込んで旗を取れるようにした 20.3
     *
     *      インベタで走るコードがバグってたので修正 21.84
     *
     * 4/20 旗の設置方法を距離の2乗に比例するようにした 22.18
     *
     *      各種変数の最適化 22.5
     *
     * @param inputs センサ情報
     * @return 操縦コマンド
     */
    public int control (SensorModel inputs) {

        this.turnStartProcess(inputs);

        this.fakeWaypoint = this.createFakeWaypoint(inputs);
        this.fakeAngle = this.getAngleToFakeWaypoint(inputs);

        int command;

        this.setField(inputs);

        if(this.fakeAngle > 0){
            command = backwardleft;

            if(this.fakeAngle > 3.00) command = backward;

            if(!isAbleToBrake(inputs)) command = forwardleft;

        }else{
            command = backwardright;

            if(this.fakeAngle < -3.00) command = backward;

            if(!isAbleToBrake(inputs)) command = forwardright;
        }

        if(DEBUG){
            try{
                Thread.sleep(500);
            }catch(InterruptedException exception){
                exception.printStackTrace();
            }
        }

        this.turnEndProcess();

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
}

