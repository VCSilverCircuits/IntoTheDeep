package vcsc.teamcode.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class PreGrabPose implements Action {
    protected ElbowState elbowState;
    protected WristState wristState;
    protected ClawState clawState;

    public PreGrabPose(ElbowState elbowState, WristState wristState, ClawState clawState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
        this.clawState = clawState;
    }

    @Override
    public void start() {
        clawState.open();
        elbowState.setPose(ElbowPose.PREGRAB);
        wristState.setRotPose(WristRotPose.PREGRAB);
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
