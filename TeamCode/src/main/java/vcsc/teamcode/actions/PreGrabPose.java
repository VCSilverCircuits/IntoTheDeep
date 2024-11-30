package vcsc.teamcode.actions;

import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.wrist.WristState;

public class PreGrabPose extends ArmAction {

    public PreGrabPose(ArmRotState rotState, ArmExtState extState, ElbowState elbowState, WristState wristState) {
        super(rotState, extState, elbowState, wristState);
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public boolean loop() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {

    }
}
