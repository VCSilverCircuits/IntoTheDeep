package vcsc.teamcode.component.arm.ext;

public enum ArmExtPose {
    BASKET(55),
    HANG(10);

    final double length;

    ArmExtPose(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }
}
