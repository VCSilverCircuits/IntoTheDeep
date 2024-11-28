package vcsc.teamcode.component.arm.elbow;

public enum ElbowPose {
    BASKET(0.3),
    PREGRAB(0.45),
    GRAB(0.57);


    final double position;

    ElbowPose(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}

