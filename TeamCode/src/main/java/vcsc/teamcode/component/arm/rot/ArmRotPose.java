package vcsc.teamcode.component.arm.rot;

public enum ArmRotPose {
    INTAKE(0),
    HANG(-10),
    BASKET(92),
    SPECIMEN(86.5),
    PRE_HANG(100);

    final double angle;

    ArmRotPose(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
