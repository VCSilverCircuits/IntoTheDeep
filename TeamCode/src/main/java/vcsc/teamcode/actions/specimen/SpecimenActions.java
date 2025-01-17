package vcsc.teamcode.actions.specimen;


import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.claw.ClawState;

public class SpecimenActions implements Action {
    ArmExtState armExtState;
    SpecimenPose specimenPose;
    ScoreSpecimen scoreSpecimen;
    ClawState clawState;

    Action currentAction;
    ElapsedTime clawTimer;

    boolean cancelled = false;
    boolean started = false;
    double clawDelay = 150;

    public SpecimenActions(ArmExtState armExtState, ClawState clawState, SpecimenPose specimenPose, ScoreSpecimen scoreSpecimen) {
        super();
        this.armExtState = armExtState;
        this.clawState = clawState;
        this.specimenPose = specimenPose;
        this.scoreSpecimen = scoreSpecimen;
        clawTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    }

    @Override
    public void start() {
        started = true;
        cancelled = false;
        if (armExtState.getPose() == ArmExtPose.SPECIMEN_PRE_SCORE) {
            scoreSpecimen.start();
            specimenPose.cancel();
            currentAction = scoreSpecimen;
        } else {
            specimenPose.start();
            scoreSpecimen.cancel();
            currentAction = specimenPose;
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
