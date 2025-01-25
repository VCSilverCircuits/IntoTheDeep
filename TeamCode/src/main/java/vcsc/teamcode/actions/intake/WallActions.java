package vcsc.teamcode.actions.intake;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.claw.ClawState;

public class WallActions implements Action {
    ElbowState elbowState;
    IntakePoseWall intakePoseWall;
    GrabWall grabWall;
    ClawState clawState;

    Action currentAction;
    ElapsedTime clawTimer;

    boolean cancelled = false;
    boolean started = false;
    double clawDelay = 150;

    public WallActions(ElbowState elbowState, ClawState clawState, IntakePoseWall intakePoseWall, GrabWall grabWall) {
        super();
        this.elbowState = elbowState;
        this.clawState = clawState;
        this.intakePoseWall = intakePoseWall;
        this.grabWall = grabWall;
        clawTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    }

    @Override
    public void start() {
        started = true;
        cancelled = false;
        if (elbowState.getPose() == ElbowPose.WALL) {
            grabWall.start();
            intakePoseWall.cancel();
            currentAction = grabWall;
        } else {
            intakePoseWall.start();
            grabWall.cancel();
            currentAction = intakePoseWall;
        }
    }

    @Override
    public void loop() {
        if (!started) {
            return;
        }
        if (currentAction != null) {
            currentAction.loop();
            if (currentAction.isFinished()) {
                currentAction = null;
                started = false;
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (currentAction != null) {
            return currentAction.isFinished();
        }
        return (clawTimer.time() > clawDelay);
    }

    @Override
    public void cancel() {
        cancelled = true;
        started = false;
        if (currentAction != null) {
            currentAction.cancel();
            currentAction = null;
        }
    }
}
