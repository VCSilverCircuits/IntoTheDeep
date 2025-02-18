package vcsc.teamcode.actions.intake;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class PreGrabPoseSpecimenGround implements Action {
    protected ElbowState elbowState;
    protected WristState wristState;
    protected ClawState clawState;

    public PreGrabPoseSpecimenGround(ElbowState elbowState, WristState wristState, ClawState clawState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
        this.clawState = clawState;
    }

    @Override
    public void start() {
        clawState.open();
        elbowState.setPose(ElbowPose.SPECIMEN_GROUND_PREGRAB);
//        wristState.setRotPose(WristRotPose.WALL);
        wristState.setPivotPose(WristPivotPose.FORWARD);
        wristState.setRotPose(WristRotPose.SPECIMEN_GROUND_PREGRAB);
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
