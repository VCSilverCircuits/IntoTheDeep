package vcsc.redesign.actuators;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.redesign.states.ClawState;

public class ClawActuator extends Actuator<ClawState> {
    Servo servo;
    double targetPosition;

    public ClawActuator(Servo clawServo) {
        servo = clawServo;
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