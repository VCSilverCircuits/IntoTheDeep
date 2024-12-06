package vcsc.teamcode.component.arm.elbow;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class ElbowActuator extends Actuator {
    Servo servo;
    double targetPosition;

    public ElbowActuator(Servo elbowServo) {
        servo = elbowServo;
    }

    @Override
    public void loop() {
        servo.setPosition(targetPosition);
    }

    @Override
    public void updateState(State newState) {
        ElbowState elbowState = (ElbowState) newState;
        targetPosition = elbowState.getPosition();
    }
}
