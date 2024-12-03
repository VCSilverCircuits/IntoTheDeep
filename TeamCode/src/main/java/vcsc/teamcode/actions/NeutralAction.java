package vcsc.teamcode.actions;

import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class NeutralAction implements Action {
    ArmRotState rotState;
    ArmExtState extState;

    ElbowState elbowState;
    WristState wristState;
    private boolean finished = false;
    private BasketPose.STAGE currentStage;

    public NeutralAction(ArmRotState rotState, ArmExtState extState, ElbowState elbowState,WristState wristState) {
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
        // I have like no faith in these but idk
        rotState.setAngle(0);
        extState.setExtensionLength(0);
        wristState.setPose(WristPose.DEFAULT);
        elbowState.setPose(ElbowPose.STOW);
        finished = false;
    }

    @Override
    public boolean loop() {
        if (finished)
            return false;
        if (!rotState.actuatorsInAction() && !extState.actuatorsInAction()) {
            stop();
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void stop() { finished = true;}

    private enum STAGE {
        RETRACT,
        ROTATE,
        ELBOW,
        CLAW;

    }
}
