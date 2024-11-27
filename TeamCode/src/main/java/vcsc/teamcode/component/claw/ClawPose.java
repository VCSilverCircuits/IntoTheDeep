package vcsc.teamcode.component.claw;

public enum ClawPose {
    OPEN(0.55),
    CLOSED(0.4);

    final double pos;

    ClawPose(double pos) {
        this.pos = pos;
    }

    public double getPos() {
        return pos;
    }
}
