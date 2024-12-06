package vcsc.teamcode.actions;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class NeutralAction implements Action {
    ArmRotState rotState;
    ArmExtState extState;

    ElbowState elbowState;
    WristState wristState;

    ActionBuilder seq;
    SetExtPose slidesIn;
    SetRotPose rotateDown;
    SetElbowPose elbowDown;
    SetWristPose wristDown;

    public NeutralAction(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.extState = extState;

        this.elbowState = elbowState;
        this.wristState = wristState;

        slidesIn = new SetExtPose(extState, ArmExtPose.RETRACT);
        rotateDown = new SetRotPose(rotState, ArmRotPose.INTAKE);
        elbowDown = new SetElbowPose(elbowState, ElbowPose.STOW);
        wristDown = new SetWristPose(wristState, WristPose.STOW);

        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Going to neutral pose");

        seq = new ActionBuilder(slidesIn)
                .then(rotateDown)
                .then(elbowDown)
                .then(wristDown);
    }

    @Override
    public void start() {
        seq = new ActionBuilder(elbowDown)
                .then(wristDown)
                .then(slidesIn)
                .then(rotateDown)
                .then(elbowDown);
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
