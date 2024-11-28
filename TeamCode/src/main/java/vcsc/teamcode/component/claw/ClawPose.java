package vcsc.teamcode.component.claw;

public enum ClawPose {
    OPEN(0.55),
    CLOSED(0.4);

    final double pos;

    ClawPose(double position) {
        pos = position;
    }

    public double getPos() {
        return pos;
    }
}
