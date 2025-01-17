package vcsc.teamcode.component.claw.actions;

import vcsc.teamcode.component.claw.ClawAction;
import vcsc.teamcode.component.claw.ClawPose;
import vcsc.teamcode.component.claw.ClawState;

public class OpenClawAction extends ClawAction {

    public OpenClawAction(ClawState state) {
        super(state);
    }


    @Override
    public void start() {
        clawState.setPosition(ClawPose.OPEN);
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
