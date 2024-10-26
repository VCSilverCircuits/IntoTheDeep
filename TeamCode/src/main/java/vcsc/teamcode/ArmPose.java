package vcsc.teamcode;

public enum ArmPose {
    INTAKE(1, 0),
    PULL_OUT(0.65, 0),
    SCORE_HOOK(0.65, 0),
    SCORE_BASKET(0.5, 0);

    public final double extension;
    public final double rotation;

    ArmPose(double extension, double rotation) {
        this.extension = extension;
        this.rotation = rotation;
    }
}
