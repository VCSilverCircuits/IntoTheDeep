package vcsc.teamcode.component.arm.rot;

public enum ArmRotPose {
    INTAKE(0),
    HANG(0),
    BASKET(92),
    SPECIMEN(10),
    PRE_HANG(100);

    final double angle;

    ArmRotPose(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
