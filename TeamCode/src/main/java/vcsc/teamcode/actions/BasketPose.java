package vcsc.teamcode.actions;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class BasketPose implements Action {
    ArmRotState rotState;
    ArmExtState extState;
    ElbowState elbowState;
    WristState wristState;
    private boolean finished = false;
    private STAGE currentStage;

    public BasketPose(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.extState = extState;
        this.elbowState = elbowState;
        this.wristState = wristState;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {
        finished = false;
        rotState.setPose(ArmRotPose.BASKET);
        currentStage = STAGE.ROTATING;

    }

    @Override
    public boolean loop() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addData("Stage", currentStage);
        telemetry.addData("RotInAction", rotState.actuatorsInAction());
        telemetry.addData("ExtInAction", extState.actuatorsInAction());
        if (finished)
            return false;
        if (!rotState.actuatorsInAction() && !extState.actuatorsInAction()) {
            if (currentStage == STAGE.ROTATING) {
                extState.setPose(ArmExtPose.BASKET);
                currentStage = STAGE.EXTENDING;
            } else if (currentStage == STAGE.EXTENDING) {
                elbowState.setPose(ElbowPose.BASKET);
                wristState.setPose(WristPose.BASKET);
                currentStage = STAGE.ELBOWING;
            } else if (currentStage == STAGE.ELBOWING) {
                stop();
            }
        }
        return false;

    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void stop() {
        finished = true;
    }

    private enum STAGE {
        ROTATING,
        EXTENDING,
        ELBOWING
    }
}

