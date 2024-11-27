package vcsc.teamcode.component.wrist;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.Actuator;
import vcsc.core.abstracts.State;

public class WristActuator extends Actuator {
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
    public void updateState(State newState) {
        WristState wristState = (WristState) newState;
        //targetPosition = wristState.getPosition();
    }

}
