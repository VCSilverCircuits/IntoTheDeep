package vcsc.teamcode.component.wrist;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum WristRotPose {
    IN(0.95),
    OUT(0),
    PREGRAB(0.9),
    GRABBING(0.8),
    STRAIGHT(0.5),
    BASKET(0.3);

    final double position;

    WristRotPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}
