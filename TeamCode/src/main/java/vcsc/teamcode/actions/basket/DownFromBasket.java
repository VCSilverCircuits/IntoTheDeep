package vcsc.teamcode.actions.basket;


import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.elbow.actions.SetElbowPose;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.ext.actions.SetExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.arm.rot.actions.SetRotPose;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;
import vcsc.teamcode.component.wrist.actions.SetWristPose;

public class DownFromBasket implements Action {
    ArmRotState rotState;
    ArmExtState extState;

    ElbowState elbowState;
    WristState wristState;

    ActionBuilder seq;
    SetExtPose slidesIn;
    SetRotPose rotateDown;
    SetElbowPose elbowDown;
    SetElbowPose elbowUp;
    SetWristPose wristDown;

    public DownFromBasket(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.extState = extState;

        this.elbowState = elbowState;
        this.wristState = wristState;

        slidesIn = new SetExtPose(extState, ArmExtPose.RETRACT);
        rotateDown = new SetRotPose(rotState, ArmRotPose.INTAKE);
        elbowDown = new SetElbowPose(elbowState, ElbowPose.STOW);
        elbowUp = new SetElbowPose(elbowState, ElbowPose.STRAIGHT);
        wristDown = new SetWristPose(wristState, WristPose.STOW);

        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
//        telemetry.addLine("Going to neutral pose");

        seq = new ActionBuilder(slidesIn)
                .then(rotateDown)
                .then(elbowDown)
                .then(wristDown);
    }

    @Override
    public void start() {

        seq = new ActionBuilder(elbowUp)
                .then(wristDown)
                .then(slidesIn)
                .then(elbowDown)
                .then(rotateDown);
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
