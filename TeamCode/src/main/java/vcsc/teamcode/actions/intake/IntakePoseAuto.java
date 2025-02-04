package vcsc.teamcode.actions.intake;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.ext.actions.SetExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.arm.rot.actions.SetRotPose;
import vcsc.teamcode.component.claw.ClawState;

public class IntakePoseAuto implements Action {
    ArmRotState rotState;
    ArmExtState extState;
    ClawState clawState;

    ActionBuilder seq;
    SetExtPose slidesIn;
    SetExtPose slidesOut;
    SetRotPose rotateDown;

    PreGrabPose preGrabPose;


    public IntakePoseAuto(ArmRotState rotState, ArmExtState extState, ClawState clawState, PreGrabPose preGrabPose) {
        this.rotState = rotState;
        this.extState = extState;
        this.clawState = clawState;
        this.preGrabPose = preGrabPose;

        slidesIn = new SetExtPose(extState, ArmExtPose.RETRACT);
        slidesOut = new SetExtPose(extState, ArmExtPose.INTAKE_AUTO);
        rotateDown = new SetRotPose(rotState, ArmRotPose.INTAKE);

        /*seq = new ActionBuilder(slidesIn)
                .then(rotateDown)
                .then(slidesOut)
                .then(preGrabPose);*/
        seq = new ActionBuilder();

        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Going to basket pose.");
    }

    @Override
    public void start() {
        if (extState.getPose() == ArmExtPose.BASKET) {
            return;
        }
        seq = new ActionBuilder();

        if (rotState.getPose() != ArmRotPose.INTAKE) {
            seq.then(slidesIn);
        }

        seq.then(rotateDown)
                .then(preGrabPose)
                .then(slidesOut);
        seq.start();
    }

    @Override
    public void loop() {
        seq.loop();
    }

    @Override
    public boolean isFinished() {
        return seq.isFinished();
    }

    @Override
    public void cancel() {
        seq.cancel();
    }
}
