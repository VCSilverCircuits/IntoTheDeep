package vcsc.teamcode.component.claw;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.core.abstracts.Actuator;
import vcsc.core.abstracts.State;

public class ClawActuator extends Actuator {
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
    public void updateState(State newState) {
        ClawState clawState = (ClawState) newState;
        targetPosition = clawState.getPosition();
    }

}