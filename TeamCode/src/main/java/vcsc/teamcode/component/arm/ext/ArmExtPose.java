package vcsc.teamcode.component.arm.ext;

public enum ArmExtPose {
    BASKET(55);

    final double length;

    ArmExtPose(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }
}
