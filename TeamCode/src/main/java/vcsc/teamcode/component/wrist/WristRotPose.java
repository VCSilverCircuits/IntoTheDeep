package vcsc.teamcode.component.wrist;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum WristRotPose {
    STOW(1),
    OUT(0),
    PREGRAB(0.92),
    GRABBING(0.85),
    SPECIMEN(0.2),
    WALL(0.65), // 0.58
    SPECIMEN_GROUND_PREGRAB(0.78),
    SPECIMEN_GROUND_GRAB(0.75),
    STRAIGHT(0.6),
    BASKET(0.4); //0.3 -> 0.2

    final double position;

    WristRotPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}
