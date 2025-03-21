package vcsc.teamcode.component.hooks;

import vcsc.core.abstracts.state.State;

public class HookState extends State {
    double positionLeft = HookPose.OPEN.getLeft();
    double positionRight = HookPose.OPEN.getRight();

    public HookState() {
        open();
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
        notifyActuators();
    }

    public double getPositionRight() {
        return positionRight;
    }

    public void setPositionRight(double positionRight) {
        this.positionRight = positionRight;
        notifyActuators();
    }

    public void setPose(HookPose pose) {
        setPositions(pose.getLeft(), pose.getRight());
    }

    public boolean isOpen() {
        return getPositionLeft() == HookPose.OPEN.getLeft() && getPositionRight() == HookPose.OPEN.getRight();
    }

    public boolean isHang() {
        return getPositionLeft() == HookPose.HANG.getLeft() && getPositionRight() == HookPose.HANG.getRight();
    }

    public void open() {
        setPose(HookPose.OPEN);
    }

    public void hang() {
        setPose(HookPose.HANG);
    }
}
