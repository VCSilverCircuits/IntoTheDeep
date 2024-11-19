package vcsc.redesign.actuators;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.redesign.states.ClawState;

public class ClawActuator extends Actuator<ClawState> {
    ServoImplEx servo;
    double targetPosition;

    public ClawActuator(HardwareMap hardwareMap) {
        servo = hardwareMap.get(ServoImplEx.class, "claw");
    }

    @Override
    public void loop() {
        if (servo.getPosition() != targetPosition) {
            setInAction(true);
            servo.setPosition(targetPosition);
        } else {
            setInAction(false);
        }
    }

    @Override
    public void updateState(ClawState newState) {
        targetPosition = newState.getPosition();
    }

}