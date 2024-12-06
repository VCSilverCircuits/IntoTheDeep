package vcsc.teamcode.actions.hang;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.hooks.HookState;

public class PreHang implements Action {
    ArmExtState extState;
    ArmRotState rotState;
    HookState hookState;
    ElbowState elbowState;

    public PreHang(
            ArmExtState extState,
            ArmRotState rotState,
            HookState hookState, ElbowState elbowState) {
        this.extState = extState;
        this.rotState = rotState;
        this.hookState = hookState;
        this.elbowState = elbowState;
    }

    @Override
    public void start() {
        elbowState.setPose(ElbowPose.HANG);
        extState.setPose(ArmExtPose.HANG);
        rotState.setPose(ArmRotPose.PRE_HANG);
        hookState.open();
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
