package vcsc.teamcode.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawState;

public class IntakePose implements Action {
    ArmRotState rotState;
    ArmExtState extState;
    ClawState clawState;

    private boolean finished = false;

    public IntakePose(ArmRotState rotState, ArmExtState extState, ClawState clawState) {
        this.rotState = rotState;
        this.extState = extState;
        this.clawState = clawState;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
        finished = false;
        rotState.setPose(ArmRotPose.INTAKE);
        clawState.open();
    }

    @Override
    public boolean loop() {
        if (finished)
            return false;
        if (!rotState.actuatorsInAction() && !clawState.actuatorsInAction()) {
            stop();
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void stop() {
        finished = true;
    }
}
