package vcsc.teamcode.actions.intake;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class GrabSubmersible implements Action {
    ElbowState elbowState;
    WristState wristState;
    ClawState clawState;
    ElapsedTime timer;
    boolean finished = true;
    int stage = -1;

    public GrabSubmersible(ElbowState elbowState, WristState wristState, ClawState clawState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
        this.clawState = clawState;
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public void start() {
        elbowState.setPose(ElbowPose.GRAB);
        wristState.setRotPose(WristRotPose.GRABBING);
        timer.reset();
        finished = false;
        stage = 0;
    }

    @Override
    public void loop() {
        if (timer.time() > 400 && !finished && stage == 0) { //300
            clawState.close();
            timer.reset();
            stage = 1;
        }
        if (timer.time() > 50 && stage == 1) {
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

    }
}
