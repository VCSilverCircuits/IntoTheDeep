package vcsc.teamcode.component.arm.ext;

public enum ArmExtPose {
    RETRACT(0),
    LOW_BASKET(21),
    BASKET(55), //61
    BASKET_AUTO(52),
    AUTO_GOLF(20),
    MAX_ROTATE(5),
    INTAKE(40),
    INTAKE_AUTO(21),
    SPECIMEN_PRE_SCORE(9),
    SPECIMEN_SCORE(21),
    HANG(18);


    final double length;

    ArmExtPose(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }
}
