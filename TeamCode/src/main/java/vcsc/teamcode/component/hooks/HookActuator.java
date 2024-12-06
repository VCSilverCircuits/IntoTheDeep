package vcsc.teamcode.component.hooks;

import com.qualcomm.robotcore.hardware.Servo;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class HookActuator extends Actuator {
    Servo hookLeft;
    Servo hookRight;
    double targetPositionLeft = HookPose.OPEN.getLeft();
    double targetPositionRight = HookPose.OPEN.getRight();

    public HookActuator(Servo hookLeft, Servo hookRight) {
        this.hookLeft = hookLeft;
        this.hookRight = hookRight;
    }

    @Override
    public void loop() {
        hookLeft.setPosition(targetPositionLeft);
        hookRight.setPosition(targetPositionRight);
    }

    @Override
    public void updateState(State newState) {
        HookState hookState = (HookState) newState;
        targetPositionLeft = hookState.getPositionLeft();
        targetPositionRight = hookState.getPositionRight();
    }
}
