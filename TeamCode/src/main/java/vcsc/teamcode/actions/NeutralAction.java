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
    private boolean finished = false;

    public NeutralAction(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        this.rotState = rotState;
        this.extState = extState;

        this.elbowState = elbowState;
        this.wristState = wristState;
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Going to neutral pose");
        finished = false;
    }

    @Override
    public void start() {
        SetExtPose slidesIn = new SetExtPose(extState, ArmExtPose.RETRACT);
        SetRotPose rotateDown = new SetRotPose(rotState, ArmRotPose.INTAKE);

        seq = new ActionBuilder(slidesIn)
                .then(rotateDown);


        // I have like no faith in these but idk
       /* rotState.setAngle(0);
        extState.setExtensionLength(0);
        wristState.setPose(WristPose.DEFAULT);
        elbowState.setPose(ElbowPose.STOW);
        finished = false; */
        seq.start();
    }

    @Override
    public void loop() {seq.loop();}

    @Override
    public boolean isFinished() {
        return seq.isFinished();
    }

    @Override
    public void cancel() {
        seq.cancel();
    }

    private enum STAGE {
        RETRACT,
        ROTATE,
        ELBOW,
        CLAW

    }
}
