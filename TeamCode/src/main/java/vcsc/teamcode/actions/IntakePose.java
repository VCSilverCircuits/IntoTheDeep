package vcsc.teamcode.actions;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawState;

public class IntakePose implements Action {
    ArmRotState rotState;
    ArmExtState extState;
    ClawState clawState;

    ActionBuilder seq;


    public IntakePose(ArmRotState rotState, ArmExtState extState, ClawState clawState) {
        this.rotState = rotState;
        this.extState = extState;
        this.clawState = clawState;

        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Going to basket pose.");
    }

    @Override
    public void start() {
        SetExtPose slidesIn = new SetExtPose(extState, ArmExtPose.MAX_ROTATE);
        SetExtPose slidesOut = new SetExtPose(extState, ArmExtPose.INTAKE);
        SetRotPose rotateDown = new SetRotPose(rotState, ArmRotPose.INTAKE);
        seq = new ActionBuilder(slidesIn)
                .then(rotateDown)
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
