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
import vcsc.teamcode.component.wrist.WristState;

public class BasketPose implements Action {
    ArmRotState rotState;
    ArmExtState extState;
    ElbowState elbowState;
    WristState wristState;
    ActionBuilder seq;
    SetExtPose slidesIn;
    SetExtPose slidesOut;
    SetRotPose rotateUp;
    WristBasketPose wristBasketPose;
    SetElbowPose elbowOut;

    public BasketPose(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.extState = extState;
        this.elbowState = elbowState;
        this.wristState = wristState;
        slidesIn = new SetExtPose(extState, ArmExtPose.RETRACT);
        slidesOut = new SetExtPose(extState, ArmExtPose.BASKET);
        rotateUp = new SetRotPose(rotState, ArmRotPose.BASKET);
        wristBasketPose = new WristBasketPose(elbowState, wristState);
        elbowOut = new SetElbowPose(elbowState, ElbowPose.STRAIGHT);

        seq = new ActionBuilder(slidesIn)
                .then(rotateUp)
                .then(slidesOut)
                .then(wristBasketPose);
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Going to basket pose.");

        seq = new ActionBuilder();

        if (rotState.getPose() != ArmRotPose.BASKET) {
            seq.then(slidesIn)
                    .then(elbowOut);
        }

        seq.then(rotateUp)
                .then(slidesOut)
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
//        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
//        telemetry.addData("Stage", currentStage);
//        telemetry.addData("RotInAction", rotState.actuatorsInAction());
//        telemetry.addData("ExtInAction", extState.actuatorsInAction());
//        if (finished)
//            return false;
//        if (!rotState.actuatorsInAction() && !extState.actuatorsInAction()) {
//            if (currentStage == STAGE.ROTATING) {
//                extState.setPose(ArmExtPose.BASKET);
//                currentStage = STAGE.EXTENDING;
//            } else if (currentStage == STAGE.EXTENDING) {
//                elbowState.setPose(ElbowPose.BASKET);
//                wristState.setPose(WristPose.BASKET);
//                currentStage = STAGE.ELBOWING;
//            } else if (currentStage == STAGE.ELBOWING) {
//                finished = true;
//            }
//        }
//        return false;

    }

    @Override
    public boolean isFinished() {
        return seq.isFinished();
    }
}

