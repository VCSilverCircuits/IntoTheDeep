package vcsc.teamcode.component.arm.elbow;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.core.abstracts.Actuator;
import vcsc.core.abstracts.State;
import vcsc.teamcode.component.claw.ClawState;

public class ElbowActuator extends Actuator {
    Servo servo;
    double targetPosition;

    public ElbowActuator(Servo elbowServo) {
        servo = elbowServo;
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
        ElbowState elbowState = (ElbowState) newState;
        targetPosition = elbowState.getPosition();
    }
}
