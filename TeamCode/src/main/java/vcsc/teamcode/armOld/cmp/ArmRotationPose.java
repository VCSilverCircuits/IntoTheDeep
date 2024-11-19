package vcsc.teamcode.armOld.cmp;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum ArmRotationPose {
    INTAKE(1),
    PULL_OUT(0.65),
    SCORE_HOOK(0.65),
    SCORE_BASKET(0.5);

    public final double rotation;

    ArmRotationPose(double rotation) {
        this.rotation = rotation;
    }
}