package vcsc.teamcode.component.hooks;

import vcsc.core.abstracts.state.State;

public class HookState extends State {
    double positionLeft = HookPose.OPEN.getLeft();
    double positionRight = HookPose.OPEN.getRight();

    public HookState() {
        super();
    }

    public void setPositions(double positionLeft, double positionRight) {
        setPositionLeft(positionLeft);
        setPositionRight(positionRight);
    }

    public double getPositionLeft() {
        return positionLeft;
    }

    public void setPositionLeft(double positionLeft) {
        this.positionLeft = positionLeft;
    }

    public double getPositionRight() {
        return positionRight;
    }

    public void setPositionRight(double positionRight) {
        this.positionLeft = positionRight;
    }

    public void setPose(HookPose pose) {
        setPositions(pose.getLeft(), pose.getRight());
    }

    public void open() {
        setPose(HookPose.OPEN);
    }

    public void hang() {
        setPose(HookPose.HANG);
    }
}
