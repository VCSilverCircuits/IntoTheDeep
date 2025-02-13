package vcsc.teamcode.component.claw;

public enum ClawPose {
    OPEN(1),
    MOSTLY_CLOSED(0.12),
    CLOSED(0);

    public static final double MIN = 0.03;
    public static final double MAX = 0.7;
    final double pos;

    ClawPose(double position) {
        pos = position;
    }

    public double getPos() {
        return pos;
    }
}