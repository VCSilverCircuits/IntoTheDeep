package vcsc.teamcode.actions.basket;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.elbow.actions.SetElbowPose;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.arm.rot.actions.SetRotPose;
import vcsc.teamcode.component.wrist.WristState;

public class LowerBasketPose implements Action {
    ArmRotState rotState;
    ElbowState elbowState;
    WristState wristState;
    ActionBuilder seq;
    SetRotPose rotateUp;
    WristBasketPose wristBasketPose;
    SetElbowPose elbowOut;

    public LowerBasketPose(ArmRotState rotState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.elbowState = elbowState;
        this.wristState = wristState;
        rotateUp = new SetRotPose(rotState, ArmRotPose.BASKET);
        wristBasketPose = new WristBasketPose(elbowState, wristState);
        elbowOut = new SetElbowPose(elbowState, ElbowPose.STRAIGHT);
        seq = new ActionBuilder();
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Going to basket pose.");

        seq = new ActionBuilder();

        if (rotState.getPose() != ArmRotPose.BASKET) {
            seq.then(elbowOut);
        }
        seq.then(rotateUp)
                .then(wristBasketPose);
        seq.start();
    }

    @Override
    public void cancel() {
        seq.cancel();
    }

    @Override
    public void loop() {
        seq.loop();
    }

    @Override
    public boolean isFinished() {
        return seq.isFinished();
    }
}
