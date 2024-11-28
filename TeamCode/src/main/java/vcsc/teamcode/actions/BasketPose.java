package vcsc.teamcode.actions;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;

public class BasketPose implements Action {
    ArmRotState rotState;
    ArmExtState extState;
    ElbowState elbowState;
    private enum STAGE {
        ROTATING,
        EXTENDING,
        ELBOWING;
    }

    private boolean finished = false;
    private STAGE currentStage;

    public BasketPose(ArmRotState rotState, ArmExtState extState, ElbowState elbowState) {
        this.rotState = rotState;
        this.extState = extState;
        this.elbowState = elbowState;
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
        telemetry.addData("Stage",currentStage);
        telemetry.addData("RotInAction",rotState.actuatorsInAction());
        telemetry.addData("ExtInAction",extState.actuatorsInAction());
        if (finished)
            return false;
        if (!rotState.actuatorsInAction() && !extState.actuatorsInAction()) {
            if(currentStage == STAGE.ROTATING) {
                extState.setPose(ArmExtPose.BASKET);
                currentStage = STAGE.EXTENDING;
            } else if(currentStage == STAGE.EXTENDING) {
                elbowState.setPose(ElbowPose.BASKET);
                currentStage = STAGE.ELBOWING;
            } else {
                stop();
            }
        }
        return false;

    }


        @Override
    public boolean isFinished() {return finished;}

    @Override
    public void stop() {finished = true;}
}

