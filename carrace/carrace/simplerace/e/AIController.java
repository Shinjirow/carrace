package simplerace.e;
import simplerace.*;

/**
 * Todo: min0対策
 * 案1 旗1と旗2の距離が近い場合、回り込んで旗を取るようにする
 * 実装方法 仮の旗を立てて、そこに向かわせる
 *
 */

public class AIController implements Controller, Constants {

    private final boolean DEBUG = false;

    private double targetAngle;

    private double targetDistance;

    private double targetSpeed;

    /**
     * 自分、1旗、2旗のangle 0 ~ pi
     */
    private double angle;

    /**
     * angleの2乗 0 ~ pi*pi
     */
    private double anglePow;

    private Vector2d fakeWaypoint;

    private double fakeAngle;

    private double brakingPoint;

    private Analyst analyst;

    private final double pipi = Math.PI * Math.PI;

    private final double defTurnSpeed = -2.564335;

    private final double collideDetection = 20.0/Math.sqrt(320000.0D);

    private double dis = 0.206;

    public AIController(){
        this.analyst = new Analyst();

        return;
    }

    public void reset(){
        this.analyst = new Analyst();
    }


    /**
     * フィールドを埋める
     * @param inputs センサ情報
     */
    private void setField(SensorModel inputs){
        this.targetAngle = inputs.getAngleToNextWaypoint();
        this.targetDistance = inputs.getDistanceToNextWaypoint();

        this.angle = this.getRadians(inputs);
        //System.err.println("angle " + Math.toDegrees(angle));
        this.anglePow = this.angle*this.angle;
        this.targetSpeed = this.defTurnSpeed * (anglePow/2.0 + 1.0);
        //System.err.println("tgtspeed      " + this.targetSpeed);
        /*
        if(!eq(this.nextWaypoint, inputs.getNextWaypointPosition())){
            this.setBrakingPoint(inputs);
        }
        */

        return;
    }

    /**
     * ブレーキングポイントを決める 今使っていません
     * @param inputs
     */
    private void setBrakingPoint(SensorModel inputs){
        double angle3 = (anglePow/pipi) * this.dis;
        //System.err.println("angle         " + Math.toDegrees(angle));

        this.brakingPoint = inputs.getDistanceToNextWaypoint() * (this.dis - angle3) + this.collideDetection;
        //System.err.println("brkpoint    " + this.brakingPoint);
        //System.err.println("raw         " + inputs.getDistanceToNextWaypoint());
        //System.err.println("speed         " + inputs.getSpeed());
        //System.err.println("dis - angle " + (this.dis - angle));
    }

    /**
     * isAbleToBrake 今からブレーキして目標速度まで落とすと止まり切れるか返す
     * @param inputs
     * @return
     */
    private boolean isAbleToBrake(SensorModel inputs){
        double finPoint = (inputs.getDistanceToNextWaypoint() - this.collideDetection) * Math.sqrt(320000.0D);
        double speed = inputs.getSpeed();
        double distance = 0;

        if(this.targetSpeed > speed) return true;

        while(true){
            if(speed <= this.targetSpeed) break;
            distance += speed;
            speed -= 0.2;
        }

        //System.err.println("nokori kyori  " + finPoint);
        //System.err.println("brakeDistance " + distance);

        if(distance > finPoint) return false;

        return true;
    }

    /**
     * 自身、一つ目の旗、二つ目の旗の角度を求める
     * 偽旗で角度を出すように
     * @param inputs
     * @return
     */
    private double getRadians(SensorModel inputs){
        Vector2d A = inputs.getPosition();
        Vector2d B = /*inputs.getNextWaypointPosition()*/ this.fakeWaypoint;
        Vector2d C = inputs.getNextNextWaypointPosition();
        Vector2d BA = new Vector2d(A.x - B.x, A.y - B.y);
        Vector2d BC = new Vector2d(C.x - B.x, C.y - B.y);
        //System.out.println(BA);
        //System.out.println(BC);
        //System.err.println("A " + A);
        //System.err.println("B " + B);
        //System.err.println("C " + C);
        double cosB = (BA.x*BC.x+BC.y*BA.y)/((Math.sqrt(Math.pow(BA.x,2.0)+Math.pow(BA.y,2.0)))*(Math.sqrt(Math.pow(BC.x,2.0)+Math.pow(BC.y,2.0))));
        //System.err.println(Math.acos(cosB));
        double rad = Math.acos(cosB);
        //System.err.println("deg " + Math.toDegrees(rad));

        return (rad > Math.PI) ? (Math.PI - rad) : rad;
    }

