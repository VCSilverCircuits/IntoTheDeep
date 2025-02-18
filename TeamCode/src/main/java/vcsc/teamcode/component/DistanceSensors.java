package vcsc.teamcode.component;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class DistanceSensors extends Actuator {
    public static double DISTANCE_SENSOR_SPACING = 150;
    DistanceSensor leftSensor;
    DistanceSensor rightSensor;

    public DistanceSensors(HardwareMap hardwareMap) {
        leftSensor = hardwareMap.get(Rev2mDistanceSensor.class, "leftSensor");
        rightSensor = hardwareMap.get(Rev2mDistanceSensor.class, "rightSensor");
    }

    public double getLeftDistance() {
        return leftSensor.getDistance(DistanceUnit.MM);
    }

    public double getRightDistance() {
        return rightSensor.getDistance(DistanceUnit.MM);
    }

    public double getAverageDistance() {
        return (getLeftDistance() + getRightDistance()) / 2;
    }

    public double getAngle() {
        return Math.atan2(getRightDistance() - getLeftDistance(), DISTANCE_SENSOR_SPACING);
    }

    @Override
    public void loop() {

    }

    @Override
    public void updateState(State newState) {

    }
}
