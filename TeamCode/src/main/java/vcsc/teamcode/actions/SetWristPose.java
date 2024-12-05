package vcsc.teamcode.actions;


import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class SetWristPose implements Action {
    WristState wristState;
    WristPose targetPose;

    public SetWristPose(WristState wristState, WristPose targetPose) {
        super();
        this.wristState = wristState;
        this.targetPose = targetPose;
    }

    @Override
    public void start() {
        wristState.setPose(targetPose);
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
