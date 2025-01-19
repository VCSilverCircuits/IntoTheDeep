package vcsc.teamcode.component.arm.ext;

public enum ArmExtPose {
    RETRACT(0),
    BASKET(61),
    MAX_ROTATE(5),
    INTAKE(30),
    SPECIMEN_PRE_SCORE(2),
    SPECIMEN_SCORE(20),
    HANG(10);


    final double length;

    ArmExtPose(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }
}
