package vcsc.teamcode.component.arm.elbow;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum ElbowPose {
    BASKET(0.5),
    PREGRAB(0.45),
    GRAB(0.57),
    STOW(0.2);


    final double position;

    ElbowPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}

