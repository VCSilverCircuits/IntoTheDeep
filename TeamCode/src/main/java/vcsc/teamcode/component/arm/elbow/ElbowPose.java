package vcsc.teamcode.component.arm.elbow;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum ElbowPose {
    BASKET(0.63), // 0.5
    PREGRAB(0.63),
    PREGRAB_AUTO(0.55),
    STRAIGHT(0.5),
    OUT_OF_WAY(0.7),
    SPECIMEN(0.7),
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

