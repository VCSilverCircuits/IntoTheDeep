package vcsc.teamcode.arm;

public enum WristPose {
    INTAKE(1, 0),
    PULL_OUT(0.65, 0),
    SCORE_HOOK(0.65, 0),
    SCORE_BASKET(0.5, 0);

    public final double x;
    public final double y;

    WristPose(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
