package vcsc.teamcode.component.arm.ext;

public enum ArmExtPose {
    RETRACT(0),
    BASKET(70), //61
    MAX_ROTATE(5),
    INTAKE(40),
    INTAKE_AUTO(30),
    SPECIMEN_PRE_SCORE(9),
    SPECIMEN_SCORE(30.5),
    HANG(18);


    final double length;

    ArmExtPose(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }
}
