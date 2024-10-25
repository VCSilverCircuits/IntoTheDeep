package vcsc.teamcode;

public enum WristPose {
    STOW(0, 0),
    INTAKE(1, 0),
    SCORING(0.32, 0);

    public final double x;
    public final double y;

    WristPose(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
