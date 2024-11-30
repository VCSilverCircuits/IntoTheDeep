package vcsc.teamcode.component.claw;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class ClawActuator extends Actuator {
    Servo servo;
    double targetPosition;

    public ClawActuator(Servo clawServo) {
        servo = clawServo;
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