package vcsc.teamcode.component.arm.rot;

import static vcsc.teamcode.component.arm.rot.ArmRotActuator.DEGREES_PER_TICK;

import vcsc.core.abstracts.state.PoweredPIDFState;

public class ArmRotState extends PoweredPIDFState {
    ArmRotPose currentPose;

    public ArmRotState() {
        setPower(0);
        setPose(ArmRotPose.INTAKE);
    }

    @Override
    public double getRealPosition() {
        return super.getRealPosition() * DEGREES_PER_TICK;
    }

    public double getAngle() {
        return getTargetPosition() * DEGREES_PER_TICK;
    }

    public void setAngle(double angle) {
        setTargetPosition(angle / DEGREES_PER_TICK);
    }

    public ArmRotPose getPose() {
        return currentPose;
    }

    public void setPose(ArmRotPose pose) {
        setAngle(pose.getAngle());
        currentPose = pose;
    }
}