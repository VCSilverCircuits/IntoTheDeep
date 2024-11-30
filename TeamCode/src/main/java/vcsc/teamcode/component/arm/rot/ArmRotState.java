package vcsc.teamcode.component.arm.rot;

import static vcsc.teamcode.component.arm.rot.ArmRotActuator.DEGREES_PER_TICK;

import vcsc.core.abstracts.state.PoweredPIDFState;

public class ArmRotState extends PoweredPIDFState {
    public ArmRotState() {
        setPower(0);
    }

    public double getAngle() {
        return getTargetPosition() * DEGREES_PER_TICK;
    }

    public void setAngle(double angle) {
        setTargetPosition(angle / DEGREES_PER_TICK);
    }

    public void setPose(ArmRotPose pose) {
        setAngle(pose.getAngle());

    }
}