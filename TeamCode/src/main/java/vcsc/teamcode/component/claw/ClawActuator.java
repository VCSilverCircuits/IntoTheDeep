package vcsc.teamcode.component.claw;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class ClawActuator extends Actuator {
    // NOTE: DO NOT LEAVE THIS AS PUBLIC
    public ClawSensor clawSensor;
    Servo servo;
    double targetPosition;

    public ClawActuator(HardwareMap hardwareMap) {
        servo = hardwareMap.get(ServoImplEx.class, "claw");
        clawSensor = new ClawSensor(hardwareMap);
    }

    @Override
    public void loop() {
        servo.setPosition(ClawPose.MIN + (targetPosition * (ClawPose.MAX - ClawPose.MIN)));
    }

    @Override
    public void updateState(State newState) {
        ClawState clawState = (ClawState) newState;
        targetPosition = clawState.getPosition();
    }


}