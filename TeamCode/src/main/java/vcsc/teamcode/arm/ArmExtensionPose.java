package vcsc.teamcode.arm;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum ArmExtensionPose {
    INTAKE(1),
    PULL_OUT(0.65),
    SCORE_HOOK(0.65),
    SCORE_BASKET(0.5);

    public final double extension;

    ArmExtensionPose(double extension) {
        this.extension = extension;
    }
}
