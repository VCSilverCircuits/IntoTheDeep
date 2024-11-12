package vcsc.teamcode.arm;

public enum ArmPose {
    INTAKE(ArmExtensionPose.INTAKE, ArmRotationPose.INTAKE),
    PULL_OUT(ArmExtensionPose.PULL_OUT, ArmRotationPose.PULL_OUT),
    SCORE_HOOK(ArmExtensionPose.SCORE_HOOK, ArmRotationPose.SCORE_HOOK),
    SCORE_BASKET(ArmExtensionPose.SCORE_BASKET, ArmRotationPose.SCORE_BASKET);

    public final ArmExtensionPose extension;
    public final ArmRotationPose rotation;

    ArmPose(ArmExtensionPose extension, ArmRotationPose rotation) {
        this.extension = extension;
        this.rotation = rotation;

    }
}
