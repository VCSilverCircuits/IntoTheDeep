package vcsc.teamcode.actions.intake;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawPose;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class GrabGround implements Action {
    ElbowState elbowState;
    WristState wristState;
    ClawState clawState;
    ElapsedTime timer;
    boolean finished = true;
    int stage = -1;

    public GrabGround(ElbowState elbowState, WristState wristState, ClawState clawState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
        this.clawState = clawState;
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public void start() {
        finished = false;
        stage = 0;
        elbowState.setPose(ElbowPose.SPECIMEN_GROUND_GRAB);
        wristState.setRotPose(WristRotPose.SPECIMEN_GROUND_GRAB);
        timer.reset();
    }

    @Override
    public void loop() {
        if (isFinished()) {
            return;
        }
        if (stage == 0 && timer.time() > 100) {
            clawState.setPose(ClawPose.MOSTLY_CLOSED);
            stage = 1;
            timer.reset();
        }
        if (stage == 1 && timer.time() > 50) {
            elbowState.setPose(ElbowPose.STOW);
            stage = 2;
            timer.reset();
        }
        if (stage == 2 && timer.time() > 200) {
            wristState.setRotPose(WristRotPose.STOW);
            finished = true;
            stage = -1;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void cancel() {
        stage = -1;
        finished = true;
    }
}
