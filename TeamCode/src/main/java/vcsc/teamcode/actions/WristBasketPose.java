package vcsc.teamcode.actions;


import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import vcsc.core.GlobalTelemetry;
import vcsc.core.abstracts.action.Action;
import vcsc.teamcode.component.arm.elbow.ElbowPose;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.wrist.WristPose;
import vcsc.teamcode.component.wrist.WristState;

public class WristBasketPose implements Action {
    ElbowState elbowState;
    WristState wristState;

    public WristBasketPose(ElbowState elbowState, WristState wristState) {
        super();
        this.elbowState = elbowState;
        this.wristState = wristState;
    }

    @Override
    public void start() {
        MultipleTelemetry telemetry = GlobalTelemetry.getInstance();
        telemetry.addLine("Moving elbow and wrist to basket pose.");
        elbowState.setPose(ElbowPose.BASKET);
        wristState.setPose(WristPose.BASKET);
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
