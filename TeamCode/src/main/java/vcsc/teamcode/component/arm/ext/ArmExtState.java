package vcsc.teamcode.component.arm.ext;

import static vcsc.teamcode.component.arm.ext.ArmExtActuator.CM_PER_TICK;

import vcsc.core.abstracts.PoweredPIDFState;
import vcsc.teamcode.component.arm.rot.ArmRotPose;

public class ArmExtState extends PoweredPIDFState {
    public double getExtensionLength() {
        return getTargetPosition() * CM_PER_TICK;
    }

    public void setExtensionLength(double length) {
        setTargetPosition(length / CM_PER_TICK);
    }

    public void setPose(ArmExtPose pose) {
        setExtensionLength(pose.getLength());
    }
}
