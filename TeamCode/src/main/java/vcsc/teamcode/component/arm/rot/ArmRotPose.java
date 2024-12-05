package vcsc.teamcode.component.arm.rot;

public enum ArmRotPose {
    INTAKE(0),
    BASKET(95),
    PRE_HANG(85);

    final double angle;

    ArmRotPose(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
