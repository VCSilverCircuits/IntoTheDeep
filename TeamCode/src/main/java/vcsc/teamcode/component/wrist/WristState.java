package vcsc.teamcode.component.wrist;

import vcsc.core.abstracts.state.State;

public class WristState extends State {
    private double rotPosition;
    private double pivotPosition;

    public WristState() {
        super();
        setPose(WristPose.DEFAULT);
    }

    public double getRot() {
        return rotPosition;
    }

    public void setRot(double wristRotPosition) {
        this.rotPosition = wristRotPosition;
        notifyActuators();
    }

    public double getPivot() {
        return pivotPosition;
    }

    public void setPivot(double wristPivotPosition) {
        this.pivotPosition = wristPivotPosition;
        notifyActuators();
    }

    public void setRotPose(WristRotPose wristRotPose) {
        setRot(wristRotPose.getPosition());
    }

    public void setPivotPose(WristPivotPose wristPivotPose) {
        setPivot(wristPivotPose.getPosition());
    }

    public void setPose(WristPose wristPose) {
        setPivot(wristPose.getPivot());
        setRot(wristPose.getRot());
    }

}