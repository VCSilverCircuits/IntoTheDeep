package vcsc.teamcode.actions;

import static vcsc.teamcode.DebugConstants.WALL_ELBOW;
import static vcsc.teamcode.DebugConstants.WALL_WRIST_ROT;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristPivotPose;
import vcsc.teamcode.component.wrist.WristState;

public class PreGrabPoseWall implements Action {
    protected ElbowState elbowState;
    protected WristState wristState;
    protected ClawState clawState;

    public PreGrabPoseWall(ElbowState elbowState, WristState wristState, ClawState clawState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
        this.clawState = clawState;
    }

    @Override
    public void start() {
        clawState.open();
        elbowState.setPosition(WALL_ELBOW);
//        wristState.setRotPose(WristRotPose.WALL);
        wristState.setPivotPose(WristPivotPose.FORWARD);
        wristState.setRot(WALL_WRIST_ROT);
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
