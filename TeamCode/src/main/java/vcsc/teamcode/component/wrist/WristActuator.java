package vcsc.teamcode.component.wrist;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import vcsc.core.abstracts.actuator.Actuator;
import vcsc.core.abstracts.state.State;

public class WristActuator extends Actuator {
    ServoImplEx wristRot;
    ServoImplEx wristPivot;
    double rotPosition;
    double pivotPosition;

    public WristActuator(HardwareMap hardwareMap) {
        wristRot = hardwareMap.get(ServoImplEx.class, "wristRot");
        wristPivot = hardwareMap.get(ServoImplEx.class, "wristPivot");
    }

    @Override
    public void loop() {
        wristRot.setPosition(rotPosition);
        wristPivot.setPosition(pivotPosition);
    }

    @Override
    public void updateState(State newState) {
        WristState wristState = (WristState) newState;
        rotPosition = wristState.getRot();
        pivotPosition = wristState.getPivot();
    }

}
