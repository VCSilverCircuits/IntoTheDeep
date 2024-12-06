package vcsc.teamcode.actions;


import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;

public class SetElbowPose implements Action {
    ElbowState elbowState;
    ElbowPose targetPose;

    public SetElbowPose(ElbowState elbowState, ElbowPose targetPose) {
        super();
        this.elbowState = elbowState;
        this.targetPose = targetPose;
    }

    @Override
    public void start() {
        elbowState.setPose(targetPose);
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
