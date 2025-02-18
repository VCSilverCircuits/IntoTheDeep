package vcsc.teamcode.component.claw;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class ClawSensor extends Actuator {
    public static double BLOCK_GRABBED_DISTANCE = 15;
    public static double OVER_BLOCK_DISTANCE = 100;
    ColorRangeSensor colorRangeSensor;

    public ClawSensor(HardwareMap hardwareMap) {
        colorRangeSensor = hardwareMap.get(ColorRangeSensor.class, "clawSensor");
        colorRangeSensor.close();
    }

    @Override
    public void init() {
        super.init();
        colorRangeSensor.enableLed(false);
        colorRangeSensor.setGain(50);
    }

    public double getDistance() {
        return colorRangeSensor.getDistance(DistanceUnit.MM);
    }

    public int getColor() {
        return colorRangeSensor.argb();
    }

    public NormalizedRGBA getNormalizedColor() {
        return colorRangeSensor.getNormalizedColors();
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


    @Override
    public void loop() {
        colorRangeSensor.enableLed(overBlock());
    }

    @Override
    public void updateState(State newState) {

    }
}
