package vcsc.teamcode.component.hooks;

public enum HookPose {
    OPEN(1, 0),
    HANG(0.5, 0.5);

    final double left, right;

    HookPose(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }
}
