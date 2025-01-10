package vcsc.teamcode.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtState;

public class RetractSlidesUntilIn implements Action {
    ArmExtState armExtState;

    public RetractSlidesUntilIn(ArmExtState armExtState) {
        this.armExtState = armExtState;
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (armExtState.getCurrent() < 1000) {
            armExtState.setPower(-1);
        } else {
            armExtState.setPower(0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void cancel() {

    }
}
