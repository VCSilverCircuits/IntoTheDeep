package vcsc.teamcode.component.arm.elbow;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum ElbowPose {
    BASKET(0.63), // 0.5
    PREGRAB(0.6),
    PREGRAB_AUTO(0.5),
    STRAIGHT(0.65),
    SPECIMEN(0.57),
    WALL(0.52),
    HANG(0.9),
    GRAB(0.75),
    STOW(0);


    final double position;

    ElbowPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}

