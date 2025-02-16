package vcsc.teamcode.component.claw;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import vcsc.core.abstracts.state.State;

public class ClawSensor extends State {
    public static double BLOCK_GRABBED_DISTANCE = 50;
    public static double OVER_BLOCK_DISTANCE = 50;
    ColorRangeSensor colorRangeSensor;

    public ClawSensor(HardwareMap hardwareMap) {
        colorRangeSensor = hardwareMap.get(ColorRangeSensor.class, "colorRangeSensor");
    }

    public double getDistance() {
        return colorRangeSensor.getDistance(DistanceUnit.MM);
    }

    public int getColor() {
        return colorRangeSensor.argb();
    }

    public int getRed() {
        return colorRangeSensor.red();
    }

    public int getGreen() {
        return colorRangeSensor.green();
    }

    public int getBlue() {
        return colorRangeSensor.blue();
    }

//    public Block.COLOR getBlockColor() {
//        int red = getRed();
//        int green = getGreen();
//        int blue = getBlue();
////        if (getColor() < Block.COLOR.RED.)
//    }

    public boolean overBlock() {
        return getDistance() < OVER_BLOCK_DISTANCE;
    }

    public boolean hasBlock() {
        return getDistance() < BLOCK_GRABBED_DISTANCE;
    }


}
