package vcsc.teamcode.component.wrist;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum WristPivotPose {
    FORWARD(0),
    TILT(0.42),
    REVERSE(0.6);

    final double position;

    WristPivotPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}
