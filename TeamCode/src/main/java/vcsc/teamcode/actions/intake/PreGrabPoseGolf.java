package vcsc.teamcode.actions.intake;

import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class PreGrabPoseGolf extends PreGrabPose {

    public PreGrabPoseGolf(ElbowState elbowState, WristState wristState, ClawState clawState) {
        super(elbowState, wristState, clawState);
    }

    @Override
    public void start() {
        clawState.open();
        elbowState.setPose(ElbowPose.PREGRAB_AUTO);
        wristState.setRotPose(WristRotPose.PREGRAB);
        wristState.setPivotPose(WristPivotPose.AUTO_GOLF);
    }
}
