package vcsc.teamcode.component.hooks.actions;


import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.hooks.HookPose;
import vcsc.teamcode.component.hooks.HookState;

public class SetHookPose implements Action {
    HookState hookState;
    HookPose targetPose;

    public SetHookPose(HookState hookState, HookPose targetPose) {
        super();
        this.hookState = hookState;
        this.targetPose = targetPose;
    }

    @Override
    public void start() {
        hookState.setPose(targetPose);
    }

    @Override
    public void loop() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void cancel() {
    }

}
