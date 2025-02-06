package vcsc.teamcode.component.wrist;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum WristPivotPose {
    FORWARD(0.5),
    TILT(0.42),
    AUTO_WALL(0.7),
    REVERSE(0.5), // TODO: Fully deprecate reversal
    MIN(0),
    MAX(1);

    final double position;

    WristPivotPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}