    /**
     * 偽の点を作る
     */
    private Vector2d createFakeWaypoint(SensorModel inputs){
        Vector2d T1 = inputs.getNextWaypointPosition();
        Vector2d T2 = inputs.getNextNextWaypointPosition();

        double T1T2Distance = Math.sqrt((T2.x - T1.x) * (T2.x - T1.x) + (T2.y - T1.y) * (T2.y - T1.y));
        double radian = Math.atan2(T2.y - T1.y,T2.x - T1.x);
        double distance = inputs.getDistanceToNextWaypoint()*Math.sqrt(320000.0D);

        double x = 1.0/(T1T2Distance/Math.sqrt(320000.0D)) / 2.0;
        Vector2d fakePoint = new Vector2d(T1.x - (Math.cos(radian) * distance * 0.35) + (Math.cos(radian)*this.collideDetection),
                                           T1.y - (Math.sin(radian) * distance * 0.35) + (Math.sin(radian)*this.collideDetection));

        //System.err.println("BDistance   = " + T1T2Distance/Math.sqrt(320000.0D));
        //System.err.println("angle     = " + Math.toDegrees(radian));
        //System.err.println("x         = " + x);
        //System.err.println("T1Point   = " + T1);
        //System.err.println("fakePoint = " + fakePoint);
        //System.err.println("diff      = " + Math.abs(fakePoint.x - T1.x) + ", " + Math.abs(fakePoint.y - T1.y));
        //System.err.println("diffDist  = " + Math.sqrt((fakePoint.x - T1.x) * (fakePoint.x - T1.x) + (fakePoint.y - T1.y) * (fakePoint.y - T1.y)));

        return fakePoint;
    }
    
    private double getAngleToFakeWaypoint(SensorModel inputs){
        Vector2d nextWp = this.fakeWaypoint;
        Vector2d position = inputs.getPosition();
        double xDiff = nextWp.x - position.x;
        double yDiff = nextWp.y - position.y;
        double angle = Math.atan2(yDiff, xDiff);
        angle = inputs.getOrientation() - angle;

        while(angle < -3.141592653589793D) {
            angle += 6.283185307179586D;
        }

        while(angle > 3.141592653589793D) {
            angle -= 6.283185307179586D;
        }

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
     *      回り込んで旗を取れるようにした 20.3
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

        //System.err.println(brakingPoint); //減速地点
        //System.err.println(this.targetDistance); //距離
        //System.err.println(inputs.getSpeed());   //スピード
        //System.err.println(this.targetAngle); //角度

        if(this.fakeAngle > 0){
            command = forwardleft;

            if(this.fakeAngle > 3.00) command = forward;

            if(!isAbleToBrake(inputs)){
                command = backwardleft;
                System.err.println("speed " + inputs.getSpeed());
            }
            /*
            if(this.targetDistance < this.brakingPoint){
                command = forwardleft;
                if(inputs.getSpeed() > this.defTurnSpeed){
                    command = backwardleft;
                }
            }
            */
        }else{
            command = forwardright;

            if(this.fakeAngle < -3.00) command = forward;

            if(!isAbleToBrake(inputs)) {
                command = forwardright;
                System.err.println("speed " + inputs.getSpeed());
            }
            /*
            if(this.targetDistance < this.brakingPoint){
                command = forwardright;
                if(inputs.getSpeed() > this.defTurnSpeed){
                    command = backwardright;
                }
            }
            */
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

