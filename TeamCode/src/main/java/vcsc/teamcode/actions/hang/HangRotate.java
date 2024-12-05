package vcsc.teamcode.actions.hang;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.hooks.HookState;

public class HangRotate implements Action {
    ArmExtState extState;
    ArmRotState rotState;
    HookState hookState;

    public HangRotate(
            ArmExtState extState,
            ArmRotState rotState,
            HookState hookState) {
        this.extState = extState;
        this.rotState = rotState;
        this.hookState = hookState;
    }

    @Override
    public void start() {
        rotState.setPose(ArmRotPose.PRE_HANG);
        hookState.hang();
        extState.setPose(ArmExtPose.INTAKE);
    }

    @Override
    public void loop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void cancel() {

    }

}
