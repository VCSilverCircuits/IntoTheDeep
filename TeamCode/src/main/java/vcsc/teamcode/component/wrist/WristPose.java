package vcsc.teamcode.component.wrist;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum WristPose {
    DEFAULT(WristPivotPose.FORWARD, WristRotPose.STRAIGHT),
    STOW(WristPivotPose.FORWARD, WristRotPose.IN),
    BASKET(WristPivotPose.REVERSE, WristRotPose.BASKET);

    final double pivot;
    final double rot;

    WristPose(double pivot, double rot) {
        this.pivot = pivot;
        this.rot = rot;
    }

    WristPose(WristPivotPose pivot, WristRotPose rot) {
        this.pivot = pivot.getPosition();
        this.rot = rot.getPosition();
    }

    public double getPivot() {
        return pivot;
    }

    public double getRot() {
        return rot;
    }
}
