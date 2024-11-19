package vcsc.redesign.actuators;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.redesign.states.ClawState;

public class WristActuator extends Actuator<ClawState> {
    ServoImplEx wristRot;
    ServoImplEx wristPivot;
    double targetPosition;

    public WristActuator(HardwareMap hardwareMap) {
        wristRot = hardwareMap.get(ServoImplEx.class, "wristX");
        wristPivot = hardwareMap.get(ServoImplEx.class, "wristY");
    }

    @Override
    public void loop() {
        if (wristRot.getPosition() != targetPosition) {
            setInAction(true);
            wristRot.setPosition(targetPosition);
        } else {
            setInAction(false);
        }
        if (wristPivot.getPosition() != targetPosition) {
            setInAction(true);
            wristPivot.setPosition(targetPosition);
        } else {
            setInAction(false);
        }
    }

    @Override
    public void updateState(ClawState newState) {
        targetPosition = newState.getPosition();
    }

}
