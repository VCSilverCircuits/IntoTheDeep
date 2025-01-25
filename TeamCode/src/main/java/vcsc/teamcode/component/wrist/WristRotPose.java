package vcsc.teamcode.component.wrist;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum WristRotPose {
    STOW(0.95),
    OUT(0),
    PREGRAB(0.8),
    GRABBING(0.75),
    SPECIMEN(0.08),
    WALL(0.55), // 0.58
    STRAIGHT(0.5),
    BASKET(0.3); //0.3 -> 0.2

    final double position;

    WristRotPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}
