package vcsc.teamcode.actions.hang;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.hooks.HookState;

public class PreHang implements Action {
    ArmExtState extState;
    ArmRotState rotState;
    HookState hookState;

    public PreHang(
            ArmExtState extState,
            ArmRotState rotState,
            HookState hookState) {
        this.extState = extState;
        this.rotState = rotState;
        this.hookState = hookState;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
        extState.setPose(ArmExtPose.HANG);
        rotState.setPose(ArmRotPose.HANG);
        hookState.open();
    }

    @Override
    public boolean loop() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {

    }
}
