package vcsc.teamcode.component.wrist;

import vcsc.core.abstracts.State;

public class WristState extends State {
    private double wristRotPosition;
    private double wristPivotPosition;

    public WristState() {
        super();
    }

    public double getWristRotPosition() {
        return wristRotPosition;
    }

    public void setWristRotPosition(double wristRotPosition) {
        this.wristRotPosition = wristRotPosition;
    }

    public double getWristPivotPosition() {
        return wristPivotPosition;
    }

    public void setWristPivotPosition(double wristPivotPosition) {
        this.wristPivotPosition = wristPivotPosition;
    }
}