package vcsc.teamcode.actions.specimen;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.actions.NeutralActionSpecimen;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.ext.actions.SetExtPosePower;
import vcsc.teamcode.component.claw.ClawState;
import vcsc.teamcode.component.claw.actions.OpenClawAction;

public class ScoreSpecimen implements Action {
    SetExtPosePower extSlides;
    ArmExtState extState;
    ActionBuilder seq;
    OpenClawAction openClawAction;
    NeutralActionSpecimen neutralAction;

    public ScoreSpecimen(ArmExtState extState, ClawState clawState, NeutralActionSpecimen neutralAction) {
        this.extState = extState;
        extSlides = new SetExtPosePower(extState, ArmExtPose.SPECIMEN_SCORE, 1);
        openClawAction = new OpenClawAction(clawState);
        this.neutralAction = neutralAction;
        seq = new ActionBuilder();
    }


    @Override
    public void start() {
        seq = new ActionBuilder(extSlides)
                .then(openClawAction)
                .then(neutralAction);
        seq.start();
    }

    @Override
    public void loop() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("[ACTION] Score specimen is running.");
        seq.loop();
    }

    @Override
    public boolean isFinished() {
        return seq.isFinished();
    }

    @Override
    public void cancel() {

    }
}
