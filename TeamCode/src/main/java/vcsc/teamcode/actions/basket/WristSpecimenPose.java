package vcsc.teamcode.actions.basket;


import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class WristSpecimenPose implements Action {
    ElbowState elbowState;
    WristState wristState;

    public WristSpecimenPose(ElbowState elbowState, WristState wristState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
//        telemetry.addLine("Moving elbow and wrist to basket pose.");
        elbowState.setPose(ElbowPose.SPECIMEN);
        wristState.setPose(WristPose.SPECIMEN);
    }

    @Override
    public void loop() {
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void cancel() {

    }
}
