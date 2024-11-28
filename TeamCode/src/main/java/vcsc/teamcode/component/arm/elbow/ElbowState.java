package vcsc.teamcode.component.arm.elbow;

import vcsc.core.abstracts.State;

public class ElbowState extends State {
    double position = 0;

    public ElbowState() {super();
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
        notifyActuators();
    }

    public void setPose(ElbowPose position) {
        this.position = position.getPosition();
        notifyActuators();
    }
}

