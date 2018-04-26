package simplerace.e;
import simplerace.*;

public class CarInformation extends Object implements Constants {

    /**
     * 自身のセンサ情報が束縛されるフィールド.
     */
    private SensorModel aSensor;

    /**
     * 自身が狙うべき旗の位置と自身の角度が代入されるフィールド.
     */
    private double targetAngle;

    /**
     * 自身が狙うべき旗の位置が束縛されるフィールド.
     */
    private Vector2d targetFlag;

    /**
     * コンストラクタ.
     */
    public CarInformation(){
        return;
    }

    /**
     * setSensor
     * SensorModelのsetter
     * @param inputs センサ情報
     */
    protected void setSensor(SensorModel inputs){
        this.aSensor = inputs;
        return;
    }

    /**
     * setTargetAngle
     * targetAngleのsetter
     * @param aValue 旗との角度
     */
    protected void setTargetAngle(double aValue){
        this.targetAngle = aValue;
        return;
    }

    /**
     * setTargetFlag
     * targetFlagのsetter
     * @param aFlag 旗の座標情報
     */
    protected void setTargetFlag(Vector2d aFlag){
        this.targetFlag = aFlag;
        return;
    }

    /**
     * getSensor
     * センサ情報のgetter
     * @return センサ情報 : SensorModel
     */
    protected SensorModel getSensor(){
        return this.aSensor;
    }

    /**
     * getTargetAngle
     * 狙うべき旗との角度のgetter
     * @return 旗との角度 : double
     */
    protected double getTargetAngle(){
        return this.targetAngle;
    }

    /**
     * getTargetFlag
     * 狙うべき旗情報のgetter
     * @return 旗の情報 : double
     */
    protected Vector2d getTargetFlag(){
        return this.targetFlag;
    }

    /**
     * toString
     * 自信を文字列にして応答する
     * @return 自信を表す文字列
     */
    @Override
    public String toString(){
        return null;
    }

    /**
     * hashCode
     * 自身のハッシュ値を応答する
     * @return 自身のハッシュ値
     */
    @Override
    public int hashCode(){
        return super.hashCode();
    }
}
