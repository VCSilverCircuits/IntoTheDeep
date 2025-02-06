package vcsc.teamcode.component.arm.ext;

import static vcsc.teamcode.component.arm.ext.ArmExtActuator.CM_PER_TICK;

import vcsc.core.abstracts.state.PoweredPIDFState;

public class ArmExtState extends PoweredPIDFState {
    ArmExtPose currentPose;

    public double getRealExtensionLength() {
        return getRealPosition() * CM_PER_TICK;
    }

    public double getExtensionLength() {
        return getTargetPosition() * CM_PER_TICK;
    }

    public void setExtensionLength(double length) {
        setTargetPosition(length / CM_PER_TICK);
    }

    public ArmExtPose getPose() {
        return currentPose;
    }

    public void setPose(ArmExtPose pose) {
        setExtensionLength(pose.getLength());
        currentPose = pose;
    }
}

