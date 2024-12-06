package vcsc.teamcode.actions;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.wrist.WristRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class Grab implements Action {
    ElbowState elbowState;
    WristState wristState;
    ClawState clawState;
    ElapsedTime timer;
    boolean finished = true;

    public Grab(ElbowState elbowState, WristState wristState, ClawState clawState) {
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
    }

    @Override
    public void loop() {
        if (timer.time() > 100 && !finished) {
            clawState.close();
            finished = true;
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
