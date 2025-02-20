package vcsc.teamcode.actions.specimen;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.actions.NeutralActionSpecimen;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.ext.actions.SetExtPosePower;
import vcsc.teamcode.component.claw.ClawPose;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.claw.actions.OpenClawAction;

public class ScoreSpecimen implements Action {
    final double SCORE_DELAY = 300;
    final double EMERGENCY_CANCEL_DELAY = 800;
    ElapsedTime delayTimer;
    ClawState clawState;
    SetExtPosePower extSlides;
    ArmExtState extState;
    ActionBuilder seq;
    OpenClawAction openClawAction;
    NeutralActionSpecimen neutralAction;
    Action currentAction;
    int stage = -1;

    public ScoreSpecimen(ArmExtState extState, ClawState clawState, NeutralActionSpecimen neutralAction) {
        this.extState = extState;
        this.clawState = clawState;
        extSlides = new SetExtPosePower(extState, ArmExtPose.SPECIMEN_SCORE, 1);
        openClawAction = new OpenClawAction(clawState);
        this.neutralAction = neutralAction;
        seq = new ActionBuilder();
        delayTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        currentAction = extSlides;
    }


    @Override
    public void start() {
        stage = 0;
        clawState.setPose(ClawPose.CLOSED);
        extSlides.start();
        currentAction = extSlides;
        seq = new ActionBuilder(openClawAction)
                .then(neutralAction);
        delayTimer.reset();
    }

    @Override
    public void loop() {
        if ((extSlides.isFinished() || delayTimer.time() > EMERGENCY_CANCEL_DELAY) && delayTimer.time() > SCORE_DELAY && stage == 0) {
            seq.start();
            stage = 1;
            currentAction = seq;
        }
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("[ACTION] Score specimen is running.");
        currentAction.loop();
    }

    @Override
    public boolean isFinished() {
        return currentAction.isFinished() && stage == 1;
    }

    @Override
    public void cancel() {
        currentAction.cancel();
    }
}
